
package br.com.dbc.PreSelecao.rest;

import br.com.dbc.PreSelecao.entity.User;
import br.com.dbc.PreSelecao.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAuthority('ADMIN_USER')")
public class UserRestController {
    
    @Autowired
    private AppUserDetailsService userService;
    
    @PostMapping
    public  ResponseEntity<?> createNewUser(@RequestBody User user){
        try{
            User resultado = userService.createNewUser(user);
            return  ResponseEntity.ok(resultado);
        }
        catch(Exception e) {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body("{ \"error\" : \"" + e.getMessage() +"\" }");
        }   
    }
    
    @PostMapping("/password")
    public  ResponseEntity<?> changingUserPassword(@RequestBody User user){
        try {
            User resultado = userService.changeUserPassword(user);
            return  ResponseEntity.ok(resultado);
        }
        catch(Exception e){
            System.out.println(e);
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body("{ \"error\" : \"" + e.getMessage() +"\" }");
        }

    }
    
    @GetMapping("/role")
    public  ResponseEntity<?> userIsAdm(){
        return ResponseEntity
        .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body("{ \"user\" : \"ok\" }");

    }
}
