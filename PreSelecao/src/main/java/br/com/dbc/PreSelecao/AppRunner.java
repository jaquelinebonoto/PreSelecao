
package br.com.dbc.PreSelecao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppRunner implements CommandLineRunner{

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    @Transactional(readOnly = false)
    public void run(String... args) throws Exception {
        System.out.println("salva roles");
        // Para o heroku Deploy
        /*
        jdbcTemplate.update("INSERT INTO app_role (id, role_name, description) VALUES (1, 'STANDARD_USER', 'Standard User - Has no admin rights')");
        jdbcTemplate.update("INSERT INTO app_role (id, role_name, description) VALUES (2, 'ADMIN_USER', 'Admin User - Has permission to perform admin tasks')");
        
        jdbcTemplate.update("INSERT INTO app_user (id, first_name, last_name, password, username, email) VALUES (nextval('S_USER'), 'John', 'Doe', '$2a$10$qtH0F1m488673KwgAfFXEOWxsoZSeHqqlB/8BTt3a6gsI5c2mdlfe', 'john.doe', 'xxxx@xxxx.xxx.xx')");
        jdbcTemplate.update("INSERT INTO app_user (id, first_name, last_name, password, username, email) VALUES (nextval('S_USER'), 'Admin', 'Admin', '$2a$10$qtH0F1m488673KwgAfFXEOWxsoZSeHqqlB/8BTt3a6gsI5c2mdlfe', 'admin.admin', 'xxxx@xxxx.xxx.xx')");
        /**/
        
        // Para uso local
        
        jdbcTemplate.update("INSERT INTO app_role (id, role_name, description) VALUES (1, 'STANDARD_USER', 'Standard User - Has no admin rights')");
        jdbcTemplate.update("INSERT INTO app_role (id, role_name, description) VALUES (2, 'ADMIN_USER', 'Admin User - Has permission to perform admin tasks')");
        
        jdbcTemplate.update("INSERT INTO app_user (id, first_name, last_name, password, username, email) VALUES (S_USER.NEXTVAL, 'John', 'Doe', '$2a$10$qtH0F1m488673KwgAfFXEOWxsoZSeHqqlB/8BTt3a6gsI5c2mdlfe', 'john.doe', 'xxxx@xxxx.xxx.xx')");
        jdbcTemplate.update("INSERT INTO app_user (id, first_name, last_name, password, username, email) VALUES (S_USER.NEXTVAL, 'Admin', 'Admin', '$2a$10$qtH0F1m488673KwgAfFXEOWxsoZSeHqqlB/8BTt3a6gsI5c2mdlfe', 'admin.admin', 'xxxx@xxxx.xxx.xx')");
        /**/

        jdbcTemplate.update("INSERT INTO user_role(user_id, role_id) VALUES(1,1)");
        jdbcTemplate.update("INSERT INTO user_role(user_id, role_id) VALUES(2,1)");
        jdbcTemplate.update("INSERT INTO user_role(user_id, role_id) VALUES(2,2)");
        
        System.out.println("salvou roles");
    }
    
}
