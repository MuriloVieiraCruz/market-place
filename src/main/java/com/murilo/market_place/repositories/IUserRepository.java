package com.murilo.market_place.repositories;

import com.murilo.market_place.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
}
