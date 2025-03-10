package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.enums.ImportStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportHistoryDTO {

    private Long id;

    private String fileName;

    private String username;

    private ImportStatus status;

    private Integer importNumber;

}
