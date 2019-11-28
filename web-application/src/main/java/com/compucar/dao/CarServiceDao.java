package com.compucar.dao;

import com.compucar.model.CarService;
import com.compucar.model.Client;
import com.compucar.model.Mechanic;
import com.compucar.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarServiceDao extends JpaRepository<CarService, Long> {
    Optional<CarService> findById(Long id);

    Optional<CarService> findByCode(String code);

    List<CarService> findByDateBetween(LocalDateTime from, LocalDateTime to);

    List<CarService> findByReaderCode(String readerCode);

    List<CarService> findByReaderCodeAndDateBetween(String readerCode, LocalDateTime from, LocalDateTime to);

    @Query("Select s from CarService s where MONTH(s.date) = :month")
    List<CarService> findByMonth(@Param("month") Integer month);

    boolean existsByCode(String code);

    boolean existsByClientAndDateBetween(Client client, LocalDateTime from, LocalDateTime to);

    boolean existsByMechanicAndDateBetweenAndWorkshopNot(Mechanic mechanic, LocalDateTime from, LocalDateTime to, Workshop workshop);

    Integer countByMechanicAndDateBetween(Mechanic mechanic, LocalDateTime from, LocalDateTime to);

    Integer countByClientAndDateBetween(Client client, LocalDateTime from, LocalDateTime to);
}