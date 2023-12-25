package com.company;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Users\\Антон\\Desktop\\data.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Row rowInit = sheet.createRow(0);
        Cell cellTotalLength = rowInit.createCell(0);
        Cell cellSpeed = rowInit.createCell(1);
        cellTotalLength.setCellValue("Total length");
        cellSpeed.setCellValue("Speed");

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Антон\\Desktop\\syslog"))) {
            String line;
            int step = 1;
            while ((line = br.readLine()) != null) {
                if (!line.contains("Total length"))
                    continue;

                // Define a pattern for Total Length and Speed
                Pattern totalLengthPattern = Pattern.compile("Total length: (\\d+\\.\\d+)");
                Pattern speedPattern = Pattern.compile("Speed\\(mm/s\\): (\\d+\\.\\d)");

                // Match Total Length
                String totalLength="";
                Matcher totalLengthMatcher = totalLengthPattern.matcher(line);
                if (totalLengthMatcher.find()) {
                    totalLength = totalLengthMatcher.group(1);
                    System.out.println("Total Length: " + totalLength);
                }
                for (int i = 0; i < 4; i++) {
                    line = br.readLine();
                }

                // Match Speed
                String speed = "";
                Matcher speedMatcher = speedPattern.matcher(line);
                if (speedMatcher.find()) {
                    speed = speedMatcher.group(1);
                    System.out.println("Speed: " + speed);
                }

                Row rowNext = sheet.createRow(step);
                Cell cell1 = rowNext.createCell(0);
                Cell cell2 = rowNext.createCell(1);
                if (!totalLength.isEmpty())
                    cell1.setCellValue(totalLength);
                else
                    continue;
                cell2.setCellValue(speed);

                workbook.write(outputStream);
                step++;
            }
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}