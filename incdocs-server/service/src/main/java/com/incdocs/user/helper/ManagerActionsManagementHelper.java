package com.incdocs.user.helper;

import com.incdocs.model.domain.User;
import com.incdocs.model.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("managerActionsManagementHelper")
public class ManagerActionsManagementHelper {

    @Autowired
    private UserManagementHelper userManagementHelper;

    public Response getPortfolio(String incdocsID) {
        User user = userManagementHelper.getUser(incdocsID);

        return null;
    }
}
