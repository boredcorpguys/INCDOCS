package com.incdocs.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component("queryManager")
public class QueryManager implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(QueryManager.class);

    @Resource(name = "queryMap")
    private Map<String, String> queryMap;

    public String getSQL(QueryManager.Sql sql) {
        return queryMap.get(sql.getKey());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug(String.valueOf(queryMap));
    }

    public enum Sql {
        SEL_USER("select_user"),
        SEL_ALL_USERS("select_all_users"),
        SEL_USER_ENTITLEMENTS("select_user_entitlement"),
        UPD_USER("modify_user"),
        INSERT_USER("insert_user"),
        INS_USER_ENTITLEMENTS("insert_user_entitlement"),

        SEL_ROLE("select_role"),
        SEL_ALL_ROLES("select_all_roles"),
        SEL_ACTIONS_FOR_ROLE("select_actions_for_role"),

        SEL_ENTITY("select_entity"),
        SEL_ALL_ENTITIES("select_all_entities"),
        SEL_ROLES_FOR_ENTITY("select_roles_for_entity"),
        SEL_ENTITIES_BY_PARENT("select_entities_by_parent"),
        SEL_ENTITIES_BY_NAME("select_entities_by_name"),
        SEL_ENTITIES_FOR_USER("select_entities_for_user"),

        INSERT_COMPANY("insert_company"),
        INSERT_COMPANY_ROLE("insert_company_role");

        private String key;

        Sql(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
