package ru.itmo.tg.springbootcrud.labwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.tg.springbootcrud.labwork.model.LabWork;

@Repository
public interface LabWorkRepository extends JpaRepository<LabWork, Long> {
}
