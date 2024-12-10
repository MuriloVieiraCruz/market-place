package com.murilo.market_place.controllers.documentation;

import com.murilo.market_place.dtos.transaction.TransactionRequestDTO;
import com.murilo.market_place.dtos.transaction.TransactionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Transaction Management", description = "Operations related to managing transactions such as creation, listAll, findById")
public interface ITransactionDocController {

    @Operation(
            description = "Register transaction to a user",
            summary = "Responsible for registering an transaction, needs an user and credit card to be linked",
            responses = {
                    @ApiResponse(description = "Transaction registered", responseCode = "201"),
                    @ApiResponse(
                            description = "When the request is sent without the body or with some attribute missing",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"error\": \"The request body has some errors or doesn't exist\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "When the attribute has an invalid format",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"errors\": \"code: 008 message: error message\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<TransactionResponseDTO> create(TransactionRequestDTO requestDTO);

    @Operation(
            description = "List All transaction to a user",
            summary = "Responsible for list all transactions referenced to a specific user",
            responses = {
                    @ApiResponse(description = "Transactions found", responseCode = "200"),
            }
    )
    ResponseEntity<Page<TransactionResponseDTO>> findAll(int page, int size, UUID userId);

    @Operation(
            description = "Find specific transaction to a user",
            summary = "Responsible for get an transaction",
            responses = {
                    @ApiResponse(description = "Transaction found", responseCode = "200"),
                    @ApiResponse(
                            description = "When the request is sent without the body or with some attribute missing",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"error\": \"The request body has some errors or doesn't exist\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "When the attribute has an invalid format",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"errors\": \"code: 008 message: error message\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<TransactionResponseDTO> findById(UUID transactionId);
}
