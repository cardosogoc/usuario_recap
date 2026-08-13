package com.javanauta.usuario_recap.business;

import com.javanauta.usuario_recap.business.converter.UsuarioConverter;
import com.javanauta.usuario_recap.business.dto.EnderecoDTO;
import com.javanauta.usuario_recap.business.dto.TelefoneDTO;
import com.javanauta.usuario_recap.business.dto.UsuarioDTO;
import com.javanauta.usuario_recap.infrastructure.entity.Endereco;
import com.javanauta.usuario_recap.infrastructure.entity.Telefone;
import com.javanauta.usuario_recap.infrastructure.entity.Usuario;
import com.javanauta.usuario_recap.infrastructure.exceptions.ConflictException;
import com.javanauta.usuario_recap.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario_recap.infrastructure.exceptions.UnauthorizedException;
import com.javanauta.usuario_recap.infrastructure.repository.EnderecoRepository;
import com.javanauta.usuario_recap.infrastructure.repository.TelefoneRepository;
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

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
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
        return converter.paraUsuarioDTO(usuarioRepository.save(usuario));
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
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        try{
            return converter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email).orElseThrow(
                            () -> new ResourceNotFoundException("Email não encontrado" + email)
                    )
            );
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado", e.getCause());
        }
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        Usuario entity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));

        Usuario usuario = converter.atualizarUsuario(dto, entity);

        return converter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO dto){
        Endereco entity = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado" + idEndereco));

        Endereco endereco = converter.atualizarEndereco(dto, entity);
        return converter.paraEnderecoDTO(enderecoRepository.save(endereco));

    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto){
        Telefone entity = telefoneRepository.findById(idTelefone)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado" + idTelefone));

        Telefone telefone = converter.atualizarTelefone(dto, entity);
        return converter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

}
