package com.invoswift.service;

import com.invoswift.model.Receipt;
import com.invoswift.model.ReceiptItem;
import com.invoswift.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAllOrderByDateDesc();
    }

    public Optional<Receipt> getReceiptById(Long id) {
        return receiptRepository.findById(id);
    }

    public Receipt saveReceipt(Receipt receipt) {
        // Set receipt reference for all items
        if (receipt.getItems() != null) {
            for (ReceiptItem item : receipt.getItems()) {
                item.setReceipt(receipt);
            }
        }
        return receiptRepository.save(receipt);
    }

    public void deleteReceipt(Long id) {
        receiptRepository.deleteById(id);
    }

    public List<Receipt> searchReceipts(String query) {
        return receiptRepository.findByNumberContainingIgnoreCase(query);
    }

    public String generateReceiptNumber() {
        String number;
        do {
            number = generateRandomReceiptNumber();
        } while (receiptRepository.existsByNumber(number));
        return number;
    }

    private String generateRandomReceiptNumber() {
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