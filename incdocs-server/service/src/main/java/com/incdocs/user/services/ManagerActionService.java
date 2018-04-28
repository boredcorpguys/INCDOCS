package com.incdocs.user.services;

import com.incdocs.model.response.Response;
import com.incdocs.user.helper.ManagerActionsManagementHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("managerActionService")
@RequestMapping("/incdocs/manager")
public class ManagerActionService {

    @Autowired
    private ManagerActionsManagementHelper managerActionsManagementHelper;

    @GetMapping("/portfolio/details")
    public Response getPortfolio(@RequestHeader(value = "incdocsID") String incdocsID) {
        return managerActionsManagementHelper.getPortfolio(incdocsID);
    }

}
