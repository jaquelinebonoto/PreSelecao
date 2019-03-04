/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.rest;

import br.com.dbc.PreSelecao.DTO.CandidatoDTO;
import br.com.dbc.PreSelecao.DTO.CandidatoFeedBackDTO;
import br.com.dbc.PreSelecao.entity.Candidato;
import br.com.dbc.PreSelecao.entity.User;
import br.com.dbc.PreSelecao.service.AppUserDetailsService;
import br.com.dbc.PreSelecao.service.CandidatoService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jonas.cruz
 */
@RestController
@RequestMapping("/api/candidato")
public class CandidatoRestController extends AbstractRestController<Candidato, CandidatoService>{

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private AppUserDetailsService userService;

    
    @Override
    protected CandidatoService getService() {
        return candidatoService;
    }
    
    @PostMapping("/dto") //cadastrar candidato
    public ResponseEntity<?> salvaCandidato (@RequestBody CandidatoDTO dto) throws Exception{
        return ResponseEntity.ok(candidatoService.salvaCandidatoComEndereco(dto));
    }
    
    @PatchMapping("/{id}") //aprovacao ou rejeicao de candidato
    public ResponseEntity<?> feedBackCandidato (@RequestBody CandidatoFeedBackDTO dto) throws Exception{
        return ResponseEntity.ok(candidatoService.feedBackCandidato(dto));
    } 
        
   @GetMapping("/cpf/{cpf}") //front busca no banco se o cpf já está cadastrado
   public ResponseEntity<?> verificaCPFExistente(@PathVariable String cpf){
       return ResponseEntity.ok(candidatoService.findByCpf(cpf));
   }
   
    @GetMapping("/email/{email}") //front busca no banco se email já é cadastrado
   public ResponseEntity<?> verificaEmailExistente(@PathVariable String email){
       return ResponseEntity.ok(userService.findByEmail(email));
   }
   
   @PreAuthorize("hasAuthority('ADMIN_USER')")
   @RequestMapping(value="/search", method = RequestMethod.GET)
   public ResponseEntity<?> findDateBetween(
                Pageable pageable,
                @RequestParam(value = "start", required = true) 
                            @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
                @RequestParam(value = "end", required = true) 
                            @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end){
       return ResponseEntity.ok(
                        candidatoService.findByDataInscricaoBetween(
                                                        pageable, 
                                                        start.atTime(0, 0) , 
                                                        end.atTime(23, 59, 59)  
                                                    )                   
       );
   }

}
