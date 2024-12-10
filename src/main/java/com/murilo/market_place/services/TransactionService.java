package com.murilo.market_place.services;

import com.murilo.market_place.domains.ItemTransaction;
import com.murilo.market_place.domains.Product;
import com.murilo.market_place.domains.Transaction;
import com.murilo.market_place.domains.User;
import com.murilo.market_place.domains.enums.PaymentMethod;
import com.murilo.market_place.dtos.productcart.ProductCartDTO;
import com.murilo.market_place.dtos.transaction.TransactionRequestDTO;
import com.murilo.market_place.exception.EntityNotFoundException;
import com.murilo.market_place.exception.NullInsertValueException;
import com.murilo.market_place.mapper.ItemTransactionMapper;
import com.murilo.market_place.repositories.ITransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final ITransactionRepository transactionRepository;
    private final UserService userService;
    private final ProductService productService;

    public Transaction create(TransactionRequestDTO requestDTO) {
        User user = userService.findById(requestDTO.getUserId());

        List<ItemTransaction> itemsTransaction = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ProductCartDTO productCart : requestDTO.getPurchaseProducts()) {
            productService.existsProduct(productCart.getProductId());
            itemsTransaction.add(ItemTransactionMapper.toItemTransaction(productCart));
            total = total.add(productCart.getPrice().multiply(BigDecimal.valueOf(productCart.getQuantity())));
        }

        //TODO inserir futuramente o tipo de pagamento, dependendo do tipo realizar validações com o padrão strategy

        Transaction transaction = Transaction.builder()
                .user(user)
                .paymentMethod(PaymentMethod.fromCode(requestDTO.getPaymentMethod()))
                .moment(LocalDateTime.now())
                .items(itemsTransaction)
                .totalPayment(total)
                .build();

        for (ItemTransaction item : itemsTransaction) {
            item.setTransaction(transaction);
        }

        return transactionRepository.save(transaction);
    }

    public Page<Transaction> findAll(UUID userId, Pageable pageable) {
        userService.existsUser(userId);
        return transactionRepository.findByUserId(userId, pageable);
    }

    public Transaction findById(UUID transactionId) {
        return this.findTransaction(transactionId);
    }

    private Transaction findTransaction(UUID transactionId) {
        if (Objects.isNull(transactionId)) {
            throw new NullInsertValueException("ID is required for transaction search");
        }

        return transactionRepository.findWithItemsById(transactionId).orElseThrow(() -> new EntityNotFoundException(Product.class));
    }
}
