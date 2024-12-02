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
import org.springframework.dao.EmptyResultDataAccessException;

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

    private CreditCard creditCard;
    private CreditCardRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        creditCard = CreditCardFactory.getCardInstance();
        requestDTO = CreditCardFactory.getCardRequestInstance();
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
            assertEquals(CreditCardMapper.toResponse(creditCard), CreditCardMapper.toResponse(response));
            assertEquals(requestDTO.id(), cardCaptor.getValue().getId());
            assertEquals(requestDTO.number(), cardCaptor.getValue().getNumber());
            assertEquals(requestDTO.holderName(), cardCaptor.getValue().getHolderName());
            assertEquals(requestDTO.cvv(), cardCaptor.getValue().getCvv());
            assertEquals(requestDTO.expirationDate(), cardCaptor.getValue().getExpirationDate());
            assertEquals(requestDTO.userId(), userIdCaptor.getValue());

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
            assertEquals(requestDTO.id(), cardCaptor.getValue().getId());
            assertEquals(requestDTO.number(), cardCaptor.getValue().getNumber());
            assertEquals(requestDTO.holderName(), cardCaptor.getValue().getHolderName());
            assertEquals(requestDTO.cvv(), cardCaptor.getValue().getCvv());
            assertEquals(requestDTO.expirationDate(), cardCaptor.getValue().getExpirationDate());
            assertEquals(requestDTO.userId(), userIdCaptor.getValue());

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

            CreditCard response = creditCardService.findById(creditCard.getId());

            assertNotNull(response);
            assertEquals(CreditCardMapper.toResponse(creditCard), CreditCardMapper.toResponse(response));
            assertEquals(creditCard.getId(), cardIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).findById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }

        @Test
        void testCaseNotFound() {
            when(creditCardRepository.findById(creditCard.getId())).thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> creditCardService.findById(creditCard.getId()));

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
            doNothing().when(creditCardRepository).deleteById(cardIdCaptor.capture());

            creditCardService.deleteById(creditCard.getId());

            assertEquals(creditCard.getId(), cardIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).deleteById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }

        @Test
        void testCaseNotFoundIdDelete() {
            doThrow(EmptyResultDataAccessException.class).when(creditCardRepository).deleteById(cardIdCaptor.capture());

            assertThrows(EntityNotFoundException.class, () -> creditCardService.deleteById(creditCard.getId()));
            assertEquals(creditCard.getId(), cardIdCaptor.getValue());

            verify(creditCardRepository, atLeastOnce()).deleteById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }

        @Test
        void testCaseNullId() {
            assertThrows(NullInsertValueException.class, () -> creditCardService.deleteById(null));

            verify(creditCardRepository, never()).deleteById(any());
            verifyNoMoreInteractions(creditCardRepository);
        }
    }
}
