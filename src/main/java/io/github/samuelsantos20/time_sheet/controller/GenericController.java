package io.github.samuelsantos20.time_sheet.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * Vai funcionar como uma classe generica para a criação de URL
 *
 * Para utilizar um metodo com corpo em uma interface é necessario utilizar o default
 * */

public interface GenericController {

            default URI gerarHaderLoccation(UUID id){

                return  ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

            }

}
