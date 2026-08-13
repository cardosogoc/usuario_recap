package com.javanauta.usuario_recap.infrastructure.repository;

import com.javanauta.usuario_recap.infrastructure.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
