package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.model.Employment;

public interface EmploymentRepository extends JpaRepository<Employment, Long> {
}