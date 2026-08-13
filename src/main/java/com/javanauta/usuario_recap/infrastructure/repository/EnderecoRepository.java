package com.javanauta.usuario_recap.infrastructure.repository;

import com.javanauta.usuario_recap.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
