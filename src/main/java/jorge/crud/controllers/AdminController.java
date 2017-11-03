/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jorge.crud.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.gson.JsonObject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.function.Predicate;
import javax.validation.Valid;

import jorge.crud.models.Usuario;

import jorge.crud.repositories.UsuarioRepository;
import jorge.crud.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author jorge
 */
@Controller
public class AdminController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
   
    
    @Autowired
    private UsuarioService usuarioService;
    
     @RequestMapping("/admin/hola")
    public String hola(){
        return "login";  
    }
  
    @RequestMapping("/admin/inicio")
    public String inicio(Authentication authentication,Model model){
        Usuario u = (Usuario) authentication;
        model.addAttribute("usuario", u);    
        return "views/index";
    }
    
    
    @RequestMapping("/login")
    public String login(){
              
        return "views/login";
    }

   
    @RequestMapping("/datos/{idUser}")
    public String datos(@PathVariable("idUser") Integer idUser,Authentication authentication,Model model){
        Usuario u = usuarioRepository.findOne(idUser);
        model.addAttribute("usuario", u);    
        return "views/datos";
    }
    

    /******************************************USUARIO********************************************/
        
    @RequestMapping("/admin/usuarios")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String usuarios(Authentication authentication,Model model){
        Usuario u = (Usuario) authentication;
        model.addAttribute("usuario", u);  
        return "views/usuarios";
    }
    
    @RequestMapping("/admin/usuariosJson")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public Iterable<Usuario> usuariosJson(Authentication authentication){
        Iterable<Usuario> usuarios = null;
        Usuario u = (Usuario) authentication;
        if(isSuAdmin(authentication)){
            usuarios = usuarioRepository.findAll();
        }
        return usuarios;
        
    }
    
    @PostMapping("/admin/usuario/j")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public Object insertarUsuario(@RequestBody Usuario usuario){
        
        try{
           
           if(usuario.getId()==0){
           usuario.setId(0);
           
           BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
           String password = bCryptPasswordEncoder.encode(usuario.getPassword());
           usuario.setPassword(password);
            
           usuario = usuarioRepository.save(usuario);
           }else{
           Usuario usuarioExist=new Usuario();
           usuarioExist = usuarioService.findByEmail(usuario.getEmail());    
               
           Usuario usu2 = new Usuario();
           usu2.setId(usuario.getId());
           usu2.setNombre(usuario.getNombre());
           usu2.setEmail(usuario.getEmail());
           if(usuario.getPassword()==null || usuario.getPassword().equals("")){
               usu2.setPassword(usuarioExist.getPassword());
           }else{
               BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
               String password = bCryptPasswordEncoder.encode(usuario.getPassword());
               usu2.setPassword(password);
           }
           
           usu2.setTelefono(usuario.getTelefono());
           usu2.setDireccion(usuario.getDireccion());
           usu2.setCi(usuario.getCi());
           usuario = usuarioRepository.save(usu2);
           } 
          
        }catch(Exception e){
            System.out.println("error: *******************");
            System.out.println(e);
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return usuario;
    }
    
    @PostMapping("/admin/usuarios/eliminar/j")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public Object eliminarUsuario(@RequestBody Usuario usuario){
        
         try{
         
           usuario.setId(usuario.getId());
           usuarioRepository.delete(usuario.getId());

        }catch(Exception e){
            System.out.println("*******************");
            System.out.println(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return usuario;
    }

    
     private boolean isSuAdmin(Authentication authentication){
          Usuario usuario = (Usuario) authentication;
        Predicate<GrantedAuthority> p1 =  g -> g.getAuthority().equals("ROLE_USER");
        return authentication.getAuthorities().stream().anyMatch(p1);
    }

}
