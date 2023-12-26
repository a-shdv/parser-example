package com.company.avg;

import com.company.eurofilter.OneEuroFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateAverageFilteredLength {
    static String lengthOpto2 = "";
    static String lengthOpto3 = "";
    static String lengthOpto4 = "";
    static String lengthOpto5 = "";
    static int avgFilteredLength;
    static String avgLength = "";
    static int filteredLengthOpto2, filteredLengthOpto3, filteredLengthOpto4, filteredLengthOpto5;


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

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Антон\\Desktop\\syslog-avg-filtered-0.0.1"))) {
            String line;
            int step = 1;
            while ((line = br.readLine()) != null) {
//                if (!line.contains("Filtered length from Opto2"))
//                    continue;

                // Define a pattern for Total Length and Speed
                Pattern filteredLengthOpto2Pattern = Pattern.compile("Length from Opto2: (\\d+)");
                Pattern filteredLengthOpto3Pattern = Pattern.compile("Length from Opto3: (\\d+)");
                Pattern filteredLengthOpto4Pattern = Pattern.compile("Length from Opto4: (\\d+)");
                Pattern filteredLengthOpto5Pattern = Pattern.compile("Length from Opto5: (\\d+)");
                Pattern avgLengthPattern = Pattern.compile("AVG length: (\\d+)");
                Pattern speedPattern = Pattern.compile("Speed\\(mm/s\\): (\\d+\\.\\d)");

                // 2
                Matcher lengthOpto2Matcher = filteredLengthOpto2Pattern.matcher(line);
                if (lengthOpto2Matcher.find()) {
                    lengthOpto2 = lengthOpto2Matcher.group(1);
                    System.out.printf("LengthOpto2: %s\n", lengthOpto2);
                    continue;
                }

                // 3
                Matcher lengthOpto3Matcher = filteredLengthOpto3Pattern.matcher(line);
                if (lengthOpto3Matcher.find()) {
                    lengthOpto3 = lengthOpto3Matcher.group(1);
                    System.out.printf("LengthOpto3: %s\n", lengthOpto3);
                    continue;
                }

                // 4
                Matcher lengthOpto4Matcher = filteredLengthOpto4Pattern.matcher(line);
                if (lengthOpto4Matcher.find()) {
                    lengthOpto4 = lengthOpto4Matcher.group(1);
                    System.out.printf("LengthOpto4: %s\n", lengthOpto4);
                    continue;
                }

                // 5
                Matcher lengthOpto5Matcher = filteredLengthOpto5Pattern.matcher(line);
                if (lengthOpto5Matcher.find()) {
                    lengthOpto5 = lengthOpto5Matcher.group(1);
                    System.out.printf("LengthOpto5: %s\n", lengthOpto5);
                    continue;
                }

                // AverageLength
                Matcher avgLengthMatcher = avgLengthPattern.matcher(line);
                if (avgLengthMatcher.find()) {
                    avgLength = avgLengthMatcher.group(1);
                    System.out.println("AVG length: " + avgLength);
                    continue;
                }

//                if (!lengthOpto2.equals("") && !lengthOpto2.equals("0") &&
//                !lengthOpto3.equals("") && !lengthOpto3.equals("0") &&
//                !lengthOpto4.equals("") && !lengthOpto4.equals("0") &&
//                !lengthOpto5.equals("") && !lengthOpto5.equals("0")) {
//                    OneEuroFilter oneEuroFilter = new OneEuroFilter(20);
//                    filteredLengthOpto2 = (int) oneEuroFilter.filter(Integer.parseInt(lengthOpto2));
//                    filteredLengthOpto3 = (int) oneEuroFilter.filter(Integer.parseInt(lengthOpto3));
//                    filteredLengthOpto4 = (int) oneEuroFilter.filter(Integer.parseInt(lengthOpto4));
//                    filteredLengthOpto5 = (int) oneEuroFilter.filter(Integer.parseInt(lengthOpto5));
//                avgFilteredLength = (filteredLengthOpto2 + filteredLengthOpto3 + filteredLengthOpto4 + filteredLengthOpto5) / 4;
//                }

                // Match Speed
                String speed = "";
                Matcher speedMatcher = speedPattern.matcher(line);
                if (speedMatcher.find()) {
                    speed = decimalFormat.format(Double.parseDouble(speedMatcher.group(1)));
                    System.out.println("Speed: " + speed);
                }

//                if (speed != "") {
//                    avgFilteredLength = (Integer.parseInt(lengthOpto2) + Integer.parseInt(lengthOpto3)
//                            + Integer.parseInt(lengthOpto4) + Integer.parseInt(lengthOpto5)) / 4;
//                    System.out.println("AVG filtered length: " + avgFilteredLength + "\n");
//                }
//                else continue;

                Row rowNext = sheet.createRow(step);
                Cell cell1 = rowNext.createCell(0);
                cell1.setCellValue(speed);

                Cell cell2 = rowNext.createCell(1);
                Cell cell3 = rowNext.createCell(2);
                if (avgFilteredLength != 0) {
                    cell2.setCellValue(avgLength);
                    cell3.setCellValue(avgFilteredLength);
                } else
                    continue;

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