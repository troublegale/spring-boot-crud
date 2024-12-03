package ru.itmo.tg.springbootcrud.labwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
}
