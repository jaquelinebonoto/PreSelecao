/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.service;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author henrique.laporta
 */
@Transactional(readOnly = true)
public abstract class AbstractCrudService<ENTITY> {
    
    protected abstract JpaRepository<ENTITY, Long> getRepository();
    
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ENTITY save(@NotNull @Valid ENTITY entity){
        return getRepository().save(entity);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void delete(Long id) {
        getRepository().deleteById(id);
    }
    
    public Optional<ENTITY> findById(Long id){
        return getRepository().findById(id);
    }
    
    public Page<ENTITY> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }
}
