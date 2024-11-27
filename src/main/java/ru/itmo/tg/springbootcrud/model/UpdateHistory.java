package ru.itmo.tg.springbootcrud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.model.enums.Action;

import java.time.LocalDateTime;

@Entity
@Table(name = "update_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "lab_work_id", nullable = false)
    @NotNull
    private long labWorkID;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Action action;

    @Column(name = "action_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private LocalDateTime actionTime;

    @PrePersist
    protected void onCreate() {
        actionTime = LocalDateTime.now();
    }

}
