package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}