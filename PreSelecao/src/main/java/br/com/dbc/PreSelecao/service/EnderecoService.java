/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.service;

import br.com.dbc.PreSelecao.entity.Endereco;
import br.com.dbc.PreSelecao.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jonas.cruz
 */
@Service
@Transactional(readOnly = true)
public class EnderecoService  extends AbstractCrudService<Endereco>{
   
    @Autowired
    private EnderecoRepository enderecoRepository;

    @Override
    protected JpaRepository<Endereco, Long> getRepository() {
        return enderecoRepository;
    }
    
}
