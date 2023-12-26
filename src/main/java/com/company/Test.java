package com.company.avg;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    static String avgFilteredLength;
    static String avgLength = "";

    public static void main(String[] args) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Users\\Антон\\Desktop\\data-comparison.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        Row rowInit = sheet.createRow(0);

        Cell cellSpeed = rowInit.createCell(0);
        Cell cellLength = rowInit.createCell(1);
        Cell cellFilteredLength = rowInit.createCell(2);

        cellSpeed.setCellValue("Speed");
        cellLength.setCellValue("AVG length");
        cellFilteredLength.setCellValue("AVG filtered length");

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Антон\\Desktop\\syslog"))) {
            String line;
            int step = 1;
            while ((line = br.readLine()) != null) {
                Pattern avgLengthPattern = Pattern.compile("AVG length: (\\d+)");
                Pattern avgFilteredLengthPattern = Pattern.compile("AVG filtered length: (\\d+)");
                Pattern speedPattern = Pattern.compile("Speed\\(mm/s\\): (\\d+\\.\\d)");

                // avglength
                Matcher avgLengthMatcher = avgLengthPattern.matcher(line);
                if (avgLengthMatcher.find()) {
                    avgLength = avgLengthMatcher.group(1);
                    System.out.printf("LengthOpto2: %s\n", avgLength);
                    continue;
                }

                // avgFilteredLength
                Matcher avgFilterLengthMatcher = avgFilteredLengthPattern.matcher(line);
                if (avgFilterLengthMatcher.find()) {
                    avgFilteredLength = avgFilterLengthMatcher.group(1);
                    System.out.println("AVG filtered length: " + avgFilteredLength);
                    continue;
                }

                // Match Speed
                String speed = "";
                Matcher speedMatcher = speedPattern.matcher(line);
                if (speedMatcher.find()) {
                    speed = decimalFormat.format(Double.parseDouble(speedMatcher.group(1)));
                    System.out.println("Speed: " + speed);
                } else continue;

                if (!avgLength.equals("") && !avgLength.equals("0")
                        && !avgFilteredLength.equals("") && !avgFilteredLength.equals("0")) {
                    Row rowNext = sheet.createRow(step);
                    Cell cell1 = rowNext.createCell(0);
                    cell1.setCellValue(speed);

                    Cell cell2 = rowNext.createCell(1);
                    cell2.setCellValue(avgLength);

                    Cell cell3 = rowNext.createCell(2);
                    cell3.setCellValue(avgFilteredLength);
                }

                workbook.write(outputStream);
                step++;
            }
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}