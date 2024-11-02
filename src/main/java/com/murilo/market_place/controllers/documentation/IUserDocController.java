package com.murilo.market_place.controllers.documentation;

import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.dtos.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "User Management", description = "Operations related to managing users such as creation, update, search and deletion")
public interface IUserDocController {

    @Operation(
            description = "Register User",
            summary = "Responsible for register an user, when registered, the application will send a e-mail confirmation",
            responses = {
                    @ApiResponse(description = "User registered", responseCode = "201"),
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
    ResponseEntity<UserResponseDTO> create(UserRequestDTO userRequestDTO);

    @Operation(
            description = "Update user",
            summary = "Responsible for update an user, all attributes are required ",
            responses = {
                    @ApiResponse(description = "User updated", responseCode = "201"),
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
    ResponseEntity<UserResponseDTO> update(UserRequestDTO userRequestDTO);

    @Operation(
            description = "Find user by ID",
            summary = "Responsible for search an product by ID",
            responses = {
                    @ApiResponse(description = "Product found", responseCode = "200"),
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
    ResponseEntity<UserResponseDTO> findById(UUID userId);

    @Operation(
            description = "Remove an user",
            summary = "Responsible for delete an user, "
                    + "requires the user id",
            responses = {
                    @ApiResponse(description = "User deleted", responseCode = "200"),
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
    ResponseEntity<Void> delete(UUID userId);
}
