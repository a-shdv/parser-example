package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static String length;
    static String lengthSmoothed = "";
    static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CompletableFuture<Void> result = CompletableFuture.runAsync(() -> {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream("C:\\Users\\Антон\\Desktop\\result-1.0.1.xlsx");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            Row rowInit = sheet.createRow(0);

            Cell cellSpeed = rowInit.createCell(0);
            Cell cellLength = rowInit.createCell(1);
            Cell cellLengthSmoothed = rowInit.createCell(2);

            cellSpeed.setCellValue("Speed");
            cellLength.setCellValue("Length");
            cellLengthSmoothed.setCellValue("Length smoothed");

            try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Антон\\Desktop\\syslog"))) {
                String line;
                int step = 1;
                while ((line = br.readLine()) != null) {
                    Pattern lengthPattern = Pattern.compile("Length \\(mm\\): (\\d+\\.\\d*)");
                    Pattern lengthSmoothedPattern = Pattern.compile("Length smoothed \\(mm\\): (\\d+\\.\\d*)");
                    Pattern speedPattern = Pattern.compile("Speed\\(mm/s\\): (\\d+\\.\\d*)");

                    // length
                    Matcher lengthMatcher = lengthPattern.matcher(line);
                    if (lengthMatcher.find()) {
                        length = decimalFormat.format(Double.parseDouble(lengthMatcher.group(1)));
                        System.out.println("---------------------------");
                        System.out.printf("Length: %s\n", length);
                        continue;
                    }

                    // smoothed length
                    Matcher lengthSmoothedMatcher = lengthSmoothedPattern.matcher(line);
                    if (lengthSmoothedMatcher.find()) {
                        lengthSmoothed = decimalFormat.format(Double.parseDouble(lengthSmoothedMatcher.group(1)));
                        System.out.println("Length smoothed: " + lengthSmoothed);
                        continue;
                    }

                    // speed
                    String speed = "";
                    Matcher speedMatcher = speedPattern.matcher(line);
                    if (speedMatcher.find()) {
                        speed = decimalFormat.format(Double.parseDouble(speedMatcher.group(1)));
                        System.out.println("Speed: " + speed);
                        System.out.println("Date: " + line.split(" ")[2]);
                        System.out.println("---------------------------");
                    } else continue;

                    if ((!length.equals("0,00")) && !lengthSmoothed.equals("0,00")) {
                        Row rowNext = sheet.createRow(step);
                        Cell cell1 = rowNext.createCell(0);
                        cell1.setCellValue(speed);

                        Cell cell2 = rowNext.createCell(1);
                        cell2.setCellValue(length);

                        Cell cell3 = rowNext.createCell(2);
                        cell3.setCellValue(lengthSmoothed);
                        step++;
                        workbook.write(outputStream);
                    }
                }
                workbook.close();
                outputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
        result.join();
    }
}
