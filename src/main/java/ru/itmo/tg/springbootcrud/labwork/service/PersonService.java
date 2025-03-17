package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.InsufficientPermissionsException;
import ru.itmo.tg.springbootcrud.labwork.exception.PersonNotFoundException;
import ru.itmo.tg.springbootcrud.labwork.exception.UniqueAttributeException;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;
import ru.itmo.tg.springbootcrud.labwork.model.Person;
import ru.itmo.tg.springbootcrud.labwork.repository.PersonRepository;
import ru.itmo.tg.springbootcrud.labwork.validator.PersonValidator;
import ru.itmo.tg.springbootcrud.misc.ModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonValidator personValidator;

    public List<PersonResponseDTO> getPersons(Integer pageNumber, Integer pageSize, String order, String sortCol) {
        Page<Person> page = personRepository.findAll(PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(order), sortCol)));
        return ModelDTOConverter.toPersonResponseDTOList(page.getContent());
    }

    public PersonResponseDTO getPersonById(Long id) {
        return ModelDTOConverter.convert(personRepository.findById(id).orElseThrow(PersonNotFoundException::new));
    }

    @Transactional
    public PersonResponseDTO createPerson(PersonRequestDTO personDTO, User user) {
        if (personRepository.existsByPassportID(personDTO.getPassportId())) {
            throw new UniqueAttributeException("this passport already exists");
        }
        Person person = ModelDTOConverter.convert(personDTO, user);
        personValidator.validatePerson(person);
        person = personRepository.save(person);
        return ModelDTOConverter.convert(person);
    }

    private int getHash(Person person) {
        return Objects.hash(person.getName(), person.getEyeColor(), person.getHairColor(),
                person.getLocation().getX(), person.getLocation().getY(), person.getLocation().getZ(),
                person.getPassportID(), person.getNationality());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int createPersons(List<Person> persons) {
        Set<Integer> uniqueHashes = new HashSet<>();
        List<Person> finalList = new ArrayList<>();
        for (Person person : persons) {
            personValidator.validatePerson(person);
            if (!uniqueHashes.contains(getHash(person))) {
                uniqueHashes.add(getHash(person));
                finalList.add(person);
            }
        }
        personRepository.saveAll(finalList);
        return finalList.size();
    }

    @Transactional
    public PersonResponseDTO updatePerson(Long id, PersonRequestDTO personDTO, User user) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        if (user.getRole() != Role.ROLE_ADMIN && !user.equals(person.getOwner())) {
            throw new InsufficientPermissionsException("no rights to edit Person #" + id);
        }
        if (!person.getPassportID().equals(personDTO.getPassportId())) {
            if (personRepository.existsByPassportID(personDTO.getPassportId())) {
                throw new UniqueAttributeException("this passport already exists");
            }
        }
        person.setName(personDTO.getName());
        person.setEyeColor(personDTO.getEyeColor());
        person.setHairColor(personDTO.getHairColor());
        person.setLocation(personDTO.getLocation());
        person.setPassportID(personDTO.getPassportId());
        person.setNationality(personDTO.getNationality());
        person = personRepository.save(person);
        return ModelDTOConverter.convert(person);
    }

    @Transactional
    public void deletePerson(Long id, User user) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        if (user.getRole() != Role.ROLE_ADMIN && !user.equals(person.getOwner())) {
            throw new InsufficientPermissionsException("no rights to delete Person #" + id);
        }
        personRepository.deleteById(id);
    }

}
