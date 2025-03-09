package ru.itmo.tg.springbootcrud;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.model.Coordinates;
import ru.itmo.tg.springbootcrud.labwork.model.Location;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Color;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Country;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Excel {

    private static void createExcel() {

        AtomicInteger c = new AtomicInteger(0);

        var lab = getLab();

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fileOut = new FileOutputStream("lab.xlsx")
        ) {

            Sheet sheet = workbook.createSheet("Labs");
            Row row = sheet.createRow(0);

            row.createCell(c.getAndIncrement()).setCellValue(lab.getName());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getCoordinates().getX());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getCoordinates().getY());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getDescription());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getDiscipline().getName());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getDiscipline().getLectureHours());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getDifficulty().toString());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getMinimalPoint());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getAveragePoint());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getAuthor().getName());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getAuthor().getEyeColor().toString());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getAuthor().getHairColor().toString());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getAuthor().getLocation().getX());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getAuthor().getLocation().getY());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getAuthor().getLocation().getZ());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getAuthor().getPassportId());
            row.createCell(c.getAndIncrement()).setCellValue(lab.getAuthor().getNationality().toString());

            workbook.write(fileOut);

        } catch (IOException e) {
            System.out.println("watafak");
        }

    }

    private static void readExcel() {
        try (FileInputStream fileIn = new FileInputStream("lab.xlsx")) {

            var workbook = new XSSFWorkbook(fileIn);
            var sheet = workbook.getSheetAt(0);
            var row = sheet.getRow(0);

            var lab = LabWorkRequestDTO.builder()
                    .name(row.getCell(0).getStringCellValue())
                    .coordinates(Coordinates.builder()
                            .x(row.getCell(1).getNumericCellValue())
                            .y((long) row.getCell(2).getNumericCellValue())
                            .build())
                    .description(row.getCell(3).getStringCellValue())
                    .discipline(DisciplineRequestDTO.builder()
                            .name(row.getCell(4).getStringCellValue())
                            .lectureHours((int) row.getCell(5).getNumericCellValue())
                            .build())
                    .difficulty(Difficulty.valueOf(row.getCell(6).getStringCellValue()))
                    .minimalPoint((int) row.getCell(7).getNumericCellValue())
                    .averagePoint((float) row.getCell(8).getNumericCellValue())
                    .author(PersonRequestDTO.builder()
                            .name(row.getCell(9).getStringCellValue())
                            .eyeColor(Color.valueOf(row.getCell(10).getStringCellValue()))
                            .hairColor(Color.valueOf(row.getCell(11).getStringCellValue()))
                            .location(Location.builder()
                                    .x((long) row.getCell(12).getNumericCellValue())
                                    .y(row.getCell(13).getNumericCellValue())
                                    .z((float) row.getCell(14).getNumericCellValue())
                                    .build())
                            .passportId(row.getCell(15).getStringCellValue())
                            .nationality(Country.valueOf(row.getCell(16).getStringCellValue()))
                            .build())
                    .build();

            System.out.println(lab);


        } catch (IOException e) {
            System.out.println("watahell");
        }
    }

    private static LabWorkRequestDTO getLab() {
        return LabWorkRequestDTO.builder()
                .name("Lab")
                .coordinates(Coordinates.builder()
                        .x(123)
                        .y(321L)
                        .build())
                .description("Description")
                .discipline(DisciplineRequestDTO.builder()
                        .name("Discipline")
                        .lectureHours(88)
                        .build())
                .difficulty(Difficulty.NORMAL)
                .minimalPoint(12)
                .averagePoint(15.5F)
                .author(PersonRequestDTO.builder()
                        .name("Person")
                        .eyeColor(Color.BROWN)
                        .hairColor(Color.WHITE)
                        .location(Location.builder()
                                .x(78)
                                .y(89D)
                                .z(90)
                                .build())
                        .passportId("jsf1488swag322")
                        .nationality(Country.SOUTH_KOREA)
                        .build())
                .build();
    }

}
