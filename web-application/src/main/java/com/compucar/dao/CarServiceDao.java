package com.compucar.dao;

import com.compucar.model.CarService;
import com.compucar.model.Client;
import com.compucar.model.Mechanic;
import com.compucar.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CarServiceDao extends JpaRepository<CarService, Long> {
    Optional<CarService> findById(Long id);

    Optional<CarService> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByClientAndDateBetween(Client client, LocalDateTime from, LocalDateTime to);

    boolean existsByMechanicAndDateBetweenAndWorkshopNot(Mechanic mechanic, LocalDateTime from, LocalDateTime to, Workshop workshop);

    Integer countByMechanicAndDateBetween(Mechanic mechanic, LocalDateTime from, LocalDateTime to);

    Integer countByClientAndDateBetween(Client client, LocalDateTime from, LocalDateTime to);
}