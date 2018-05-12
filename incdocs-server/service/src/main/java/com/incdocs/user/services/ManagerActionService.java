package com.incdocs.user.services;

import com.incdocs.model.response.Response;
import com.incdocs.user.helper.ManagerActionsManagementHelper;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController("managerActionService")
@RequestMapping("/incdocs/manager")
public class ManagerActionService {

    @Autowired
    private ManagerActionsManagementHelper managerActionsManagementHelper;

    @Autowired
    private UserManagementHelper userManagementHelper;

    @GetMapping("/portfolio/details")
    public Response getPortfolio(@RequestHeader(value = "incdocsID") String incdocsID) {
        return managerActionsManagementHelper.getPortfolio(incdocsID);
    }

    @DeleteMapping("/membermapping/delete")
    public boolean deleteMemberToCompanyMapping(@RequestHeader(value = "incdocsID") String incdocsID,
                                                @RequestParam(value = "memberId", required = false) String memberID,
                                                @RequestParam(value = "companyId", required = false) String companyID) throws ApplicationException {
        return userManagementHelper.deleteUserEntitlement(memberID, companyID);
    }

    @PutMapping("/membermapping/add")
    @Transactional
    public boolean addMemberToCompanyMapping(@RequestHeader(value = "incdocsID") String incdocsID,
                                             @RequestParam(value = "memberId", required = false) String memberID,
                                             @RequestParam(value = "companyId", required = false) String companyID) throws ApplicationException {
        userManagementHelper.createUserEntitlement(memberID, companyID);
        return true;
    }
}
