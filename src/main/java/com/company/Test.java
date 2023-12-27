package com.company;

import com.company.eurofilter.OneEuroFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    private static final Logger log = Logger.getLogger(Test.class.getName());
    static int avgFilteredLength;
    static int avgLength;


    public static void main(String[] args) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Users\\Антон\\Desktop\\avg-0.0.0.xlsx");
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
            OneEuroFilter oef = new OneEuroFilter(15);

            while ((line = br.readLine()) != null) {
                Pattern speedPattern = Pattern.compile("Speed\\(mm/s\\): (\\d+\\.\\d)");
                Pattern avgLengthPattern = Pattern.compile("AVG length: (\\d+)");
//                Pattern avgFilteredLengthPattern = Pattern.compile("AVG filtered length: (\\d+)");

                // avglength
                Matcher avgLengthMatcher = avgLengthPattern.matcher(line);
                if (avgLengthMatcher.find() && !avgLengthMatcher.group(1).isEmpty() && !avgLengthMatcher.group(1).equals("0")) {
                    avgLength = Integer.parseInt(avgLengthMatcher.group(1));
                    System.out.printf("avgLength: %s\n", avgLength);
                    avgFilteredLength = (int) oef.filter(avgLength);
                    System.out.println("avgFilteredLength: " + avgFilteredLength);
                    continue;
                }

                // avgFilteredLength
                // Matcher avgFilterLengthMatcher = avgFilteredLengthPattern.matcher(line);

                // Match Speed
                String speed = "";
                Matcher speedMatcher = speedPattern.matcher(line);
                if (speedMatcher.find()) {
                    speed = decimalFormat.format(Double.parseDouble(speedMatcher.group(1)));
                    System.out.println("Speed: " + speed);
                } else continue;

                if (avgLength != 0) {
                    Row rowNext = sheet.createRow(step);
                    Cell cell1 = rowNext.createCell(0);
                    cell1.setCellValue(speed);

                    Cell cell2 = rowNext.createCell(1);
                    cell2.setCellValue(avgLength);

                    Cell cell3 = rowNext.createCell(2);
                    cell3.setCellValue(avgFilteredLength);
                    step++;
                }

                workbook.write(outputStream);
            }
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}