package ru.neoflex.dossier.service;

import com.itextpdf.text.DocumentException;
import ru.neoflex.dossier.client.dto.ApplicationDTO;

import java.io.File;
import java.io.FileNotFoundException;

public interface DocumentService {

    File createClientDataDocument(ApplicationDTO application, Long applicationId) throws Exception;

    File createCreditDataDocument(ApplicationDTO application, Long applicationId) throws Exception;

    File createPaymentScheduleDocument(ApplicationDTO application, Long applicationId) throws Exception;
}
