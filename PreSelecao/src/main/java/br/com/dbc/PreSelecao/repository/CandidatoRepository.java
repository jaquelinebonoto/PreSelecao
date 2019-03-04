/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.repository;

import br.com.dbc.PreSelecao.entity.Candidato;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 *
 * @author jonas.cruz
 */
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
 
    public Optional<Candidato> findByCpf(String cpf);
    
    public Page<Candidato> findByDataInscricaoBetween(Pageable pageable, LocalDateTime ini, LocalDateTime fim);
}
