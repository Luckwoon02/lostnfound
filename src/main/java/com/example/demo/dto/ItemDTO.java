package com.example.demo.dto;

import com.example.demo.entity.ItemCategory;
import com.example.demo.entity.ItemStatus;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ItemDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Status is required")
    private ItemStatus status;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private ItemCategory category;
    
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String imageUrl;
    private LocalDateTime dateReported;
}
