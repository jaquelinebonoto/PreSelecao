/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dbc.PreSelecao.rest;

import br.com.dbc.PreSelecao.DTO.CandidatoDTO;
import br.com.dbc.PreSelecao.DTO.CandidatoFeedBackDTO;
import br.com.dbc.PreSelecao.PreSelecaoApplicationTests;
import br.com.dbc.PreSelecao.entity.Candidato;
import br.com.dbc.PreSelecao.entity.Endereco;
import br.com.dbc.PreSelecao.entity.Status;
import br.com.dbc.PreSelecao.entity.User;
import br.com.dbc.PreSelecao.repository.CandidatoRepository;
import br.com.dbc.PreSelecao.repository.EnderecoRepository;
import br.com.dbc.PreSelecao.repository.UserRepository;
import br.com.dbc.PreSelecao.service.CandidatoService;
import br.com.dbc.PreSelecao.service.MailService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 *
 * @author jonas.cruz
 */
public class CandidatoRestControllerTest extends PreSelecaoApplicationTests {

    @Override
    protected AbstractRestController getController() {
        return candidatoRestController;
    }
    @Autowired
    private CandidatoRestController candidatoRestController;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private MailService mailService;

    @Before
    public void before() {
        candidatoRepository.deleteAllInBatch();
        enderecoRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        //Mockito.reset(mailService);
    }

