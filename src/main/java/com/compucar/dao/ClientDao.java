package com.compucar.dao;

import com.compucar.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientDao extends JpaRepository<Client, Long> {
    Client findByCode(Integer code);

    Optional<Client> findByEmail(String email);
}
