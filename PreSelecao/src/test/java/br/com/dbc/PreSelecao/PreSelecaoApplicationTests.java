package br.com.dbc.PreSelecao;

import br.com.dbc.PreSelecao.rest.AbstractRestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class PreSelecaoApplicationTests {

    @Test
    public void contextLoads() {
    }
    
    protected final double DELTA = 0.1d;

    protected MockMvc restMockMvc;

    @Autowired
    protected MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    
    protected abstract AbstractRestController getController();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.restMockMvc = MockMvcBuilders.standaloneSetup(getController())
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
        
    }

}
