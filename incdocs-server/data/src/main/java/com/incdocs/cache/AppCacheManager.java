package com.incdocs.cache;

import com.incdocs.model.domain.Action;
import com.incdocs.model.domain.Entity;
import com.incdocs.model.domain.Role;
import com.incdocs.model.domain.User;
import com.incdocs.model.response.RoleActions;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.search.expression.Criteria;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.incdocs.cache.CacheName.*;


@Component("appCacheManager")
public class AppCacheManager implements InitializingBean{
    @Autowired
    private org.springframework.cache.CacheManager cacheManager;

    private Map<CacheName, TypedCache> appCaches = new HashMap<>();

    class TypedCache<K, V> {
        private final Cache cache;

        public TypedCache(Cache cache) {
            this.cache = cache;
        }

        public V getValue(K key) {
            Cache.ValueWrapper val = cache.get(key);
            return (val != null ? (V) val.get(): null);
        }

        public void put(K key, V value) {
            cache.put(key, value);
        }

        public V putIfAbsent(K key, V value) {
            Cache.ValueWrapper  val = cache.putIfAbsent(key, value);
            return (val !=null ? (V) val.get(): null);
        }

        public void evict(K key) {
            cache.evict(key);
        }

        public Ehcache getNativeCache() {
            return (Ehcache)cache.getNativeCache();
        }
    }

    private TypedCache<String, User> userCache;
    private TypedCache<String, Entity> entityCache;
    private TypedCache<String, List<Role>> entityRoleCache;
    private TypedCache<Integer, Role> roleCache;
    private TypedCache<Integer, List<Action>> roleActionCache;

    public <K,V> V getValue(CacheName cacheName, K key) {
        TypedCache<K, V> typedCache = getTypedCache(cacheName);
        return typedCache.getValue(key);
    }

    public <K,V> void put(CacheName cacheName, K key, V value) {
        TypedCache<K, V> typedCache = getTypedCache(cacheName);
        typedCache.put(key, value);
    }

    public <K, V> V putIfAbsent(CacheName cacheName, K key, V value) {
        TypedCache<K, V> typedCache = getTypedCache(cacheName);
        return typedCache.getValue(key);
    }

    public <K, V> void evict(CacheName cacheName, K key) {
        TypedCache<K, V> typedCache = getTypedCache(cacheName);
        typedCache.evict(key);
    }

    public <K, V> TypedCache<K, V> getTypedCache(CacheName cacheName) {
        TypedCache<K, V> typedCache = appCaches.get(cacheName);
        if (typedCache ==  null) {
            throw new RuntimeException("No Such cache = "+cacheName);
        }
        return typedCache;
    }

    public <V,T> List<V>
    queryCacheValues(CacheName cacheName, Criteria criteria) {
        Ehcache cache = getTypedCache(cacheName).getNativeCache();
        Query query = cache.createQuery()
                .includeKeys()
                .includeValues()
                .addCriteria(criteria);
        Results results = query.execute();
        return results.all()
                .stream()
                .map(result -> (V) result.getValue())
                .collect(Collectors.toList());
    }

    public <T> Attribute<T>
    createSearchAttribute(CacheName cacheName, CacheSearchAttributes searchAttributes) {
        Ehcache cache = getTypedCache(cacheName).getNativeCache();
        return cache.getSearchAttribute(searchAttributes.name());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userCache = new TypedCache<>(cacheManager.getCache(USER.getName()));
        entityCache = new TypedCache<>(cacheManager.getCache(ENTITY.getName()));
        entityRoleCache = new TypedCache<>(cacheManager.getCache(ENTITY_ROLES.getName()));
        roleCache = new TypedCache<>(cacheManager.getCache(ROLE.getName()));
        roleActionCache = new TypedCache<>(cacheManager.getCache(ROLE_ACTIONS.getName()));
        appCaches.put(USER, userCache);
        appCaches.put(ENTITY, entityCache);
        appCaches.put(ENTITY_ROLES, entityRoleCache);
        appCaches.put(ROLE, roleCache);
        appCaches.put(ROLE_ACTIONS, roleActionCache);
    }
}
