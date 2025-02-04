package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonDTO;
import ru.itmo.tg.springbootcrud.labwork.model.Person;
import ru.itmo.tg.springbootcrud.labwork.repository.PersonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final ModelDTOConverter modelDTOConverter;

    public List<PersonDTO> getPersons(Integer pageNumber, Integer pageSize, String order, String sortCol) {
        Page<Person> page = personRepository.findAll(PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(order), sortCol)));
        return modelDTOConverter.toPersonDTOList(page.getContent());
    }

    public PersonDTO getPersonById(Long id) {
        return modelDTOConverter.convert(personRepository.findById(id).orElseThrow());
    }

    public void createPerson(PersonDTO personDTO) {
        Person person = modelDTOConverter.convert(personDTO);
        personRepository.save(person);
    }

    public void updatePerson(Long id, PersonDTO personDTO) {
        personDTO.setId(id);
        personRepository.save(modelDTOConverter.convert(personDTO));
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

}
