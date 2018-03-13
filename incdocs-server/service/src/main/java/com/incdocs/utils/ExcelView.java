package com.incdocs.utils;

import com.indocs.model.domain.BulkUploadRow;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Repository("excelView")
public class ExcelView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"bulk-upload-mapping.xlsx\"");

        List<BulkUploadRow> rows = (List<BulkUploadRow>) model.get("bulk-upload-row");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Bulkooo Upload");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);


        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("EmployeeID");
        header.createCell(1).setCellValue("CompanyID");
        header.createCell(2).setCellValue("Role");
        header.createCell(3).setCellValue("GroupHeadID");

        for (BulkUploadRow row: rows)
        {
            Row fauxData = sheet.createRow(1);
            fauxData.createCell(0).setCellValue(row.getEmpID());
            fauxData.createCell(1).setCellValue(row.getCompanyID());
            fauxData.createCell(2).setCellValue(row.getRole());
            fauxData.createCell(3).setCellValue(row.getGhID());
        }

       /* header.getCell(0).setCellStyle(style);
        header.getCell(1).setCellStyle(style);
        header.getCell(2).setCellStyle(style);
        header.getCell(3).setCellStyle(style);*/

    }
}
