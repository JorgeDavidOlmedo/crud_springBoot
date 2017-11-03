/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jorge.crud.services;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import jorge.crud.models.Usuario;
import jorge.crud.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author william
 */
@Service
public class UsuarioAuthService implements UserDetailsService{
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario==null){
            System.out.println("null...");
            //throw new UsernameNotFoundException("El Usuario no esta activo");
            return null;
        }
        System.out.println("entra ak a consultar...");
        List<GrantedAuthority> authoritys = new ArrayList<>();
        authoritys.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getPassword(), authoritys);
    }
    
    
    
}
