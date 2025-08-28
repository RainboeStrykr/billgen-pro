package com.billgenpro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.billgenpro.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    @Query("SELECT i FROM Invoice i ORDER BY i.date DESC")
    List<Invoice> findAllOrderByDateDesc();
    
    List<Invoice> findByNumberContainingIgnoreCase(String number);
    
    boolean existsByNumber(String number);
}