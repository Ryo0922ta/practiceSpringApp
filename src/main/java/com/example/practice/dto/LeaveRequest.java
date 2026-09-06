package com.example.practice.dto;

import com.example.practice.domain.attendance.LeaveStatus;
import com.example.practice.domain.attendance.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
	private Long userId;
	private LeaveType leaveType;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reason;
	private UUID approverUserId;
	private LeaveStatus status;
	private Integer version;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
