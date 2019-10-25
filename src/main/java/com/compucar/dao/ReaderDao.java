package com.compucar.dao;

import com.compucar.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReaderDao extends JpaRepository<Reader, Long> {
    Optional<Reader> findById(Long id);

    Optional<Reader> findByCode(String code);

    List<Reader> findByWorkshopId(Long workshopId);

    List<Reader> findAll();

    boolean existsByCode(String code);

    @Query("Select r from Reader r where (r.batteryLife - r.actualTimeUse) <= :delta")
    List<Reader> findByAvailableBatteryLessThan(@Param("delta") Integer delta);
}
