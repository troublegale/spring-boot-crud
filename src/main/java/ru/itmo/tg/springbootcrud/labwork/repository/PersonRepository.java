package ru.itmo.tg.springbootcrud.labwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.tg.springbootcrud.labwork.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Boolean existsByPassportID(String passportID);

}
