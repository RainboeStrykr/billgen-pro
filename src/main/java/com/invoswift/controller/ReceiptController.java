package com.invoswift.controller;

import com.invoswift.model.Receipt;
import com.invoswift.model.ReceiptItem;
import com.invoswift.model.Company;
import com.invoswift.service.ReceiptService;
import com.invoswift.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public String listReceipts(Model model) {
        model.addAttribute("receipts", receiptService.getAllReceipts());
        return "receipts/list";
    }

    @GetMapping("/new")
    public String newReceipt(Model model) {
        Receipt receipt = new Receipt();
        receipt.setNumber(receiptService.generateReceiptNumber());
        receipt.setDate(LocalDate.now());
        receipt.setCompany(new Company());
        
        // Add one empty item
        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem());
        receipt.setItems(items);
        
        model.addAttribute("receipt", receipt);
        return "receipts/form";
    }

    @GetMapping("/{id}")
    public String viewReceipt(@PathVariable Long id, Model model) {
        Receipt receipt = receiptService.getReceiptById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));
        model.addAttribute("receipt", receipt);
        return "receipts/view";
    }

    @GetMapping("/{id}/edit")
    public String editReceipt(@PathVariable Long id, Model model) {
        Receipt receipt = receiptService.getReceiptById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));
        model.addAttribute("receipt", receipt);
        return "receipts/form";
    }

    @PostMapping("/save")
    public String saveReceipt(@Valid @ModelAttribute Receipt receipt, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "receipts/form";
        }

        // Remove empty items
        if (receipt.getItems() != null) {
            receipt.getItems().removeIf(item -> 
                item.getName() == null || item.getName().trim().isEmpty());
        }

        receiptService.saveReceipt(receipt);
        return "redirect:/receipts";
    }

    @GetMapping("/{id}/delete")
    public String deleteReceipt(@PathVariable Long id) {
        receiptService.deleteReceipt(id);
        return "redirect:/receipts";
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        Receipt receipt = receiptService.getReceiptById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        byte[] pdfBytes = pdfService.generateReceiptPdf(receipt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "receipt-" + receipt.getNumber() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}