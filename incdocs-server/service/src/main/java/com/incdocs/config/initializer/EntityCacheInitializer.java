package com.incdocs.config.initializer;

import com.incdocs.cache.AppCacheManager;
import com.incdocs.cache.CacheName;
import com.incdocs.entity.dao.EntityDAO;
import com.incdocs.model.domain.Entity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("entityCacheInitializer")
public class EntityCacheInitializer implements InitializingBean{

    @Autowired
    private AppCacheManager appCacheManager;

    @Autowired
    private EntityDAO entityDAO;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Entity> allEntities = entityDAO.getAllEntities();
        if (CollectionUtils.isNotEmpty(allEntities)) {
            allEntities.forEach(entity -> appCacheManager.put(CacheName.ENTITY,
                    entity.getEntityID(), entity));
        }
    }
}
