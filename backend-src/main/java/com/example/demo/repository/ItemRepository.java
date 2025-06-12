package com.example.demo.repository;

import com.example.demo.entity.Item;
import com.example.demo.entity.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStatus(ItemStatus status);
    List<Item> findByCategory(String category);
    List<Item> findByLocationContainingIgnoreCase(String location);
    
    @Query("SELECT i FROM Item i WHERE " +
           "LOWER(i.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Item> searchItems(String keyword);
    
    List<Item> findByDateReportedBetween(LocalDateTime start, LocalDateTime end);
}