package ru.itmo.tg.springbootcrud.security.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itmo.tg.springbootcrud.security.model.RoleChangeRequest;

public interface RoleChangeRequestRepository extends CrudRepository<RoleChangeRequest, Long> {
}
