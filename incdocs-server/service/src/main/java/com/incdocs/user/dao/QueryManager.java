package com.incdocs.user.dao;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component("queryManager")
public class QueryManager implements InitializingBean{
    enum Sql {
        SEL_USER_ACTION_ON_ENTITIES_SQL("select_user_action_on_entities"),
        SEL_ROLES("select_roles"),
        SEL_ROLE_BY_ID("select_role_by_id"),
        SEL_ACTIONS_FOR_ROLE("select_actions_for_role"),
        ;
        private String key;

        Sql(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    @Resource(name="queryMap")
    private Map<String,String> queryMap;

    public String getSQL(QueryManager.Sql sql) {
        return queryMap.get(sql.getKey());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(queryMap);
    }
}
