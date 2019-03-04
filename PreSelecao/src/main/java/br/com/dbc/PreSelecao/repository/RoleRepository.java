/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.repository;

import br.com.dbc.PreSelecao.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Camila e Henrique
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Role findByRoleName(String roleName);
    
}
