package ru.itmo.tg.springbootcrud.labwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.tg.springbootcrud.labwork.model.UpdateHistory;

import java.util.Optional;

@Repository
public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory, Long> {

    Optional<UpdateHistory> findByLabWorkID(long id);

}
