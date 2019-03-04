/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jaqueline.bonoto
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ENDERECO")
public class Endereco extends AbstractEntity<Long> implements Serializable {

    @Id
    @NotNull
    @SequenceGenerator(name= "S_ENDERECO", sequenceName = "S_ENDERECO", allocationSize=1)
    @GeneratedValue(generator = "S_ENDERECO", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "LOGRADOURO", nullable = false)
    private String logradouro;
 
    
    @NotNull
    @Column(name = "NUMERO", nullable = false)
    private Long numero;
    
    
    @Size(max = 100)
    @Column(name = "COMPLEMENTO", nullable = true)
    private String complemento;
    
    
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "CEP", nullable = false)
    private String cep;
    
    
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "BAIRRO", nullable = false)
    private String bairro;
    
   
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CIDADE", nullable = false)
    private String cidade;
    
   
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ESTADO", nullable = false)
    private String estado;
        
}
