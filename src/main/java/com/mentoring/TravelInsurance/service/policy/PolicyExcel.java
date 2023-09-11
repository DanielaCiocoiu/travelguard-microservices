package com.mentoring.TravelInsurance.service.policy;

import com.mentoring.TravelInsurance.model.Policy;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PolicyExcel {
    private static final String[] HEADERs = {"Id", "Eligibility", "Validity", "Type", "BenefitAndLimit", "Price"};
    private static final String SHEET = "Travel insurance policies";


    public ByteArrayInputStream tutorialsToExcel(List<Policy> policiesList) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
                cell.setCellStyle(headerStyle);
            }

            XSSFFont font = (XSSFFont) workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 13);
            font.setBold(true);
            headerStyle.setFont(font);

            int rowIdx = 1;
            for (Policy policy : policiesList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(policy.getId());
                row.createCell(1).setCellValue(policy.getEligibility());
                row.createCell(2).setCellValue(policy.isValidity());
                row.createCell(3).setCellValue(policy.getType().toString());
                row.createCell(4).setCellValue(policy.getBenefitAndLimit().toString());
                row.createCell(5).setCellValue(policy.getPrice());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Fail to export data to Excel file: " + e.getMessage());
        }
    }

}
