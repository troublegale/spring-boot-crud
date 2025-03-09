package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.InsufficientPermissionsException;
import ru.itmo.tg.springbootcrud.labwork.exception.PersonNotFoundException;
import ru.itmo.tg.springbootcrud.labwork.exception.UniqueAttributeException;
import ru.itmo.tg.springbootcrud.labwork.model.Person;
import ru.itmo.tg.springbootcrud.labwork.repository.PersonRepository;
import ru.itmo.tg.springbootcrud.misc.ModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<PersonResponseDTO> getPersons(Integer pageNumber, Integer pageSize, String order, String sortCol) {
        Page<Person> page = personRepository.findAll(PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(order), sortCol)));
        return ModelDTOConverter.toPersonResponseDTOList(page.getContent());
    }

    public PersonResponseDTO getPersonById(Long id) {
        return ModelDTOConverter.convert(personRepository.findById(id).orElseThrow(PersonNotFoundException::new));
    }

    public PersonResponseDTO createPerson(PersonRequestDTO personDTO, User user) {
        if (personRepository.existsByPassportID(personDTO.getPassportId())) {
            throw new UniqueAttributeException("this passport already exists");
        }
        Person person = ModelDTOConverter.convert(personDTO, user);
        person = personRepository.save(person);
        return ModelDTOConverter.convert(person);
    }

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

    public void deletePerson(Long id, User user) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        if (user.getRole() != Role.ROLE_ADMIN && !user.equals(person.getOwner())) {
            throw new InsufficientPermissionsException("no rights to delete Person #" + id);
        }
        personRepository.deleteById(id);
    }

}
