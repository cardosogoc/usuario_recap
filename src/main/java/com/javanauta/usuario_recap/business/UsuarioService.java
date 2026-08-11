package com.javanauta.usuario_recap.business;

import com.javanauta.usuario_recap.business.converter.UsuarioConverter;
import com.javanauta.usuario_recap.business.dto.UsuarioDTO;
import com.javanauta.usuario_recap.infrastructure.entity.Usuario;
import com.javanauta.usuario_recap.infrastructure.exceptions.ConflictException;
import com.javanauta.usuario_recap.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioConverter converter;
    private final PasswordEncoder passwordEncoder;

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
            throw new ConflictException("Email já cadastrado" + e.getCause());
        }
    }

    private boolean verificaEmailExistente(String email) {
        return repository.existsByEmail(email);
    }
}
