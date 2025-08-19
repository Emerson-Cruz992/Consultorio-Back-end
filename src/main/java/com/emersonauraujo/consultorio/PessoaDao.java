package com.emersonauraujo.consultorio;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaDao extends JpaRepository<Pessoa, UUID> {
    boolean existsByCpf(String cpf);

}
