/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jorge.crud.services;

import jorge.crud.models.Usuario;
import jorge.crud.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jorge
 */
@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
     public Usuario findByEmail(String email){
        return usuarioRepository.findByEmail(email);
    }
     
      public Usuario findById(Integer id){
        return usuarioRepository.findOne(id);
    }
    
    public Usuario findByName(String name){
        return usuarioRepository.findByNombre(name);
    }
    
    public Usuario validateUser(Integer idUsuario){
        return usuarioRepository.validateUser(idUsuario);
    }
     
    public Usuario usuarioByEmail(String email){
        return usuarioRepository.findUsuarioByEmail(email);
    }
    
    public Usuario saveOrUpdate(Usuario usuario){
        
        usuario=usuarioRepository.save(usuario);
        
        return usuario;
    }
    
    public void updatePassword(String password,String email){
      usuarioRepository.updatePassword(password,email);
       
    }
    
    public Iterable<Usuario>findAll(){
        return usuarioRepository.findAll();
    }
}
