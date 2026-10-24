package com.javanauta.usuario_recap.business.dto.out;

import com.javanauta.usuario_recap.business.dto.EnderecoDTO;
import com.javanauta.usuario_recap.business.dto.TelefoneDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private String nome;
    private String email;
    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;

}
