package com.company;

import com.company.eurofilter.OneEuroFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeedAvgLength {
//    private static final Logger log = Logger.getLogger(SpeedAvgLength.class.getName());
    private static final Logger log = LoggerFactory.getLogger(SpeedAvgLength.class);
    static int filteredTotalLength;
    static double totalLength;

    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Антон\\Desktop\\syslog"));
             FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Антон\\Desktop\\avg-0.0.0.xlsx");
             Workbook workbook = new XSSFWorkbook()) {
            OneEuroFilter oef = new OneEuroFilter(15);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            Sheet sheet = workbook.createSheet("Sheet1");
            Row rowInit = sheet.createRow(0);

            Cell cellSpeed = rowInit.createCell(0);
            Cell cellLength = rowInit.createCell(1);
            Cell cellFilteredLength = rowInit.createCell(2);

            cellSpeed.setCellValue("Speed");
            cellLength.setCellValue("Total length");
            cellFilteredLength.setCellValue("Filtered total length");

            String line;
            int step = 1;
            while ((line = br.readLine()) != null) {
                Pattern totalLengthPattern = Pattern.compile("Total length: (\\d+)");
                Pattern speedPattern = Pattern.compile("Speed\\(mm/s\\): (\\d+\\.\\d)");

                // Match total length
                Matcher totalLengthMatcher = totalLengthPattern.matcher(line);
                if (totalLengthMatcher.find() && Integer.parseInt(totalLengthMatcher.group(1)) != 0) {
                    totalLength = Double.parseDouble(totalLengthMatcher.group(1));
                    System.out.printf("totalLength: %s\n", totalLength);
                    filteredTotalLength = (int) Math.round(oef.filter(totalLength));
                    System.out.println("filteredTotalLength: " + filteredTotalLength);
                    continue;
                }

                // Match Speed
                String speed = "";
                Matcher speedMatcher = speedPattern.matcher(line);
                if (speedMatcher.find() && !speedMatcher.group(1).isEmpty() && !speedMatcher.group(1).equals("0.0")) {
                    speed = decimalFormat.format(Double.parseDouble(speedMatcher.group(1)));
                    System.out.println("Speed: " + speed);
                } else continue;

                if (totalLength != 0 && !speed.equals("0.0")) {
                    Row rowNext = sheet.createRow(step);
                    Cell cell1 = rowNext.createCell(0);
                    cell1.setCellValue(speed);

                    Cell cell2 = rowNext.createCell(1);
                    cell2.setCellValue(totalLength);

                    Cell cell3 = rowNext.createCell(2);
                    cell3.setCellValue(filteredTotalLength);
                    step++;
                    workbook.write(outputStream);
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}