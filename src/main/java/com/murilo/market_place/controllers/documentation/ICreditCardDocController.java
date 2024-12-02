package com.murilo.market_place.controllers.documentation;

import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.dtos.creditCard.CreditCardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Credit Card Management", description = "Operations related to managing credit cards")
public interface ICreditCardDocController {

    @Operation(
            description = "Register credit card to a user",
            summary = "Responsible for registering an credit card, needs an user to be linked",
            responses = {
                    @ApiResponse(description = "Credit Card registered", responseCode = "201"),
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
    ResponseEntity<CreditCardResponseDTO> addCreditCard(CreditCardRequestDTO creditCardRequestDTO);

    @Operation(
            description = "List all credit card linked to a user",
            summary = "Responsible to list all credit cards linked to a user, based on user id",
            responses = {
                    @ApiResponse(description = "Credit cards found", responseCode = "200"),
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
    ResponseEntity<List<CreditCardResponseDTO>> listAllCardsFromUser(UUID userId);

    @Operation(
            description = "Get a specific credit card",
            summary = "Responsible to get a specific credit card, based on id",
            responses = {
                    @ApiResponse(description = "Credit Card found", responseCode = "200"),
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
    ResponseEntity<CreditCardResponseDTO> findCardById(UUID cardId);

    @Operation(
            description = "Delete a specific credit card",
            summary = "Responsible to delete a specific credit card, based on id",
            responses = {
                    @ApiResponse(description = "Credit Card deleted", responseCode = "200"),
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
    ResponseEntity<Void> deleteCardById(UUID cardId);
}
