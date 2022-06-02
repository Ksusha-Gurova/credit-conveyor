package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}