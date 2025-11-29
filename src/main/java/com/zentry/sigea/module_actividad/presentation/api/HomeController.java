package com.zentry.sigea.module_actividad.presentation.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "*")
public class HomeController {
    
    @GetMapping(value = "/")
    @Operation(
        summary = "Api de home" , 
        tags = {"Home"}
    )
    public String Index(){
        return "el backend esta corriendo-se";
    }
}
