package com.murilo.market_place.repositories;

import com.murilo.market_place.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByCpf(String cpf);
}
