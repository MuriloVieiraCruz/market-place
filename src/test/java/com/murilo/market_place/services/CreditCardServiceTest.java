package com.murilo.market_place.services;

import com.murilo.market_place.domains.CreditCard;
import com.murilo.market_place.dtos.creditCard.CreditCardRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.factory.CreditCardFactory;
import com.murilo.market_place.mapper.CreditCardMapper;
import com.murilo.market_place.repositories.ICreditCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditCardServiceTest {

    @InjectMocks
    private CreditCardService creditCardService;

    @Mock
    private ICreditCardRepository creditCardRepository;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<CreditCard> cardCaptor;

    @Captor
    private ArgumentCaptor<UUID> cardIdCaptor;

    @Captor
    private ArgumentCaptor<UUID> userIdCaptor;

    private UUID existCreditCardId;
    private UUID nonExistCreditCardId;
    private CreditCard creditCard;
    private CreditCardRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        creditCard = CreditCardFactory.getCardInstance();
        requestDTO = CreditCardFactory.getCardRequestInstance();
        existCreditCardId = creditCard.getId();
        nonExistCreditCardId = UUID.randomUUID();
    }

    @Nested
    class create {

        @Test
        void testCaseSuccess() {
            when(creditCardRepository.save(cardCaptor.capture()))
                    .thenReturn(creditCard);

            when(userService.findById(userIdCaptor.capture()))
                    .thenReturn(creditCard.getUser());

            CreditCard response = creditCardService.create(requestDTO);

            assertNotNull(response);
            assertEquals(creditCard.getId(), response.getId());
            assertEquals(creditCard.getNumber(), response.getNumber());
            assertEquals(creditCard.getHolderName(), response.getHolderName());
            assertEquals(creditCard.getCvv(), response.getCvv());
            assertEquals(creditCard.getExpirationDate(), response.getExpirationDate());
            assertEquals(creditCard.getUser(), response.getUser());
            assertEquals(requestDTO.getId(), cardCaptor.getValue().getId());
            assertEquals(requestDTO.getNumber(), cardCaptor.getValue().getNumber());
            assertEquals(requestDTO.getHolderName(), cardCaptor.getValue().getHolderName());
            assertEquals(requestDTO.getCvv(), cardCaptor.getValue().getCvv());
            assertEquals(requestDTO.getExpirationDate(), cardCaptor.getValue().getExpirationDate());
            assertEquals(requestDTO.getUserId(), userIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).save(any());
            verify(userService, atLeastOnce()).findById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }

        @Test
        void testCaseUserNotFound() {
            when(creditCardRepository.save(cardCaptor.capture()))
                    .thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> creditCardService.create(requestDTO));
            assertEquals(CreditCardMapper.toCreditCard(requestDTO), cardCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).save(any());
            verify(userService, atLeastOnce()).findById(any());
        }

        @Test
        void testCaseExceptionTriggersRollback() {
            when(userService.findById(userIdCaptor.capture()))
                    .thenReturn(creditCard.getUser());

            when(creditCardRepository.save(cardCaptor.capture()))
                    .thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> creditCardService.create(requestDTO));
            assertEquals(requestDTO.getId(), cardCaptor.getValue().getId());
            assertEquals(requestDTO.getNumber(), cardCaptor.getValue().getNumber());
            assertEquals(requestDTO.getHolderName(), cardCaptor.getValue().getHolderName());
            assertEquals(requestDTO.getCvv(), cardCaptor.getValue().getCvv());
            assertEquals(requestDTO.getExpirationDate(), cardCaptor.getValue().getExpirationDate());
            assertEquals(requestDTO.getUserId(), userIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).save(any());
            verify(userService, atLeastOnce()).findById(any());
        }
    }

    @Nested
    class findAllCardsByUser {

        @Test
        void testCaseSuccess() {
            when(creditCardRepository.findAllByUserId(userIdCaptor.capture()))
                    .thenReturn(List.of(creditCard));

            List<CreditCard> response = creditCardService.findAllCardsByUser(creditCard.getUser().getId());

            assertNotNull(response);
            assertEquals(creditCard.getUser().getId(), userIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).findAllByUserId(any());
            verifyNoMoreInteractions(creditCardRepository);
        }

        @Test
        void testCaseNullUserId() {
            assertThrows(NullInsertValueException.class, () -> creditCardService.findAllCardsByUser(null));

            verify(creditCardRepository, never()).findAllByUserId(any());
            verifyNoMoreInteractions(creditCardRepository);
        }
    }

    @Nested
    class findById {

        @Test
        void testCaseSuccess() {
            when(creditCardRepository.findById(cardIdCaptor.capture())).thenReturn(Optional.ofNullable(creditCard));

            CreditCard response = creditCardService.findById(existCreditCardId);

            assertNotNull(response);
            assertEquals(creditCard.getId(), response.getId());
            assertEquals(creditCard.getNumber(), response.getNumber());
            assertEquals(creditCard.getHolderName(), response.getHolderName());
            assertEquals(creditCard.getCvv(), response.getCvv());
            assertEquals(creditCard.getExpirationDate(), response.getExpirationDate());
            assertEquals(creditCard.getUser(), response.getUser());

            assertEquals(existCreditCardId, cardIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).findById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }

        @Test
        void testCaseNotFound() {
            when(creditCardRepository.findById(cardIdCaptor.capture())).thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> creditCardService.findById(nonExistCreditCardId));
            assertEquals(nonExistCreditCardId, cardIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).findById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }

        @Test
        void testCaseNullId() {
            assertThrows(NullInsertValueException.class, () -> creditCardService.findById(null));

            verify(creditCardRepository, never()).findById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }
    }

    @Nested
    class deleteById {

        @Test
        void testCaseSuccessDelete() {
            when(creditCardRepository.existsById(existCreditCardId)).thenReturn(true);
            doNothing().when(creditCardRepository).deleteById(cardIdCaptor.capture());

            creditCardService.deleteById(existCreditCardId);

            assertEquals(existCreditCardId, cardIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).existsById(any());
            verify(creditCardRepository, atLeastOnce()).deleteById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }

        @Test
        void testCaseNotFoundIdDelete() {
            doThrow(EntityNotFoundException.class).when(creditCardRepository).existsById(cardIdCaptor.capture());

            assertThrows(EntityNotFoundException.class, () -> creditCardService.deleteById(nonExistCreditCardId));
            assertEquals(nonExistCreditCardId, cardIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).existsById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }

        @Test
        void testCaseNullId() {
            assertThrows(NullInsertValueException.class, () -> creditCardService.deleteById(null));

            verify(creditCardRepository, never()).existsById(any());
            verify(creditCardRepository, never()).deleteById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }
    }
}
