package org.financial.assistant.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.financial.assistant.models.Transaction;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class TransactionPdfExporter {

    public TransactionPdfExporter() {
    }

    public void exportTransactionsToPdf(List<Transaction> transactions, HttpServletResponse response) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=transactions.pdf";
        response.setHeader(headerKey, headerValue);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            for (Transaction transaction : transactions) {
                addTransactionToDocument(transaction, document);
            }

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public void exportTransactionToPdf(Transaction transaction, HttpServletResponse response) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=transaction_" + transaction.getId() + ".pdf";
        response.setHeader(headerKey, headerValue);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            addTransactionToDocument(transaction, document);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addTransactionToDocument(Transaction transaction, Document document) throws DocumentException {
        document.add(new Paragraph("Transaction ID: " + transaction.getId()));
        document.add(new Paragraph("Name: " + transaction.getName()));
        document.add(new Paragraph("Amount: " + Transaction.formatAmount(transaction.getAmount())));
        document.add(new Paragraph("Category: " + transaction.getCategory().getName()));
        document.add(new Paragraph("Description: " + transaction.getDescription()));
        document.add(new Paragraph("Is Accounting: " + transaction.getIsAccounting()));
        document.add(new Paragraph("Date: " + transaction.formatCreatedAt(transaction.getCreatedAt())));
        document.add(new Paragraph("----------------------------------------------"));
    }
}
