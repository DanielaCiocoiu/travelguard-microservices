package com.mentoring.TravelInsurance.service.person;

import com.mentoring.TravelInsurance.model.Person;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PersonExcel {
    static String[] HEADERs = { "Id", "Name", "Age", "Nationality", "Policy" };
    static String SHEET = "Travel insurance persons policies";


    public static ByteArrayInputStream tutorialsToExcel(List<Person> personList) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
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
            for (Person person : personList) {
                Row row = sheet.createRow(rowIdx++);
               // row.setRowStyle(headerStyle);
                row.createCell(0).setCellValue(person.getId());
                row.createCell(1).setCellValue(person.getName());
                row.createCell(2).setCellValue(person.getAge());
                row.createCell(3).setCellValue(person.getNationality());
                row.createCell(4).setCellValue(String.valueOf(person.getPolicy()));
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Fail to export data to Excel file: " + e.getMessage());
        }
    }

}
