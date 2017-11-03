/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jorge.crud.services;

import java.security.Principal;
import jorge.crud.models.Usuario;
import jorge.crud.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

/**
 *
 * @author william
 */
@Service
public class AuthorizationService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    
    public boolean isUserProduct(Authentication authentication,Long idProducto){
        Usuario usuario = usuarioRepository.findByNombre(authentication.getName());
        if(usuario==null){
            return false;
        }
        /*Producto producto = productoRepository.findOne(idProducto);
        if(producto==null){
            return false;
        }
        return usuario.getEmpresa().getId().equals(producto.getEmpresa().getId());*/
        return true;
    }
    
    public boolean isUserValidate(Principal principal){
        Usuario usuario = (Usuario) principal;
        System.out.println("ID USUARIO: "+usuario.getEmail());
        if(usuario.getEmail().equals("jorge@gmail.com")){
            return true;
        }
        
        return false;
    }
}
