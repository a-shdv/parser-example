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

public class AverageFilteredLength {
    static String filteredLengthOpto2 = "";
    static String filteredLengthOpto3 = "";
    static String filteredLengthOpto4 = "";
    static String filteredLengthOpto5 = "";
    static int avgFilteredLength;
    static String avgLength = "";


    public static void main(String[] args) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Users\\Антон\\Desktop\\data-average-filtered.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        Row rowInit = sheet.createRow(0);
        Cell cellSpeed = rowInit.createCell(0);
        Cell cellTotalLength = rowInit.createCell(1);
        cellSpeed.setCellValue("Speed");
        cellTotalLength.setCellValue("AVG filtered length");

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Антон\\Desktop\\syslog-avg-filtered-0.0.1"))) {
            String line;
            int step = 1;
            while ((line = br.readLine()) != null) {
//                if (!line.contains("Filtered length from Opto2"))
//                    continue;

                // Define a pattern for Total Length and Speed
                Pattern filteredLengthOpto2Pattern = Pattern.compile("Filtered length from Opto2: (\\d+)");
                Pattern filteredLengthOpto3Pattern = Pattern.compile("Filtered length from Opto3: (\\d+)");
                Pattern filteredLengthOpto4Pattern = Pattern.compile("Filtered length from Opto4: (\\d+)");
                Pattern filteredLengthOpto5Pattern = Pattern.compile("Filtered length from Opto5: (\\d+)");
                Pattern avgLengthPattern = Pattern.compile("AVG length: (\\d+)");
                Pattern speedPattern = Pattern.compile("Speed\\(mm/s\\): (\\d+\\.\\d)");

                // 2
                Matcher filteredLengthOpto2Matcher = filteredLengthOpto2Pattern.matcher(line);
                if (filteredLengthOpto2Matcher.find()) {
                    filteredLengthOpto2 = filteredLengthOpto2Matcher.group(1);
                    System.out.printf("filteredLengthOpto2: %s\n", filteredLengthOpto2);
                    continue;
                }

                // 3
                Matcher filteredLengthOpto3Matcher = filteredLengthOpto3Pattern.matcher(line);
                if (filteredLengthOpto3Matcher.find()) {
                    filteredLengthOpto3 = filteredLengthOpto3Matcher.group(1);
                    System.out.printf("filteredLengthOpto3: %s\n", filteredLengthOpto3);
                    continue;
                }

                // 4
                Matcher filteredLengthOpto4Matcher = filteredLengthOpto4Pattern.matcher(line);
                if (filteredLengthOpto4Matcher.find()) {
                    filteredLengthOpto4 = filteredLengthOpto4Matcher.group(1);
                    System.out.printf("filteredLengthOpto4: %s\n", filteredLengthOpto4);
                    continue;
                }

                // 5
                Matcher filteredLengthOpto5Matcher = filteredLengthOpto5Pattern.matcher(line);
                if (filteredLengthOpto5Matcher.find()) {
                    filteredLengthOpto5 = filteredLengthOpto5Matcher.group(1);
                    System.out.printf("filteredLengthOpto5: %s\n", filteredLengthOpto5);
                    continue;
                }

                // AverageLength
                Matcher avgLengthMatcher = avgLengthPattern.matcher(line);
                if (avgLengthMatcher.find()) {
                    avgLength = avgLengthMatcher.group(1);
                    System.out.println("AVG length: " + avgLength);
                    continue;
                }

                // Match Speed
                String speed = "";
                Matcher speedMatcher = speedPattern.matcher(line);
                if (speedMatcher.find()) {
                    speed = decimalFormat.format(Double.parseDouble(speedMatcher.group(1)));
                    System.out.println("Speed: " + speed);
                }

                if (speed != "") {
                    avgFilteredLength = (Integer.parseInt(filteredLengthOpto2) + Integer.parseInt(filteredLengthOpto3)
                            + Integer.parseInt(filteredLengthOpto4) + Integer.parseInt(filteredLengthOpto5)) / 4;
                    System.out.println("AVG filtered length: " + avgFilteredLength + "\n");
                }
                else continue;

                Row rowNext = sheet.createRow(step);
                Cell cell1 = rowNext.createCell(0);
                cell1.setCellValue(speed);

                Cell cell2 = rowNext.createCell(1);
                Cell cell3 = rowNext.createCell(2);
                if (avgFilteredLength != 0) {
                    cell2.setCellValue(avgFilteredLength);
                    cell3.setCellValue(avgLength);
                }
                else
                    continue;

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