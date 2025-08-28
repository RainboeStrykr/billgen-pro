package com.invoswift.repository;

import com.invoswift.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    @Query("SELECT i FROM Invoice i ORDER BY i.date DESC")
    List<Invoice> findAllOrderByDateDesc();
    
    List<Invoice> findByNumberContainingIgnoreCase(String number);
    
    boolean existsByNumber(String number);
}