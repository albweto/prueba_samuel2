package com.example.pruebabackendbanco.controller;

import com.example.pruebabackendbanco.dao.SolicitudDao;
import com.example.pruebabackendbanco.entities.Solicitudes;
import com.example.pruebabackendbanco.services.SoliccitudesServiceImpl;
import com.example.pruebabackendbanco.util.Mensaje;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/solicitudes")
@Slf4j
public class SolicitudController {


    @Autowired
    SoliccitudesServiceImpl service;

    @Autowired
    SolicitudDao solicitudDao;

   @GetMapping("/all")
    public ResponseEntity<List<Solicitudes>> listarTodos(){
    List<Solicitudes> lista = service.listaSolicitudes();
     return new ResponseEntity(lista, HttpStatus.OK);
    }

    @GetMapping("/consultar/{id}")
    public ResponseEntity<?> consultarid(@PathVariable("id")Long id){
       var  solicitud = service.buscarSolicitud(id);
       return new  ResponseEntity(solicitud, HttpStatus.OK);
    }

    @GetMapping("/consultarf/{fecha_n1}/{fecha_n2}")
    public ResponseEntity<?> consultarfechas(@PathVariable("fecha_n1")String  fecha_n1,@PathVariable("fecha_n2")String  fecha_n2 ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate fecha_inicio = LocalDate.parse(fecha_n1, formatter);
        LocalDate fecha_final = LocalDate.parse(fecha_n2, formatter);
        var  solicitud = service.getClienteFEcha(fecha_inicio,fecha_final);
        var saldoTotal = 0D;
        saldoTotal = solicitud.stream().mapToDouble(Solicitudes::getMonto).average().orElse(Double.NaN);
        return new  ResponseEntity(saldoTotal, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Solicitudes solicitudes) throws Exception  {
        //log.info(String.valueOf(solicitudes));
        LocalDate fecha = LocalDate.now();
        boolean isBefore = fecha.isAfter(solicitudes.getFechaIngreso());
        int id_cliente = 0;
        try{
            var solictud_cliente = service.getCliente(solicitudes.getId_cliente());
            id_cliente  = solictud_cliente.getId_cliente();
        }catch (Exception e){
            log.error("ocurrio un error en "+e.getMessage());
        }




        if(solicitudes.getMonto() < 1000000){
            return  new ResponseEntity(new Mensaje("el monto ni puede ser inferior a 1 millon"), HttpStatus.NOT_ACCEPTABLE);
        }
       // if(LocalDateTime.now().isAfter(ChronoLocalDateTime.from(fecha))){
            //return  new ResponseEntity(new Mensaje("la fecha no puede ser inferior a la actual"), HttpStatus.NOT_ACCEPTABLE);
       // }
        if (id_cliente >0 ){
            return  new ResponseEntity(new Mensaje("este usuario ya creo uan solicitud"), HttpStatus.NOT_ACCEPTABLE);
        }

        if (isBefore){
            return  new ResponseEntity(new Mensaje("no se puede aceptar una fecha anterior"), HttpStatus.NOT_ACCEPTABLE);
        }




        service.guardar(solicitudes);
        return new ResponseEntity(new Mensaje("Se creo con exito la solicitud"), HttpStatus.OK);

    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Solicitudes solicitudes){
       log.info("ingreso");
        if(!service.existSolicitudId(id)){
            return new ResponseEntity(new Mensaje("no existe solicitud"), HttpStatus.NOT_FOUND);
        }
        if(solicitudes.getMonto() < 1000000){
            return  new ResponseEntity(new Mensaje("el monto ni puede ser inferior a 1 millon"), HttpStatus.NOT_ACCEPTABLE);
        }
        Solicitudes s1 = service.getOne(id).get();
        s1.setEstado(solicitudes.getEstado());
        s1.setMonto(solicitudes.getMonto());
        service.guardar(s1);

        return new ResponseEntity(new Mensaje("Se Actualizo la solicitud"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")Long id){
        if(!service.existSolicitudId(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        service.eliminar(id);
        return new ResponseEntity(new Mensaje("tarea eliminada"), HttpStatus.OK);
    }

}
