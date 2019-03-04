package br.com.dbc.PreSelecao.service;


import br.com.dbc.PreSelecao.entity.User;
import br.com.dbc.PreSelecao.repository.RoleRepository;
import br.com.dbc.PreSelecao.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class AppUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    //método para criar novo usuário. o username, que é o proprio email, não pode estar cadastrado
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public User createNewUser(User user) throws Exception{
        if(userRepository.findByUsername(user.getUsername()) != null){
           throw new Exception("User already exist!");
        }
        user.setRoles(Arrays.asList( roleRepository.findByRoleName("STANDARD_USER") ));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    //método para trocar senha
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public User changeUserPassword(User user) throws UsernameNotFoundException{
        User atual = userRepository.findByUsername(user.getUsername());
        if(atual == null) {
           throw new UsernameNotFoundException(String.format("The username %s doesn't exist", user.getUsername()));
        }
        atual.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(atual);
    }
    
    //método padrão da interface que nos ajuda a encontrar pelo nome de usuário
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);

        if(user == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });

        UserDetails userDetails = new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), authorities);

        return userDetails;
    }
    
    public User findByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() ? user.get() : User.builder().build();
    }
}
