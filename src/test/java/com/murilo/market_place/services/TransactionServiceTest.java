package com.murilo.market_place.services;

import com.murilo.market_place.domains.Transaction;
import com.murilo.market_place.dtos.transaction.TransactionRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.factory.TransactionFactory;
import com.murilo.market_place.repositories.ITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Captor
    private ArgumentCaptor<UUID> transactionIdCaptor;

    @Captor
    private ArgumentCaptor<UUID> userIdCaptor;

    @Captor
    private ArgumentCaptor<UUID> productIdCaptor;

    private UUID existTransactionId;
    private UUID nonExistTransactionId;
    private UUID existEntityId;
    private UUID nonExistEntityId;
    private Transaction transaction;
    private TransactionRequestDTO transactionRequestDTO;

    @BeforeEach
    void setup() {
        transaction = TransactionFactory.getTransactionInstance();
        transactionRequestDTO = TransactionFactory.getTransactionRequestInstance();
        existTransactionId = transaction.getId();
        existEntityId = UUID.randomUUID();
        nonExistTransactionId = UUID.randomUUID();
        nonExistEntityId = UUID.randomUUID();
    }

    @Nested
    class create {

        @Test
        void testCaseSuccess() {
            when(userService.findById(userIdCaptor.capture()))
                    .thenReturn(transaction.getUser());

            doNothing().when(productService).existsProduct(productIdCaptor.capture());

            when(transactionRepository.save(transactionCaptor.capture()))
                    .thenReturn(transaction);

            Transaction response = transactionService.create(transactionRequestDTO);

            assertNotNull(response);
            assertEquals(transaction.getId(), response.getId());
            assertEquals(transaction.getTotalPayment(), response.getTotalPayment());
            assertEquals(transaction.getMoment(), response.getMoment());
            assertEquals(transaction.getPaymentMethod(), response.getPaymentMethod());
            assertEquals(transaction.getItems().getFirst(), response.getItems().getFirst());
            assertEquals(transaction.getUser(), response.getUser());

            assertEquals(transaction.getTotalPayment(), transactionCaptor.getValue().getTotalPayment());
            assertEquals(transaction.getPaymentMethod(), transactionCaptor.getValue().getPaymentMethod());
            assertEquals(transaction.getItems().getFirst().getQuantity(), transactionCaptor.getValue().getItems().getFirst().getQuantity());
            assertEquals(transaction.getItems().getFirst().getPrice(), transactionCaptor.getValue().getItems().getFirst().getPrice());
            assertEquals(transaction.getItems().getFirst().getProductName(), transactionCaptor.getValue().getItems().getFirst().getProductName());
            assertEquals(transaction.getItems().getFirst().getProduct().getAlbum(), transactionCaptor.getValue().getItems().getFirst().getProduct().getAlbum());
            assertEquals(transaction.getUser(), transactionCaptor.getValue().getUser());

            verify(transactionRepository, atLeastOnce()).save(any());
            verify(userService, atLeastOnce()).findById(any());
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        void testCaseUserNotFound() {
            transactionRequestDTO.setUserId(nonExistEntityId);

            when(userService.findById(userIdCaptor.capture()))
                    .thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> transactionService.create(transactionRequestDTO));
            assertEquals(nonExistEntityId, userIdCaptor.getValue());

            verify(transactionRepository, never()).save(any());
            verify(userService, atLeastOnce()).findById(any());
        }

        @Test
        void testCaseNullUserId() {
            transactionRequestDTO.setUserId(null);

            when(userService.findById(userIdCaptor.capture()))
                    .thenThrow(NullInsertValueException.class);

            assertThrows(NullInsertValueException.class, () -> transactionService.create(transactionRequestDTO));
            assertNull(userIdCaptor.getValue());

            verify(transactionRepository, never()).save(any());
            verify(userService, atLeastOnce()).findById(any());
        }

        @Test
        void testCaseProductNotFound() {
            transactionRequestDTO.getPurchaseProducts().getFirst().setProductId(nonExistEntityId);

            doThrow(EntityNotFoundException.class).when(productService).existsProduct(productIdCaptor.capture());

            assertThrows(EntityNotFoundException.class, () -> transactionService.create(transactionRequestDTO));
            assertEquals(nonExistEntityId, productIdCaptor.getValue());

            verify(transactionRepository, never()).save(any());
            verify(productService, atLeastOnce()).existsProduct(any());
        }

        @Test
        void testCaseNullProductId() {
            transactionRequestDTO.getPurchaseProducts().getFirst().setProductId(null);

            doThrow(NullInsertValueException.class).when(productService).existsProduct(productIdCaptor.capture());

            assertThrows(NullInsertValueException.class, () -> transactionService.create(transactionRequestDTO));
            assertNull(productIdCaptor.getValue());

            verify(transactionRepository, never()).save(any());
            verify(productService, atLeastOnce()).existsProduct(any());
        }

        @Test
        void testCaseExceptionTriggersRollback() {
            when(userService.findById(userIdCaptor.capture()))
                    .thenReturn(transaction.getUser());

            doNothing().when(productService).existsProduct(productIdCaptor.capture());

            when(transactionRepository.save(transactionCaptor.capture()))
                    .thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> transactionService.create(transactionRequestDTO));
            assertEquals(transaction.getTotalPayment(), transactionCaptor.getValue().getTotalPayment());
            assertEquals(transaction.getPaymentMethod(), transactionCaptor.getValue().getPaymentMethod());
            assertEquals(transaction.getItems().getFirst().getQuantity(), transactionCaptor.getValue().getItems().getFirst().getQuantity());
            assertEquals(transaction.getItems().getFirst().getPrice(), transactionCaptor.getValue().getItems().getFirst().getPrice());
            assertEquals(transaction.getItems().getFirst().getProductName(), transactionCaptor.getValue().getItems().getFirst().getProductName());
            assertEquals(transaction.getItems().getFirst().getProduct().getAlbum(), transactionCaptor.getValue().getItems().getFirst().getProduct().getAlbum());
            assertEquals(transaction.getUser(), transactionCaptor.getValue().getUser());

            verify(transactionRepository, atLeastOnce()).save(any());
            verify(userService, atLeastOnce()).findById(any());
        }
    }

    @Nested
    class findAllByUser {

        @Test
        void testCaseSuccess() {
            when(transactionRepository.findByUserId(userIdCaptor.capture(), any()))
                    .thenReturn(new PageImpl<>(List.of(transaction)));

            Page<Transaction> response = transactionService.findAll(existEntityId, PageRequest.of(0,10));

            assertNotNull(response);
            assertEquals(existEntityId, userIdCaptor.getValue());

            verify(transactionRepository, atLeastOnce()).findByUserId(any(), any());
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        void testCaseUserNotFound() {
            doThrow(EntityNotFoundException.class).when(userService).existsUser(userIdCaptor.capture());

            assertThrows(EntityNotFoundException.class, () -> transactionService.findAll(nonExistEntityId, PageRequest.of(0,10)));
            assertEquals(nonExistEntityId, userIdCaptor.getValue());

            verify(transactionRepository, never()).findByUserId(any(), any());
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        void testCaseNullUserId() {
            doThrow(NullInsertValueException.class).when(userService).existsUser(userIdCaptor.capture());

            assertThrows(NullInsertValueException.class, () -> transactionService.findAll(null, PageRequest.of(0,10)));
            assertNull(userIdCaptor.getValue());

            verify(transactionRepository, never()).findWithItemsById(any());
            verifyNoMoreInteractions(transactionRepository);
        }
    }

    @Nested
    class findById {

        @Test
        void testCaseSuccess() {
            when(transactionRepository.findWithItemsById(transactionIdCaptor.capture())).thenReturn(Optional.ofNullable(transaction));

            Transaction response = transactionService.findById(existTransactionId);

            assertNotNull(response);
            assertEquals(transaction.getId(), response.getId());
            assertEquals(transaction.getTotalPayment(), response.getTotalPayment());
            assertEquals(transaction.getMoment(), response.getMoment());
            assertEquals(transaction.getPaymentMethod(), response.getPaymentMethod());
            assertEquals(transaction.getItems().getFirst(), response.getItems().getFirst());
            assertEquals(transaction.getUser(), response.getUser());

            assertEquals(existTransactionId, transactionIdCaptor.getValue());

            verify(transactionRepository, atLeastOnce()).findWithItemsById(any());
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        void testCaseNotFound() {
            when(transactionRepository.findWithItemsById(transactionIdCaptor.capture())).thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> transactionService.findById(nonExistTransactionId));
            assertEquals(nonExistTransactionId, transactionIdCaptor.getValue());

            verify(transactionRepository, atLeastOnce()).findWithItemsById(any());
            verifyNoMoreInteractions(transactionRepository);
        }

        @Test
        void testCaseNullId() {
            assertThrows(NullInsertValueException.class, () -> transactionService.findById(null));

            verify(transactionRepository, never()).findWithItemsById(any());
            verifyNoMoreInteractions(transactionRepository);
        }
    }
}
