/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.DTO;

import br.com.dbc.PreSelecao.entity.Candidato;
import br.com.dbc.PreSelecao.entity.Endereco;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jonas.cruz
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoDTO {
    
    private String nomeCompleto;
    
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate nascimento;
    
    private String cpf;
    
    private String telefone;
    
    private String cep;
    
    private String logradouro;
    
    private Long numero;
    
    private String complemento;
    
    private String bairro;
    
    private String cidade;
    
    private String estado;
    
    private String email;
    
    private String password;
    
    private String instituicao;
    
    private String curriculo;
   
}
