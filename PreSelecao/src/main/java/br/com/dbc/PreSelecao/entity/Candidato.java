/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author jaqueline.bonoto
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CANDIDATO")
public class Candidato extends AbstractEntity<Long> implements Serializable {

    @Id
    @NotNull
    @SequenceGenerator(name= "S_CANDIDATO", sequenceName = "S_CANDIDATO", allocationSize=1)
    @GeneratedValue(generator = "S_CANDIDATO", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "NOME", nullable = false)
    private String nomeCompleto;
    
    @NotNull
    @Column(name = "DATA_NASCIMENTO", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    
    @NotNull
    @Column(name = "DATA_INSCRICAO", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataInscricao;
    
    
    @NotNull
    @Size(min = 1, max = 14)
    @Column(name = "CPF", nullable = false)
    private String cpf;
    
    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Status status;
    
    @NotNull
    @Column(name = "CURRICULO", nullable = false)
    private String curriculo;
    
    @NotNull
    @Size(min = 8 , max = 13)
    @Column(name = "TELEFONE", nullable = false)
    private String telefone;
    
    @NotNull
    @Column(name = "INSTITUICAO", nullable = false)
    private String instituicao;
        
    @NotNull
    @JoinColumn(name = "ID_USER", unique = true)
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    
    @NotNull
    @JoinColumn(name = "ID_ENDERECO", unique = true)
    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;
     
}
