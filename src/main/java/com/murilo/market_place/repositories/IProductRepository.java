package com.murilo.market_place.repositories;

import com.murilo.market_place.domains.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProductRepository extends JpaRepository<Product, UUID> {
}
