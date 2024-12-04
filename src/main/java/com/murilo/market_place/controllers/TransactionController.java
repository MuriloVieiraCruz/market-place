package com.murilo.market_place.controllers;

import com.murilo.market_place.controllers.documentation.ITransactionDocController;
import com.murilo.market_place.dtos.transaction.TransactionRequestDTO;
import com.murilo.market_place.dtos.transaction.TransactionResponseDTO;
import com.murilo.market_place.mapper.TransactionMapper;
import com.murilo.market_place.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController implements ITransactionDocController {

    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(transactionService.create(requestDTO)));
    }

    @Override
    public ResponseEntity<Page<TransactionResponseDTO>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new PageImpl<>(transactionService.findAll(PageRequest.of(page, size)).stream()
                        .map(TransactionMapper::toResponse).toList()));
    }

    @Override
    public ResponseEntity<TransactionResponseDTO> findById(UUID transactionId) {
        return ResponseEntity.status(HttpStatus.OK).body(TransactionMapper.toResponse(transactionService.findById(transactionId)));
    }
}
