package ru.itmo.tg.springbootcrud.labwork.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.enums.ImportStatus;
import ru.itmo.tg.springbootcrud.security.model.User;

@Entity
@Table(name = "import_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    @NotNull
    private String fileName;

    @Column(name = "file_name_minio", nullable = false)
    @NotNull
    private String fileNameMinio;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    @NotNull
    private User user;

    @Column(name = "import_status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ImportStatus importStatus;

    @Column(name = "import_number", nullable = false)
    @NotNull
    private Integer importNumber;

}
