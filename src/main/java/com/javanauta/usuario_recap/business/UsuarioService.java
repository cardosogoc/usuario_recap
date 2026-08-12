package com.javanauta.usuario_recap.business;

import com.javanauta.usuario_recap.business.converter.UsuarioConverter;
import com.javanauta.usuario_recap.business.dto.UsuarioDTO;
import com.javanauta.usuario_recap.infrastructure.entity.Usuario;
import com.javanauta.usuario_recap.infrastructure.exceptions.ConflictException;
import com.javanauta.usuario_recap.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario_recap.infrastructure.exceptions.UnauthorizedException;
import com.javanauta.usuario_recap.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuario_recap.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioConverter converter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    // **LOGIN**** //

    public String autenticarUsuario(UsuarioDTO dto){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
            return "Bearer " + jwtUtil.generateToken(authentication.getName());
        } catch (BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException e){
            throw new UnauthorizedException("Usuario ou Senha inválidos", e);
        }
    }

    public UsuarioDTO salvaUsuario(UsuarioDTO dto){
        emailExiste(dto.getEmail());
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        Usuario usuario = converter.paraUsuarioEntity(dto);
        return converter.paraUsuarioDTO(repository.save(usuario));
    }

    public void emailExiste(String email){
        try{
            boolean existe = verificaEmailExistente(email);
            if (existe){
                throw new ConflictException("Email já cadastrado" + email);
            }
        } catch (ConflictException e){
            throw new ConflictException("Email já cadastrado" + e);
        }
    }

    private boolean verificaEmailExistente(String email) {
        return repository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        try{
            return converter.paraUsuarioDTO(
                    repository.findByEmail(email).orElseThrow(
                            () -> new ResourceNotFoundException("Email não encontrado" + email)
                    )
            );
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado", e.getCause());
        }
    }

    public void deletaUsuarioPorEmail(String email){
        repository.deleteByEmail(email);
    }

}
