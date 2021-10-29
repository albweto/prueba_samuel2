package com.example.pruebabackendbanco.services;

import com.example.pruebabackendbanco.entities.Solicitudes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SolicitudeServices {

     List<Solicitudes> listaSolicitudes();
     void guardar(Solicitudes solicitudes);
     void  eliminar(Long id);
     Solicitudes buscarSolicitud(Long id);
     Boolean existSolicitudId(Long id);
     Optional<Solicitudes> getOne(Long id);
     Solicitudes getCliente(int id_cliente);
     List<Solicitudes> getClienteFEcha(LocalDate fecha_n1, LocalDate  fecha_n2);

}
