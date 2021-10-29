package com.example.pruebabackendbanco.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Table(name = "t01_solicitudes")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Solicitudes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private  Long id;
    @Column(name = "estado")
    private int estado;
    @Column(name = "fecha_ingreso")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaIngreso;
    @Column(name = "monto")
    private Double monto;
    @Column(name = "id_cliente", unique = true)
    private int id_cliente;
}