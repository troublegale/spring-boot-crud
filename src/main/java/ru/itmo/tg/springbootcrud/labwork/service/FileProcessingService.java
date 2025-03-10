package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.BadFileException;
import ru.itmo.tg.springbootcrud.labwork.model.*;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Color;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Country;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;
import ru.itmo.tg.springbootcrud.labwork.model.enums.ImportStatus;
import ru.itmo.tg.springbootcrud.labwork.repository.ImportHistoryRepository;
import ru.itmo.tg.springbootcrud.misc.ModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class FileProcessingService {

    private final UserService userService;
    private final DisciplineService disciplineService;
    private final PersonService personService;
    private final LabWorkService labWorkService;
    private final ImportHistoryRepository importHistoryRepository;

    public void processFile(MultipartFile file, Class<?> clazz) {
        if (file == null || file.isEmpty()) {
            throw new BadFileException("empty file");
        }
        String contentType = file.getContentType();
        if (!Objects.equals(contentType, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            throw new BadFileException(".xlsx file required");
        }
        ImportHistory importHistory = ImportHistory.builder()
                .fileName(file.getOriginalFilename())
                .user(userService.getCurrentUser())
                .importStatus(ImportStatus.PROCESSING)
                .importNumber(0)
                .build();
        importHistoryRepository.save(importHistory);
        int imported = processXLSX(file, clazz);
        if (imported > 0) {
            importHistory.setImportNumber(imported);
            importHistory.setImportStatus(ImportStatus.IMPORTED);
            importHistoryRepository.save(importHistory);
        } else {
            importHistory.setImportStatus(ImportStatus.REJECTED);
            importHistoryRepository.save(importHistory);
            throw new BadFileException("bad file content");
        }
    }

    private int processXLSX(MultipartFile file, Class<?> clazz) {
        try (InputStream inputStream = file.getInputStream()) {

            var workbook = new XSSFWorkbook(inputStream);
            var sheet = workbook.getSheetAt(0);
            var iter = sheet.rowIterator();

            if (clazz == LabWorkRequestDTO.class) {
                return processXLSXLabWork(iter);
            } else if (clazz == PersonRequestDTO.class) {
                return processXLSXPerson(iter);
            } else {
                return processXLSXDiscipline(iter);
            }

        } catch (Exception e) {
            return -1;
        }
    }

    private int processXLSXDiscipline(Iterator<Row> rowIterator) {
        List<DisciplineRequestDTO> disciplinesDTO = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            DisciplineRequestDTO discipline = extractDiscipline(row, new AtomicInteger(0));
            disciplinesDTO.add(discipline);
        }
        List<Discipline> disciplines = ModelDTOConverter.toDisciplineList(disciplinesDTO, userService.getCurrentUser());
        return disciplineService.createDisciplines(disciplines);
    }

    private int processXLSXPerson(Iterator<Row> rowIterator) {
        List<PersonRequestDTO> personsDTO = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            PersonRequestDTO person = extractPerson(row, new AtomicInteger(0));
            personsDTO.add(person);
        }
        List<Person> persons = ModelDTOConverter.toPersonList(personsDTO, userService.getCurrentUser());
        return personService.createPersons(persons);
    }

    private int processXLSXLabWork(Iterator<Row> rowIterator) {
        List<LabWorkRequestDTO> labWorksDTO = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            LabWorkRequestDTO labWork = extractLabWork(row);
            labWorksDTO.add(labWork);
        }
        List<LabWork> labWorks = ModelDTOConverter.toLabWorkList(labWorksDTO, userService.getCurrentUser());
        return labWorkService.createLabWorks(labWorks);
    }

    private DisciplineRequestDTO extractDiscipline(Row row, AtomicInteger c) {
        return DisciplineRequestDTO.builder()
                .name(row.getCell(c.getAndIncrement()).getStringCellValue())
                .lectureHours((int) row.getCell(c.getAndIncrement()).getNumericCellValue())
                .build();
    }

    private PersonRequestDTO extractPerson(Row row, AtomicInteger c) {
        return PersonRequestDTO.builder()
                .name(row.getCell(c.getAndIncrement()).getStringCellValue())
                .eyeColor(Color.valueOf(row.getCell(c.getAndIncrement()).getStringCellValue()))
                .hairColor(Color.valueOf(row.getCell(c.getAndIncrement()).getStringCellValue()))
                .location(Location.builder()
                        .x((long) row.getCell(c.getAndIncrement()).getNumericCellValue())
                        .y(row.getCell(c.getAndIncrement()).getNumericCellValue())
                        .z((float) row.getCell(c.getAndIncrement()).getNumericCellValue())
                        .build())
                .passportId(row.getCell(c.getAndIncrement()).getStringCellValue())
                .nationality(Country.valueOf(row.getCell(c.getAndIncrement()).getStringCellValue()))
                .build();
    }

    private LabWorkRequestDTO extractLabWork(Row row) {
        AtomicInteger c = new AtomicInteger(0);
        return LabWorkRequestDTO.builder()
                .name(row.getCell(c.getAndIncrement()).getStringCellValue())
                .coordinates(Coordinates.builder()
                        .x(row.getCell(c.getAndIncrement()).getNumericCellValue())
                        .y((long) row.getCell(c.getAndIncrement()).getNumericCellValue())
                        .build())
                .description(row.getCell(c.getAndIncrement()).getStringCellValue())
                .discipline(extractDiscipline(row, c))
                .difficulty(Difficulty.valueOf(row.getCell(c.getAndIncrement()).getStringCellValue()))
                .minimalPoint((int) row.getCell(c.getAndIncrement()).getNumericCellValue())
                .averagePoint((float) row.getCell(c.getAndIncrement()).getNumericCellValue())
                .author(extractPerson(row, c))
                .build();
    }

}
