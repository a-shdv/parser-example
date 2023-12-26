package com.company;

import com.company.eurofilter.OneEuroFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Logger;

public class Main {
    static String rawSpeed = "";
    static double speed = 0.0;
    static int avgLength = 0;
    static int avgFilteredLength = 0;

    public static void main(String[] args) {
        Logger log = Logger.getLogger(Main.class.getName());

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Users\\Антон\\Desktop\\avg-0.0.3.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Number number;
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

        Row rowInit = sheet.createRow(0);

        // HEADER: speed
        Cell cellSpeed = rowInit.createCell(0);
        cellSpeed.setCellValue("Speed");

        // HEADER: length
        Cell cellLength = rowInit.createCell(1);
        cellLength.setCellValue("AVG length");

        // HEADER: filtered length
        Cell cellFilteredLength = rowInit.createCell(2);
        cellFilteredLength.setCellValue("AVG filtered length");

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Антон\\Desktop\\test.txt"))) {
            String line;
            int step = 1;
            OneEuroFilter oef = new OneEuroFilter(20);

            while ((line = br.readLine()) != null) {
                // parsing
                rawSpeed = line.split("\t")[0];
                if (rawSpeed.contains(",")) {
                    number = format.parse(rawSpeed);
                    speed = number.doubleValue();
                } else {
                    speed = Double.parseDouble(rawSpeed);
                }
                avgLength = Integer.parseInt(line.split("\t")[1]);
                avgFilteredLength = (int) oef.filter(avgLength);

                // speed
                Row rowNext = sheet.createRow(step);
                Cell cell1 = rowNext.createCell(0);
                cell1.setCellValue(speed);

                // average length
                Cell cell2 = rowNext.createCell(1);
                cell2.setCellValue(avgLength);

                // filtered average length
                Cell cell3 = rowNext.createCell(2);
                cell3.setCellValue(avgFilteredLength);

                log.info("\n{\n" +
                "\tspeed: " + speed + "\n" +
                "\tavgLength: " + avgLength + "\n" +
                "\tavgFilteredLength: " + avgFilteredLength + "\n" +
                "}\n");
                workbook.write(outputStream);
                step++;
            }
            workbook.close();
            outputStream.close();
        }  catch (Exception e) {
            log.info(e.getMessage());
        }

    }
}
