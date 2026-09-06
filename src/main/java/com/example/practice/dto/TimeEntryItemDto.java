package com.example.practice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntryItemDto {
    private Long userId;
    private LocalDate date;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
    private Integer workMinutes;
    private String note;
    private Integer version;
    private LocalDateTime createdAt; // 追加
    private LocalDateTime updatedAt; // 追加
}
