package ru.neoflex.deal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employment")
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "employment_status", nullable = false)
    private String employmentStatus;

    @Column(name = "employer_inn", nullable = false)
    private String employerINN;

    @Column(name = "salary", nullable = false)
    private BigDecimal salary;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "work_experience_total", nullable = false)
    private Integer workExperienceTotal;

    @Column(name = "work_experience_current")
    private Integer workExperienceCurrent;



}