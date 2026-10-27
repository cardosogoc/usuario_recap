package com.javanauta.usuario_recap.controller;

import com.javanauta.usuario_recap.business.UsuarioService;
import com.javanauta.usuario_recap.business.dto.EnderecoDTO;
import com.javanauta.usuario_recap.business.dto.TelefoneDTO;
import com.javanauta.usuario_recap.business.dto.in.UsuarioRequestDTO;
import com.javanauta.usuario_recap.business.dto.ViaCepService;
import com.javanauta.usuario_recap.business.dto.out.UsuarioResponseDTO;
import com.javanauta.usuario_recap.infrastructure.clients.ViaCepDTO;
import com.javanauta.usuario_recap.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Cadastro de usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {

    private final UsuarioService service;
    private final ViaCepService viaCepService;

    @PostMapping("/login")
    @Operation(
            summary = "Realiza o login",
            description = "Autentica o usuário e retorna o token de acesso")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "400", description = "Dados de login inválidos ou ausentes")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<String> login(@RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(service.autenticarUsuario(dto));
    }

    @PostMapping
    @Operation(summary = "Salvar Usuários", description = "Cria um novo usuário")
    @ApiResponse(responseCode = "200", description = "usuário salvo com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<UsuarioResponseDTO> salvaUsuario(@RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(service.salvaUsuario(dto));
    }


    //Pesquisando Usuario por meio de parâmetro
    @GetMapping
    @Operation(summary = "Busca usuário por email", description = "Busca usuário ja cadastrado por email")
    @ApiResponse(responseCode = "200", description = "usuário encontrado com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    @ApiResponse(responseCode = "401", description = "Busca não Autorizada")
    public ResponseEntity<UsuarioResponseDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(service.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Deleta Usuário por email de Usuário", description = "Deleta Usuário cadastradas por email de Usuário")
    @ApiResponse(responseCode = "200", description = "Usuário deletado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "401", description = "Usuário não Autorizado")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email) {
        service.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(summary = "Altera dados do usuário ", description = "Altera dados de usuários cadastradas ")
    @ApiResponse(responseCode = "200", description = "Dados de usuário alterados")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "401", description = "Usuário não Autorizado")
    public ResponseEntity<UsuarioResponseDTO> atualizaUsuario(
            @RequestBody UsuarioRequestDTO dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.atualizaDadosUsuario(token, dto));
    }

    @PutMapping("/endereco")
    @Operation(summary = "Altera dados do(s) endereço(s) do usuário ", description = "Altera dados de endereço de usuários cadastradas ")
    @ApiResponse(responseCode = "200", description = "Endereço(s)  de usuário alterado(s) ")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "401", description = "Usuário não Autorizado")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(
            @RequestBody EnderecoDTO dto,
            @RequestParam("id") Long id) {
        return ResponseEntity.ok(service.atualizaEndereco(id, dto));
    }

    @PutMapping("/telefone")
    @Operation(summary = "Altera dados do(s) telefone(s) do usuário ", description = "Altera dados de endereço de usuários cadastradas ")
    @ApiResponse(responseCode = "200", description = "telefone(s)  de usuário alterado(s)")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "401", description = "Usuário não Autorizado")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(
            @RequestBody TelefoneDTO dto,
            @RequestParam("id") Long id) {
        return ResponseEntity.ok(service.atualizaTelefone(id, dto));
    }

    @PostMapping("/endereco")
    @Operation(summary = "Cadastra endereço(s) de Usuários", description = "Cria um novo endereço de usuário")
    @ApiResponse(responseCode = "200", description = "endereço salvo com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<EnderecoDTO> cadastroEndereco(
            @RequestBody EnderecoDTO dto,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(service.cadastroEndereco(token, dto));
    }

    @PostMapping("/telefone")
    @Operation(summary = "Cadastra telefone(s) de Usuários", description = "Cria um novo telefone de usuário")
    @ApiResponse(responseCode = "200", description = "telefone salvo com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<TelefoneDTO> cadastroTelefone(
            @RequestBody TelefoneDTO dto,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(service.cadastroTelefone(token, dto));
    }

    @GetMapping("/endereco/{cep}")
    public ResponseEntity<ViaCepDTO> buscarDadosCep(@PathVariable("cep") String cep) {
        return ResponseEntity.ok(viaCepService.buscarDadosEndereco(cep));
    }

    @DeleteMapping("/endereco/{id}")
    @Operation(summary = "Deleta endereço por meio do id", description = "Deleta um endereço cadastrado pelo id do mesmo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou ausente"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor")
    })
    public ResponseEntity<Void> deletaEnderecoPorId(@PathVariable Long id) {
        service.deletaEndereco(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/telefone/{id}")
    @Operation(summary = "Deleta telefone por meio do id", description = "Deleta um telefone cadastrado pelo id do mesmo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Telefone deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Telefone não encontrado"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou ausente"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor")
    })
    public ResponseEntity<Void> deletaTelefonePorId(@PathVariable Long id) {
        service.deletaTelefone(id);
        return ResponseEntity.noContent().build();
    }
}
