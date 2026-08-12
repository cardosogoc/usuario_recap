package com.javanauta.usuario_recap.controller;

import com.javanauta.usuario_recap.business.UsuarioService;
import com.javanauta.usuario_recap.business.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioDTO dto){
        return ResponseEntity.ok(service.autenticarUsuario(dto));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO dto){
        return ResponseEntity.ok(service.salvaUsuario(dto));
    }

    //Pesquisando o usuario por meio do body
    @GetMapping("/pesquisa")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@RequestBody EmailRequest request) {
        return ResponseEntity.ok(service.buscarUsuarioPorEmail(request.email()));
    }

    public record EmailRequest(String email) {}

    //Pesquisando Usuario por meio de parâmetro
    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(service.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email){
        service.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

}
