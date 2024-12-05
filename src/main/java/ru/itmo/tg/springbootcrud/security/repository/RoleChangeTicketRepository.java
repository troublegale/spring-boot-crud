package ru.itmo.tg.springbootcrud.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.tg.springbootcrud.security.model.RoleChangeTicket;
import ru.itmo.tg.springbootcrud.security.model.User;

import java.util.List;

public interface RoleChangeTicketRepository extends JpaRepository<RoleChangeTicket, Long> {

    List<RoleChangeTicket> findByUser(User user);

}
