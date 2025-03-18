package ru.itmo.tg.springbootcrud.labwork.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.BadFileException;
import ru.itmo.tg.springbootcrud.labwork.exception.StorageException;
import ru.itmo.tg.springbootcrud.labwork.exception.UniqueAttributeException;
import ru.itmo.tg.springbootcrud.labwork.model.*;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Color;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Country;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;
import ru.itmo.tg.springbootcrud.labwork.model.enums.ImportStatus;
import ru.itmo.tg.springbootcrud.misc.ModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class FileProcessingService {

    private final UserService userService;
    private final DisciplineService disciplineService;
    private final PersonService personService;
    private final LabWorkService labWorkService;
    private final ImportHistoryService importHistoryService;
    private final MinIOService minIOService;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void processFile(MultipartFile file, Class<?> clazz, String fileNameInMinio, ImportHistory importHistory) throws StorageException, BadFileException, InterruptedException {

        int imported;

        try {

            imported = processXLSX(file, clazz);

        } catch (IOException e) {
            try {
                importHistory.setImportStatus(ImportStatus.REJECTED);
                importHistoryService.save(importHistory);
            } catch (Exception ex) {
                throw new StorageException("DB error");
            }
            throw new BadFileException("error reading file: " + e.getMessage());

        } catch (Exception e) {
            throw new StorageException("DB error");
        }

        if (imported > 0) {
            try {
                importHistory.setImportNumber(imported);
                minIOService.uploadFile(fileNameInMinio, file);
            } catch (Exception e) {
                try {
                    importHistory.setImportStatus(ImportStatus.ERROR);
                    importHistoryService.save(importHistory);
                } catch (Exception ex) {
                    throw new StorageException("DB & MinIO error");
                }
                throw new StorageException("MinIO error");
            }
        } else {
            try {
                importHistory.setImportStatus(ImportStatus.REJECTED);
                importHistoryService.save(importHistory);
            } catch (Exception e) {
                throw new StorageException("DB error");
            }
            throw new BadFileException("bad file content");
        }

//        Thread.sleep(5*1000);
    }

    private int processXLSX(MultipartFile file, Class<?> clazz) throws Exception {
        try (InputStream inputStream = file.getInputStream()) {

            var workbook = new XSSFWorkbook(inputStream);
            var sheet = workbook.getSheetAt(0);
            var iter = sheet.rowIterator();

            if (clazz == LabWork.class) {
                return processXLSXLabWork(iter);
            } else if (clazz == Person.class) {
                return processXLSXPerson(iter);
            } else {
                return processXLSXDiscipline(iter);
            }

        } catch (ValidationException | UniqueAttributeException e) {
            return -1;
        }
    }

    private int processXLSXDiscipline(Iterator<Row> rowIterator) {
        List<DisciplineRequestDTO> disciplinesDTO = new ArrayList<>();
        while (rowIterator.hasNext()) {
            try {
                Row row = rowIterator.next();
                DisciplineRequestDTO discipline = extractDiscipline(row, new AtomicInteger(0));
                disciplinesDTO.add(discipline);
            } catch (ValidationException e) {
                return -1;
            }
        }
        List<Discipline> disciplines = ModelDTOConverter.toDisciplineList(disciplinesDTO, userService.getCurrentUser());
        return disciplineService.createDisciplines(disciplines);
    }

    private int processXLSXPerson(Iterator<Row> rowIterator) {
        List<PersonRequestDTO> personsDTO = new ArrayList<>();
        while (rowIterator.hasNext()) {
            try {
                Row row = rowIterator.next();
                PersonRequestDTO person = extractPerson(row, new AtomicInteger(0));
                personsDTO.add(person);
            } catch (ValidationException e) {
                return -1;
            }
        }
        List<Person> persons = ModelDTOConverter.toPersonList(personsDTO, userService.getCurrentUser());
        return personService.createPersons(persons);
    }

    private int processXLSXLabWork(Iterator<Row> rowIterator) {
        List<LabWorkRequestDTO> labWorksDTO = new ArrayList<>();
        while (rowIterator.hasNext()) {
            try {
                Row row = rowIterator.next();
                LabWorkRequestDTO labWork = extractLabWork(row);
                labWorksDTO.add(labWork);
            } catch (ValidationException e) {
                return -1;
            }
        }
        List<LabWork> labWorks = ModelDTOConverter.toLabWorkList(labWorksDTO, userService.getCurrentUser());
        return labWorkService.createLabWorks(labWorks);
    }

    private DisciplineRequestDTO extractDiscipline(Row row, AtomicInteger c) {
        try {
            return DisciplineRequestDTO.builder()
                    .name(row.getCell(c.getAndIncrement()).getStringCellValue())
                    .lectureHours((int) row.getCell(c.getAndIncrement()).getNumericCellValue())
                    .build();
        } catch (Exception e) {
            throw new ValidationException();
        }
    }

    private PersonRequestDTO extractPerson(Row row, AtomicInteger c) {
        try {
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
        } catch (Exception e) {
            throw new ValidationException();
        }
    }

    private LabWorkRequestDTO extractLabWork(Row row) {
        try {
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
        } catch (Exception e) {
            throw new ValidationException();
        }
    }

}
