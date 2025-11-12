package com.example.backend.service;

import com.example.backend.dto.request.LoanRequest;
import com.example.backend.dto.response.LoanResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanService {
    List<LoanResponse> getAllLoans();
    LoanResponse findLoanById(Integer id);
    LoanResponse createLoan(LoanRequest loanRequest);
    LoanResponse updateLoan(Integer id, LoanRequest loanRequest);
    LoanResponse returnLoan(Integer id);
    List<LoanResponse> searchLoans(Integer userId, LocalDateTime startDate, LocalDateTime endDate);
}
