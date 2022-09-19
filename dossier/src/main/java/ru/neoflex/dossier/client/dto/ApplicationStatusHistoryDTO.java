package ru.neoflex.dossier.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Builder
@Getter
public class ApplicationStatusHistoryDTO {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
