package com.incdocs.user.services;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.utils.MediaTypeUtils;
import com.indocs.model.domain.BulkUploadRow;
import com.indocs.model.request.CreateCompanyRequest;
import com.indocs.model.request.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.Arrays;

@RestController
@RequestMapping("/incdocs/admin")
public class AdminService {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    @Qualifier("userManagementHelper")
    private UserManagementHelper userManagementHelper;

    @Autowired
    @Qualifier("entityManagementHelper")
    private EntityManagementHelper entityManagementHelper;

    @PostMapping("/create/company")
    public boolean createCompany(@RequestHeader(value = "incdocsID") String adminID,
                                 @RequestBody CreateCompanyRequest createCompanyRequest) {
        return entityManagementHelper.createCompany(adminID, createCompanyRequest);
    }

    @PostMapping("/create/user")
    public String createUser(@RequestHeader(value = "incdocsID") String adminID,
                             @RequestBody CreateUserRequest userCreateRequest)
            throws ApplicationException {

        return userManagementHelper.createUser(adminID, userCreateRequest);
    }

    @GetMapping(value = "/bulkmap/download")
    public void downloadBulkMappingExcel(HttpServletResponse response) throws ApplicationException {
        String fileName = "bulk-upload-mapping.xlsx";
        File file =  new File(ClassLoader.getSystemResource(fileName).getFile());
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (FileNotFoundException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
