package com.example.pruebabackendbanco.dao;

import com.example.pruebabackendbanco.entities.Solicitudes;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface SolicitudDao  extends JpaRepository<Solicitudes, Long> {

   @Query(value = "SELECT s FROM Solicitudes s WHERE s.id_cliente = :id_cliente" )
   public Solicitudes findSolicitudesById_cliente(@Param("id_cliente")int id_cliente);
   List<Solicitudes> findSolicitudesByFechaIngresoIsBetween(@Param("startDate") LocalDate startDate, @Param("endDate")LocalDate endDate);
}
