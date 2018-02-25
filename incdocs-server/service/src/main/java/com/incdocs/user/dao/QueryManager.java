package com.incdocs.user.dao;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component("queryManager")
public class QueryManager implements InitializingBean {
    enum Sql {
        SEL_USER_ENTITLEMENTS("select_user_entitlement"),
        SEL_USER("select_user"),
        UPD_USER("modify_user"),
        SEL_ROLES("select_roles"),
        SEL_ROLE_BY_ID("select_role_by_id"),
        SEL_ACTIONS_FOR_ROLE("select_actions_for_role"),
        SEL_ENTITY("select_entity"),
        SEL_ENTITIES_BY_PARENT("select_entities_by_parent"),
        SEL_RESOURCE_GROUP("select_resource_group"),
        SEL_ENTITY_ROLES("select_entities_roles"),
        SEL_ENTITIES_BY_NAME("select_entities_by_name");
        private String key;

        Sql(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    @Resource(name = "queryMap")
    private Map<String, String> queryMap;

    public String getSQL(QueryManager.Sql sql) {
        return queryMap.get(sql.getKey());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(queryMap);
    }
}
