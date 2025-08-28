package com.invoswift.service;

import com.invoswift.model.Invoice;
import com.invoswift.model.InvoiceItem;
import com.invoswift.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAllOrderByDateDesc();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice saveInvoice(Invoice invoice) {
        // Set invoice reference for all items
        if (invoice.getItems() != null) {
            for (InvoiceItem item : invoice.getItems()) {
                item.setInvoice(invoice);
            }
        }
        return invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    public List<Invoice> searchInvoices(String query) {
        return invoiceRepository.findByNumberContainingIgnoreCase(query);
    }

    public String generateInvoiceNumber() {
        String number;
        do {
            number = generateRandomInvoiceNumber();
        } while (invoiceRepository.existsByNumber(number));
        return number;
    }

    private String generateRandomInvoiceNumber() {
        Random random = new Random();
        int length = random.nextInt(6) + 3; // 3-8 characters
        int alphabetCount = Math.min(random.nextInt(4), length);
        StringBuilder result = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";

        // Add alphabet characters
        for (int i = 0; i < alphabetCount; i++) {
            result.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        // Add number characters
        for (int i = alphabetCount; i < length; i++) {
            result.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        return result.toString();
    }
}