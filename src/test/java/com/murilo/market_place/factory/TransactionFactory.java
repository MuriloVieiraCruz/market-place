package com.murilo.market_place.factory;

import com.murilo.market_place.domains.ItemTransaction;
import com.murilo.market_place.domains.Product;
import com.murilo.market_place.domains.Transaction;
import com.murilo.market_place.domains.enums.PaymentMethod;
import com.murilo.market_place.dtos.productcart.ProductCartDTO;
import com.murilo.market_place.dtos.transaction.TransactionRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TransactionFactory {

    public static Transaction getTransactionInstance() {
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .totalPayment(BigDecimal.valueOf(185.7))
                .moment(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .user(UserFactory.getUserInstance())
                .build();

        transaction.setItems(getItemTransactionInstance(transaction));
        return transaction;
    }

    public static TransactionRequestDTO getTransactionRequestInstance(UUID userId, Product product) {
        return new TransactionRequestDTO(
                userId,
                PaymentMethod.CREDIT_CARD.getCode(),
                getProductsCartInstance(product)
        );
    }

    public static TransactionRequestDTO getTransactionRequestInstance() {
        return new TransactionRequestDTO(
                UUID.randomUUID(),
                PaymentMethod.CREDIT_CARD.getCode(),
                getProductsCartInstance()
        );
    }

    private static List<ProductCartDTO> getProductsCartInstance(Product product) {
        return List.of(new ProductCartDTO(product.getId(), product.getAlbum(), 3, BigDecimal.valueOf(61.90)));
    }

    private static List<ProductCartDTO> getProductsCartInstance() {
        return List.of(new ProductCartDTO(UUID.randomUUID(), "Dask Side of The Moon", 3, BigDecimal.valueOf(61.90)));
    }

    private static List<ItemTransaction> getItemTransactionInstance(Transaction transaction) {
        ItemTransaction item = ItemTransaction.builder()
                .productName("Dask Side of The Moon")
                .quantity(3)
                .price(BigDecimal.valueOf(61.90))
                .transaction(transaction)
                .product(ProductFactory.getProductInstance())
                .build();

        return List.of(item);
    }

}