    @Test
    public void criarCandidatoTest() throws Exception {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        CandidatoDTO dto = CandidatoDTO.builder()
                .nomeCompleto("candidato")
                .nascimento(LocalDate.now())
                .cpf("000.000.000-00")
                .telefone("000000000")
                .cep("91225002")
                .logradouro("rua")
                .numero(Long.MIN_VALUE)
                .bairro("bairoo")
                .cidade("cidade")
                .estado("estado")
                .email("candidato@dbccompany.com.br")
                .password("senhaWWW")
                .instituicao("instituicao")
                .curriculo("xxxxxx")
                .build();

        Candidato candidato = objectMapper.readValue(
                restMockMvc.perform(MockMvcRequestBuilders.post("/api/candidato/dto")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.nomeCompleto").value(dto.getNomeCompleto()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.dataNascimento").value(dto.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(dto.getCpf()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.PENDENTE.name()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.id").isNumber())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.logradouro").value(dto.getLogradouro()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.numero").value(dto.getNumero()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.bairro").value(dto.getBairro()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.cidade").value(dto.getCidade()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.estado").value(dto.getEstado()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(dto.getEmail()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.user.firstName").value(dto.getNomeCompleto()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.dataInscricao").value(LocalDateTime.now().format(formatador)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.user.email").value(dto.getEmail()))
                        .andReturn().getResponse().getContentAsString(), Candidato.class);

        List<Candidato> candidatos = candidatoRepository.findAll();

        assertEquals(1, candidatos.size());

        assertEquals(candidato.getId(), candidatos.get(0).getId());
        assertEquals(dto.getNomeCompleto(), candidatos.get(0).getNomeCompleto());
        assertEquals(dto.getNascimento(), candidatos.get(0).getDataNascimento());
        assertEquals(dto.getCpf(), candidatos.get(0).getCpf());
        assertEquals(dto.getTelefone(), candidatos.get(0).getTelefone());
        assertEquals(dto.getInstituicao(), candidatos.get(0).getInstituicao());

        List<Endereco> enderecos = enderecoRepository.findAll();

        assertEquals(dto.getLogradouro(), candidatos.get(0).getEndereco().getLogradouro());
        assertEquals(dto.getNumero(), candidatos.get(0).getEndereco().getNumero());
        assertEquals(dto.getBairro(), candidatos.get(0).getEndereco().getBairro());
        assertEquals(dto.getCidade(), candidatos.get(0).getEndereco().getCidade());
        assertEquals(dto.getEstado(), candidatos.get(0).getEndereco().getEstado());

        assertEquals(dto.getLogradouro(), enderecos.get(0).getLogradouro());
        assertEquals(dto.getNumero(), enderecos.get(0).getNumero());
        assertEquals(dto.getBairro(), enderecos.get(0).getBairro());
        assertEquals(dto.getCidade(), enderecos.get(0).getCidade());
        assertEquals(dto.getEstado(), enderecos.get(0).getEstado());

        assertEquals(dto.getEmail(), candidatos.get(0).getUser().getUsername());
        assertEquals(dto.getNomeCompleto(), candidatos.get(0).getUser().getFirstName());
        assertEquals(dto.getEmail(), candidatos.get(0).getUser().getEmail());
    }

    @Test
    public void feedBackCandidatoAprovadoTest() throws Exception {   
        
        CandidatoDTO dtoParaCriar = CandidatoDTO.builder()
                .nomeCompleto("ocandidato")
                .nascimento(LocalDate.now())
                .cpf("000.000.000-00")
                .telefone("000000000")
                .cep("91225002")
                .logradouro("rua")
                .numero(Long.MIN_VALUE)
                .bairro("bairoo")
                .cidade("cidade")
                .estado("estado")
                .email("jaqueline.pazbonoto@gmail.com")
                .password("senhaWWW")
                .instituicao("instituicao")
                .curriculo("xxxxxx")
                .build();
        Candidato candidato1 = candidatoService.salvaCandidatoComEndereco(dtoParaCriar);
        CandidatoFeedBackDTO dto = CandidatoFeedBackDTO.builder()
                .id(candidato1.getId())
                .status(Status.CONVITE_ENVIADO)
                .build();

        String message = ("<h2>Agradecemos sua participação no processo seletivo. \n Informamos que você foi selecionado para a próxima etapa. \n Clique no botão para confirmar presença.</h2>"
                + "<h3> Por favor confirme sua participação na próxima etapa do processo no botão abaixo.\n</h3>"
                + "<a href=\"http://localhost:8080/#/confirma/" + dto.getId() + "\"> <button style=\"background-color:#0C98DA; cursor: pointer; height:50px; width: 150px; border-radius: 10px;\"> Confirmar Participação </button></a>");

        Mockito.doNothing().when(mailService).sendMail(
                "emailparatestarfuncionalidades@gmail.com",
                dtoParaCriar.getEmail(),
                "Resultado do Processo Seletivo do Programa Vem Ser",
                message
        );
  
        restMockMvc.perform(MockMvcRequestBuilders.patch("/api/candidato/{id}", candidato1.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.CONVITE_ENVIADO.name()));
        List<Candidato> candidatos = candidatoRepository.findAll();
        assertEquals(1, candidatos.size());
        assertEquals(candidato1.getId(), candidatos.get(0).getId());
        assertEquals(dto.getStatus(), candidatos.get(0).getStatus());
        Mockito.verify(mailService, times(1)).sendMail(
                "emailparatestarfuncionalidades@gmail.com",
                dtoParaCriar.getEmail(),
                "Resultado do Processo Seletivo do Programa Vem Ser",
                message
        );
    }

    @Test
    public void feedBackCandidatoReprovadoTest() throws Exception {
        CandidatoDTO dtoParaCriar = CandidatoDTO.builder()
                .nomeCompleto("candidate")
                .nascimento(LocalDate.now())
                .cpf("000.000.000-99")
                .telefone("000000000")
                .cep("91225002")
                .logradouro("rua")
                .numero(Long.MIN_VALUE)
                .bairro("bairoo")
                .cidade("cidade")
                .estado("estado")
                .email("jaqueline.pazbonoto@gmail.com")
                .password("senhaWWW")
                .instituicao("instituicao")
                .curriculo("xxxxxx")
                .build();
        Candidato candidatte = candidatoService.salvaCandidatoComEndereco(dtoParaCriar);

        CandidatoFeedBackDTO dto = CandidatoFeedBackDTO.builder()
                .id(candidatte.getId())
                .status(Status.REJEITADA)
                .build();

        String message = ("<h3>Agradecemos sua participação no processo seletivo. \n Infelizmente você não serguirá no processo.</h3>");
        Mockito.doNothing().when(mailService).sendMail(
                "emailparatestarfuncionalidades@gmail.com",
                dtoParaCriar.getEmail(),
                "Resultado do Processo Seletivo do Programa Vem Ser", 
                message
        );

        restMockMvc.perform(MockMvcRequestBuilders.patch("/api/candidato/{id}", candidatte.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.REJEITADA.name()));
        List<Candidato> candidatos = candidatoRepository.findAll();
        assertEquals(1, candidatos.size());
        assertEquals(candidatte.getId(), candidatos.get(0).getId());
        assertEquals(dto.getStatus(), candidatos.get(0).getStatus());

        Mockito.verify(mailService, times(1)).sendMail(
                "emailparatestarfuncionalidades@gmail.com",
                dtoParaCriar.getEmail(),
                "Resultado do Processo Seletivo do Programa Vem Ser", 
                message
        );
    }

    @Test
    public void retornoConfirmacaoCandidatoTest() throws Exception {
        CandidatoDTO dtoParaCriar = CandidatoDTO.builder()
                .nomeCompleto("ocandidato")
                .nascimento(LocalDate.now())
                .cpf("000.000.000-00")
                .telefone("000000000")
                .cep("91225002")
                .logradouro("rua")
                .numero(Long.MIN_VALUE)
                .bairro("bairoo")
                .cidade("cidade")
                .estado("estado")
                .email("jaqueline.pazbonoto@gmail.com")
                .password("senhaWWW")
                .instituicao("instituicao")
                .curriculo("xxxxxx")
                .build();
        Candidato candidato1 = candidatoService.salvaCandidatoComEndereco(dtoParaCriar);

        CandidatoFeedBackDTO dto = CandidatoFeedBackDTO.builder()
                .id(candidato1.getId())
                .status(Status.PRESENCA_CONFIRMADA)
                .build();

        restMockMvc.perform(MockMvcRequestBuilders.patch("/api/candidato/{id}", candidato1.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.PRESENCA_CONFIRMADA.name()));
        List<Candidato> candidatos = candidatoRepository.findAll();
        assertEquals(1, candidatos.size());
        assertEquals(candidato1.getId(), candidatos.get(0).getId());
        assertEquals(dto.getStatus(), candidatos.get(0).getStatus());
    }

    @Test
    public void verificaCPFExistenteTest() throws Exception {
        CandidatoDTO dto = CandidatoDTO.builder()
                .nomeCompleto("aquelecandidato")
                .nascimento(LocalDate.now())
                .cpf("00000000010")
                .telefone("000000000")
                .cep("91225002")
                .logradouro("rua")
                .numero(Long.MIN_VALUE)
                .bairro("bairoo")
                .cidade("cidade")
                .estado("estado")
                .email("jonas@gmail.com")
                .password("senhaWWW")
                .instituicao("instituicao")
                .curriculo("xxxxxx")
                .build();
        Candidato candidato2 = candidatoService.salvaCandidatoComEndereco(dto);

        restMockMvc.perform(MockMvcRequestBuilders.get("/api/candidato/cpf/{cpf}", candidato2.getCpf())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeCompleto").value(dto.getNomeCompleto()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataNascimento").value(dto.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(dto.getCpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.PENDENTE.name()));
        Candidato retorno = candidatoService.findByCpf(candidato2.getCpf());
        List<Candidato> candidatos = candidatoRepository.findAll();
        assertEquals(1, candidatos.size());
        assertEquals(new Long(1), candidatos.get(0).getId());
        assertEquals(candidato2.getCpf(), retorno.getCpf());
    }

    @Test
    public void verificaEmailExistenteTest() throws Exception {
        CandidatoDTO dto = CandidatoDTO.builder()
                .nomeCompleto("aquelecandidato")
                .nascimento(LocalDate.now())
                .cpf("00000000010")
                .telefone("000000000")
                .cep("91225002")
                .logradouro("rua")
                .numero(Long.MIN_VALUE)
                .bairro("bairoo")
                .cidade("cidade")
                .estado("estado")
                .email("jonas@gmailcom")
                .password("senhaWWW")
                .instituicao("instituicao")
                .curriculo("xxxxxx")
                .build();
        Candidato candidato2 = candidatoService.salvaCandidatoComEndereco(dto);
        User user = userRepository.findByUsername(dto.getEmail());
        System.out.println(user.getEmail());

        restMockMvc.perform(MockMvcRequestBuilders.get("/api/candidato/email/{email}", user.getEmail())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());

        User retorno = candidatoService.findByEmail(user.getEmail());
        List<Candidato> candidatos = candidatoRepository.findAll();
        assertEquals(1, candidatos.size());
        assertEquals(candidato2.getId(), candidatos.get(0).getId());
        assertEquals(user.getEmail(), retorno.getEmail());
    }

    @Test
    @WithMockUser(username = "admin.admin",
            password = "jwtpass",
            authorities = {"ADMIN_USER"})
    public void serchBetweenDatasTest() throws Exception {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter formatadorDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        CandidatoDTO dto = CandidatoDTO.builder()
                .nomeCompleto("ocandidato")
                .nascimento(LocalDate.now())
                .cpf("000.000.000-00")
                .telefone("000000000")
                .cep("91225002")
                .logradouro("rua")
                .numero(Long.MIN_VALUE)
                .bairro("bairoo")
                .cidade("cidade")
                .estado("estado")
                .email("jaqueline.pazbonoto@gmail.com")
                .password("senhaWWW")
                .instituicao("instituicao")
                .curriculo("xxxxxx")
                .build();
        Candidato candidato = candidatoService.salvaCandidatoComEndereco(dto);

        String url = "/api/candidato/search?page=0&size=10&sort=id,desc"
                + "&start=" + LocalDate.now().minusDays(1).format(formatadorDate).toString()
                + "&end=" + LocalDate.now().plusDays(1).format(formatadorDate).toString();

        restMockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nomeCompleto").value(dto.getNomeCompleto()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].dataNascimento").value(dto.getNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].cpf").value(dto.getCpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status").value(Status.PENDENTE.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endereco.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endereco.logradouro").value(dto.getLogradouro()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endereco.numero").value(dto.getNumero()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endereco.bairro").value(dto.getBairro()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endereco.cidade").value(dto.getCidade()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endereco.estado").value(dto.getEstado()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].user.username").value(dto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].user.firstName").value(dto.getNomeCompleto()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].dataInscricao").value(LocalDateTime.now().format(formatador)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].user.email").value(dto.getEmail()));
    }
}
