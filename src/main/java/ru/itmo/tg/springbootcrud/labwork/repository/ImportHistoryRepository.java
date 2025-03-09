package ru.itmo.tg.springbootcrud.labwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.tg.springbootcrud.labwork.model.ImportHistory;
import ru.itmo.tg.springbootcrud.security.model.User;

import java.util.List;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, Long> {

    List<ImportHistory> findByUser(User user);

}
