package com.incdocs.user.services;

import com.incdocs.model.domain.User;
import com.incdocs.model.response.Response;
import com.incdocs.model.response.SearchSubordinateResponse;
import com.incdocs.user.helper.ManagerActionsManagementHelper;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.UriUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/search/subordinates")
    public @ResponseBody
    Response<SearchSubordinateResponse> searchSubordindates(@RequestHeader(value = "incdocsID") String incdocsID) {
        List<User> armList = userManagementHelper.getSubordinates(incdocsID);
        Response.Metadata metadata1 = new Response.Metadata()
                .setColDisplayName("ARM Name")
                .setColName("name")
                .setColType("string")
                .setVisibility(true);

        Response.Metadata metadata2 = new Response.Metadata()
                .setColDisplayName("ARM ID")
                .setColName("empID")
                .setColType("string")
                .setVisibility(true);

        List<SearchSubordinateResponse> rows = armList.stream()
                .map(user -> new SearchSubordinateResponse().setName(user.getName()).setEmpID(user.getEmpID()))
                .collect(Collectors.toList());
        Response<SearchSubordinateResponse> response = UriUtils.createResponse(
                Arrays.asList(metadata1, metadata2), rows);

        return response;
    }
}
