package com.billgenpro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.billgenpro.model.Receipt;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    
    @Query("SELECT r FROM Receipt r ORDER BY r.date DESC")
    List<Receipt> findAllOrderByDateDesc();
    
    List<Receipt> findByNumberContainingIgnoreCase(String number);
    
    boolean existsByNumber(String number);
}