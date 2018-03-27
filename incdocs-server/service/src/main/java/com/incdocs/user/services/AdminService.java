package com.incdocs.user.services;

import com.incdocs.entity.helper.EntityManagementHelper;
import com.incdocs.user.helper.BulkUploadMappingProcessor;
import com.incdocs.user.helper.UserManagementHelper;
import com.incdocs.utils.ApplicationException;
import com.incdocs.model.domain.BulkUploadUserRow;
import com.incdocs.model.request.CreateCompanyRequest;
import com.incdocs.model.request.CreateUserRequest;
import com.incdocs.utils.UriUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.incdocs.utils.Utils.getCellValue;

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

    @Autowired
    @Qualifier("bulkUploadMappingProcessor")
    private BulkUploadMappingProcessor bulkUploadMappingProcessor;

    @PostMapping("/create/company")
    public boolean createCompany(@RequestHeader(value = "incdocsID") String adminID,
                                 @RequestBody CreateCompanyRequest createCompanyRequest)
            throws ApplicationException {
        return entityManagementHelper.createCompany(adminID, createCompanyRequest);
    }

    @PostMapping("/create/user")
    public String createUser(@RequestHeader(value = "incdocsID") String adminID,
                             @RequestBody CreateUserRequest userCreateRequest)
            throws ApplicationException {

        return userManagementHelper.createUser(adminID, userCreateRequest);
    }

    @GetMapping(value = "/bulk/user/download")
    public void downloadBulkMappingExcel(@RequestHeader(value = "incdocsID") String adminID,
                                         HttpServletResponse response) throws ApplicationException {
        String fileName = "bulk-upload-mapping.xlsx";
        File file = new File(ClassLoader.getSystemResource(fileName).getFile());
        UriUtils.setContentHeadersForExcelFile(response, file);
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (FileNotFoundException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/bulk/user/upload", headers = ("content-type=multipart/*"))
    public List<BulkUploadUserRow>
    uploadBulkMappingExcel(@RequestHeader(value = "incdocsID") String adminID,
                           @RequestParam("file") MultipartFile multipartFile)
            throws IOException, ApplicationException {
        InputStream stream = multipartFile.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(stream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<BulkUploadUserRow> rowsUploaded = new ArrayList<>();
        sheet.forEach(row ->
            rowsUploaded.add(new BulkUploadUserRow()
                    .setName(getCellValue(row.getCell(0)))
                    .setEmpID(getCellValue(row.getCell(1)))
                    .setGhID(getCellValue(row.getCell(2)))
                    .setRole(getCellValue(row.getCell(3)))));

        return bulkUploadMappingProcessor.processUserRows(adminID, rowsUploaded);
    }

    @GetMapping(value = "/bulk/company/download")
    public void downloadBulkCompanyInfoExcel(@RequestHeader(value = "incdocsID") String adminID,
                                         HttpServletResponse response) throws ApplicationException {
        String fileName = "bulk-upload-company-info.xlsx";
        File file = new File(ClassLoader.getSystemResource(fileName).getFile());
        UriUtils.setContentHeadersForExcelFile(response, file);
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (FileNotFoundException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/bulk/company/upload", headers = ("content-type=multipart/*"))
    public void uploadBulkCompanyInfoExcel(@RequestHeader(value = "incdocsID") String adminID,
                               @RequestParam("file") MultipartFile multipartFile)
            throws IOException, ApplicationException {
        InputStream stream = multipartFile.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(stream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<CreateCompanyRequest> rowsToUpload = new ArrayList<>();
        sheet.forEach(row ->
            rowsToUpload.add(new CreateCompanyRequest()
                    .setName(getCellValue(row.getCell(0)))
                    .setId(getCellValue(row.getCell(1)))
                    .setPan(getCellValue(row.getCell(2)))));
        bulkUploadMappingProcessor.processCompanyRows(adminID, rowsToUpload);
    }
}
