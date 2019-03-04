package br.com.dbc.PreSelecao.service;

import br.com.dbc.PreSelecao.DTO.CandidatoDTO;
import br.com.dbc.PreSelecao.DTO.CandidatoFeedBackDTO;
import br.com.dbc.PreSelecao.entity.Candidato;
import br.com.dbc.PreSelecao.entity.Endereco;
import br.com.dbc.PreSelecao.entity.Status;
import br.com.dbc.PreSelecao.entity.User;
import br.com.dbc.PreSelecao.repository.CandidatoRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CandidatoService extends AbstractCrudService<Candidato>{

    @Autowired
    private CandidatoRepository candidatoRepository;
    
    @Autowired
    private EnderecoService enderecoService;
    
    @Autowired
    private AppUserDetailsService userService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private MailService mailService;
    
    @Override
    protected CandidatoRepository getRepository() {
        return candidatoRepository;
    }
    
    //método chamado pelo rest para salvar o candidato
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Candidato salvaCandidatoComEndereco(CandidatoDTO dto) throws Exception{
        Candidato candidato = convertToCandidato(dto);        
        Endereco endereco = convertToEndereco(dto);
        User user = convertToUser(dto);
        
        try{
            user = userService.createNewUser(user);
            endereco = enderecoService.save(endereco);
            candidato.setEndereco(endereco);
            candidato.setUser(user);
            candidato.setCurriculo(
                            fileService.uploadFile(dto.getCurriculo(), dto.getNomeCompleto())
                            );
            candidato = salvaCandidato(candidato);
        }        
        catch (IOException e){
            System.out.println(e);
            throw new Exception("Erro ao tentar salvar o arquivo de curriculo!");
        }
        catch (Exception e){
            System.out.println(e);
            throw new Exception(e);
        }
        return candidato;        
    }
    
    //método auxiliar para verificar se candidato email está validade em usuário
    public Candidato salvaCandidato(Candidato candidato) throws Exception{
        if(getRepository().findByCpf(candidato.getCpf()).isPresent())
            throw new Exception("Erro ao salvar o candidato!");
        candidato = getRepository().save(candidato);
        return candidato;
    }
    
    
    //método para transformar o candidato recebido na tela em um candidato no banco
    public Candidato convertToCandidato(CandidatoDTO dto){
        Candidato candidato = Candidato.builder()
                .nomeCompleto(dto.getNomeCompleto())
                .dataInscricao(LocalDateTime.now())
                .dataNascimento(dto.getNascimento())
                .cpf(dto.getCpf())
                .telefone(dto.getTelefone())
                .status(Status.PENDENTE)
                .instituicao(dto.getInstituicao())
                .build();
        return candidato;
    }
    
    //método auxiliar para criar endereco através de candidato informado no front end
    //os dados de endereço informados no front end são preenchidos pelo service dos correios
    public Endereco convertToEndereco(CandidatoDTO dto){
        Endereco endereco  = Endereco.builder()
                .logradouro(dto.getLogradouro())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .cep(dto.getCep())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .build();
        return endereco;
    }
    
    //método auxiliar para criar usuário através de candidato informado no front end
    public User convertToUser(CandidatoDTO dto){
        User user  = User.builder()
                .firstName(dto.getNomeCompleto())
                .password(dto.getPassword())
                .username(dto.getEmail())
                .email(dto.getEmail())
                .build();
        return user;
    }

    //método a ser acionado ao aprovar ou rejeitar um candidato
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Candidato feedBackCandidato (CandidatoFeedBackDTO dto) throws Exception{
        Candidato candidato = getRepository().findById(dto.getId()).get();
        try{
        if (dto.getStatus() == Status.PENDENTE);
        if (dto.getStatus() == Status.REJEITADA){
            String message = ("<h3>Agradecemos sua participação no processo seletivo. \n Infelizmente você não serguirá no processo.</h3>");
            mailService.sendMail(
                    "emailparatestarfuncionalidades@gmail.com", 
                    candidato.getUser().getEmail(), 
                    "Resultado do Processo Seletivo do Programa Vem Ser", 
                    message);
                candidato.setStatus(dto.getStatus());
                candidato = getRepository().save(candidato);
        };
        if (dto.getStatus() == Status.CONVITE_ENVIADO){
            
            String message = ("<h2>Agradecemos sua participação no processo seletivo. \n Informamos que você foi selecionado para a próxima etapa. \n Clique no botão para confirmar presença.</h2>" 
                            + "<h3> Por favor confirme sua participação na próxima etapa do processo no botão abaixo.\n</h3>"
                            + "<a href=\"http://localhost:8080/#/confirma/"+dto.getId()+"\"> <button style=\"background-color:#0C98DA; cursor: pointer; height:50px; width: 150px; border-radius: 10px;\"> Confirmar Participação </button></a>" );
           
            mailService.sendMail(
                    "emailparatestarfuncionalidades@gmail.com", 
                    candidato.getUser().getEmail(), 
                    "Resultado do Processo Seletivo do Programa Vem Ser", 
                    message
                    );
            candidato.setStatus(dto.getStatus());
            candidato = getRepository().save(candidato);
        };
        if (dto.getStatus() == Status.PRESENCA_CONFIRMADA){
            candidato.setStatus(dto.getStatus());
            candidato = getRepository().save(candidato);
        };
        } catch (Exception e){
            System.out.println(e);
            throw new Exception(e);
        } finally {
            return candidato;
        }      
    }    
    
    
    public Candidato findByCpf(String cpf){
        Optional<Candidato> candidato = candidatoRepository.findByCpf(cpf);
        return candidato.isPresent() ? candidato.get() : Candidato.builder().build();
    }
    
    public User findByEmail(String email){
        return userService.findByEmail(email);
    }

    public Page<Candidato> findByDataInscricaoBetween (Pageable pageable, LocalDateTime ini, LocalDateTime fim){
        return candidatoRepository.findByDataInscricaoBetween(pageable, ini, fim);
    }
    
}
