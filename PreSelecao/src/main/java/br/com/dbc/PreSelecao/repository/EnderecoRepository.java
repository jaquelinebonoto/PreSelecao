/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.repository;

import br.com.dbc.PreSelecao.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jonas.cruz
 */
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    
}
