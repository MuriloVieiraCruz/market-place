package com.murilo.market_place.controllers;

import com.murilo.market_place.controllers.documentation.IUserDocController;
import com.murilo.market_place.dtos.user.UserRequestDTO;
import com.murilo.market_place.dtos.user.UserResponseDTO;
import com.murilo.market_place.mapper.UserMapper;
import com.murilo.market_place.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements IUserDocController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponse(userService.createUser(userRequestDTO)));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> update(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toResponse(userService.updateUser(userRequestDTO)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toResponse(userService.findById(userId)));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
        userService.deleteById(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
