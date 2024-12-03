package ru.itmo.tg.springbootcrud.labwork.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.tg.springbootcrud.labwork.model.LabWork;

@Repository
public interface LabWorkRepository extends CrudRepository<LabWork, Long> {
}
