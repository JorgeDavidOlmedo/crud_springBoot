/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jorge.crud.services;

import java.security.Principal;
import java.util.function.Predicate;
import jorge.crud.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 *
 * @author jorge
 */
@Service
public class AuthService {
    
    @Autowired
    private UsuarioService usuarioService;
    
    public boolean isUserValidate(Authentication authentication){
        Usuario usuario = (Usuario) authentication;
        System.out.println("ID USUARIO: "+usuario.getId());
        Usuario u = usuarioService.validateUser(usuario.getId());
         System.out.println("ENTIDAD: "+u);
        if(u==null){
            return false;
        }
       
        return true;
    }
}
