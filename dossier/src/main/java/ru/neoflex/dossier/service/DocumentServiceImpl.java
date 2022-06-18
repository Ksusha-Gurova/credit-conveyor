package ru.neoflex.dossier.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.client.dto.ApplicationDTO;

import java.io.File;
import java.io.FileOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final static String CLIENT_DATA_TITLE = "Данные клиента";
    private final static String CREDIT_DATA_TITLE = "Данные по кредиту";
    private final static String PAYMENT_SCHEDULE_TITLE = "График платежей";
    private final static Font FONT = FontFactory.getFont("/fonts/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);

    @Override
    public File createClientDataDocument(ApplicationDTO application, Long applicationId) throws Exception {
        log.debug("createClientDataDocument()");

        try {
            String pathToSavedDocumentPdf = "client_data.pdf";
            log.debug("createClientDataDocument(), обозначили имя файла, pathToSavedDocumentPdf = {}", pathToSavedDocumentPdf);

            String applicationNumber = "Заявка №";
            String fullName = "Полное имя клиента: ";
            String birthDate = "Дата рождения: ";
            String gender = "Пол: ";
            String maritalStatus = "Семейное положение: ";
            String dependentAmount = "Колличество еждевенцев: ";
            String email = "Email: ";
            String passportSeries = "Серия паспорта: ";
            String passportNumber = "Номер паспорта: ";
            String passportIssueDate = "Дата выдачи: ";
            String passportIssueBranch = "Код подразделения: ";
            String employmentStatus = "Рабочий статус: ";
            String employerINN = "ИНН: ";
            String salary = "Размер заработной платы: ";
            String position = "Должность: ";
            String workExperienceTotal = "Общий опыт работы: ";
            String workExperienceCurrent = "Опыт работы на текущем месте: ";
            String account = "Счет клиента: ";

            Document clientDataDocument = new Document();
            File clientDataFile = new File(pathToSavedDocumentPdf);
            PdfWriter.getInstance(clientDataDocument, new FileOutputStream(clientDataFile));

            clientDataDocument.open();
            Paragraph title = new Paragraph(CLIENT_DATA_TITLE, FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            clientDataDocument.add(title);

            Paragraph applicationNumberTitle = new Paragraph(applicationNumber + applicationId, FONT);
            applicationNumberTitle.setAlignment(Element.ALIGN_CENTER);
            clientDataDocument.add(applicationNumberTitle);
            clientDataDocument.add(Chunk.NEWLINE);

            if (application.getMiddleName() != null){
                clientDataDocument.add(new Paragraph(fullName + application.getFirstName() + " " + application.getMiddleName() + " " + application.getLastName(), FONT));
            } else clientDataDocument.add(new Paragraph(fullName + application.getFirstName() + " " + application.getLastName(), FONT));
            clientDataDocument.add(new Paragraph(birthDate + application.getBirthdate(), FONT));
            clientDataDocument.add(new Paragraph(gender + application.getGender(), FONT));
            clientDataDocument.add(new Paragraph(maritalStatus + application.getMaritalStatus(), FONT));
            clientDataDocument.add(new Paragraph(dependentAmount + application.getDependentAmount(), FONT));
            clientDataDocument.add(new Paragraph(email + application.getEmail(), FONT));
            clientDataDocument.add(new Paragraph(passportSeries + application.getPassportSeries(), FONT));
            clientDataDocument.add(new Paragraph(passportNumber + application.getPassportNumber(), FONT));
            clientDataDocument.add(new Paragraph(passportIssueDate + application.getPassportIssueDate(), FONT));
            clientDataDocument.add(new Paragraph(passportIssueBranch + application.getPassportIssueBranch(), FONT));
            clientDataDocument.add(new Paragraph(employmentStatus + application.getEmploymentStatus(), FONT));
            clientDataDocument.add(new Paragraph(employerINN + application.getEmployerINN(), FONT));
            clientDataDocument.add(new Paragraph(salary + application.getSalary(), FONT));
            clientDataDocument.add(new Paragraph(position + application.getPosition(), FONT));
            clientDataDocument.add(new Paragraph(workExperienceTotal + application.getWorkExperienceTotal(), FONT));
            clientDataDocument.add(new Paragraph(workExperienceCurrent + application.getWorkExperienceCurrent(), FONT));
            clientDataDocument.add(new Paragraph(account + application.getAccount(), FONT));
            clientDataDocument.close();
            log.debug("createClientDataDocument(),сформировали client_data.pdf");

            return clientDataFile;
        } catch (Exception e) {
            log.error("createClientDataDocument(), не удалось создать client_data.pdf");
            throw new Exception("Не удалось сформировать client_data.pdf");
        }
    }


    @Override
    public File createCreditDataDocument(ApplicationDTO application, Long applicationId) throws Exception {
        log.debug("createCreditDataDocument()");

        try {
            String pathToSavedDocumentPdf = "credit_data.pdf";
            log.debug("createCreditDataDocument(), обозначили имя файла, pathToSavedDocumentPdf = {}", pathToSavedDocumentPdf);

            String applicationNumber = "Заявка №";
            String amount = "Сумма кредита: ";
            String term = "Срок в месяцах: ";
            String monthlyPayment = "Ежемесячный платеж: ";
            String rate = "Процентная ставка: ";
            String psk = "Полная стоимость кредита: ";
            String isInsuranceEnabled = "Страховка: ";
            String isSalaryClient = "Является зарплатным клиентом: ";

            Document creditDataDocument = new Document();
            File creditDataFile = new File(pathToSavedDocumentPdf);
            PdfWriter.getInstance(creditDataDocument, new FileOutputStream(creditDataFile));

            creditDataDocument.open();
            Paragraph title = new Paragraph(CREDIT_DATA_TITLE, FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            creditDataDocument.add(title);

            Paragraph applicationNumberTitle = new Paragraph(applicationNumber + applicationId, FONT);
            applicationNumberTitle.setAlignment(Element.ALIGN_CENTER);
            creditDataDocument.add(applicationNumberTitle);
            creditDataDocument.add(Chunk.NEWLINE);

            creditDataDocument.add(new Paragraph(amount + application.getAmount(), FONT));
            creditDataDocument.add(new Paragraph(term + application.getTerm(), FONT));
            creditDataDocument.add(new Paragraph(monthlyPayment + application.getMonthlyPayment(), FONT));
            creditDataDocument.add(new Paragraph(rate + application.getRate() + "%", FONT));
            creditDataDocument.add(new Paragraph(psk + application.getPsk() + "%", FONT));
            if (application.getIsInsuranceEnabled()){
                creditDataDocument.add(new Paragraph(isInsuranceEnabled + "включена", FONT));
            } else creditDataDocument.add(new Paragraph(isInsuranceEnabled + "-", FONT));
            if (application.getIsSalaryClient()){
                creditDataDocument.add(new Paragraph(isSalaryClient + "да", FONT));
            } else creditDataDocument.add(new Paragraph(isSalaryClient + "нет", FONT));
            creditDataDocument.close();
            log.debug("createCreditDataDocument(),сформировали credit_data.pdf");

            return creditDataFile;
        } catch (Exception e) {
            log.error("createCreditDataDocument(), не удалось создать credit_data.pdf");
            throw new Exception("Не удалось создать credit_data.pdf");
        }
    }

    @Override
    public File createPaymentScheduleDocument(ApplicationDTO application, Long applicationId) throws Exception {
        log.debug("createPaymentScheduleDocument()");

        try {
            String pathToSavedDocumentPdf = "payment_schedule.pdf";
            log.debug("createPaymentScheduleDocument(), обозначили имя файла, pathToSavedDocumentPdf = {}", pathToSavedDocumentPdf);

            String applicationNumber = "Заявка №";
            String number = "Платеж № ";
            String date = "Дата платежа: ";
            String totalPayment = "Сумма платежа: ";
            String interestPayment = "Выплата процентов: ";
            String debtPayment = "Погашение основного долга: ";
            String remainingDebt = "Остаток: ";


            Document paymentScheduleDocument = new Document();
            File paymentScheduleFile = new File(pathToSavedDocumentPdf);
            PdfWriter.getInstance(paymentScheduleDocument, new FileOutputStream(paymentScheduleFile));

            paymentScheduleDocument.open();
            Paragraph title = new Paragraph(PAYMENT_SCHEDULE_TITLE, FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            paymentScheduleDocument.add(title);

            Paragraph applicationNumberTitle = new Paragraph(applicationNumber + applicationId, FONT);
            applicationNumberTitle.setAlignment(Element.ALIGN_CENTER);
            paymentScheduleDocument.add(applicationNumberTitle);
            paymentScheduleDocument.add(Chunk.NEWLINE);

            for (int i = 0; i <= application.getTerm(); i++){
                paymentScheduleDocument.add(new Paragraph(number + application.getPaymentSchedule().get(i).getNumber(), FONT));
                paymentScheduleDocument.add(new Paragraph(date + application.getPaymentSchedule().get(i).getDate(), FONT));
                paymentScheduleDocument.add(new Paragraph(totalPayment + application.getPaymentSchedule().get(i).getTotalPayment(), FONT));
                paymentScheduleDocument.add(new Paragraph(interestPayment + application.getPaymentSchedule().get(i).getInterestPayment(), FONT));
                paymentScheduleDocument.add(new Paragraph(debtPayment + application.getPaymentSchedule().get(i).getDebtPayment(), FONT));
                paymentScheduleDocument.add(new Paragraph(remainingDebt + application.getPaymentSchedule().get(i).getRemainingDebt(), FONT));
                paymentScheduleDocument.add(Chunk.NEWLINE);
            }
            paymentScheduleDocument.close();
            log.debug("createPaymentScheduleDocument(),сформировали payment_schedule.pdf");

            return paymentScheduleFile;
        } catch (Exception e) {
            log.error("createPaymentScheduleDocument(), не удалось создать payment_schedule.pdf");
            throw new Exception("Не удалось создать payment_schedule.pdf");
        }
    }
}
