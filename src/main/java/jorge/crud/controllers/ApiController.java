/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jorge.crud.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.servlet.http.HttpServletResponse;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import jorge.crud.models.Usuario;
import jorge.crud.repositories.UsuarioRepository;
import jorge.crud.services.UsuarioService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author william
 */
@RestController
public class ApiController {
    
    @Autowired
    private UsuarioService usuarioService;


    
    private final String TOPIC = "JavaSampleApproach";

    
         
    @GetMapping(value = "/")
    public String home(){
        return "views/index";
    }
    
    @RequestMapping(value="/api/info/" ,method = RequestMethod.GET)
    public String info(){
        return "usuario correcto";
    }

 
    @RequestMapping(value="/api/usuario/obtenerTodos" ,method = RequestMethod.GET)
    public Iterable<Usuario> allUsuarios(){
        return usuarioService.findAll(); 
    }
    
    @RequestMapping(value="/api/usuario/{idUsuario}" ,method = RequestMethod.GET)
    public Usuario usuarioById(@PathVariable("idUsuario") Integer idUsuario,HttpServletResponse response){
         Usuario usuario = null;
        try {
            usuario = usuarioService.findById(idUsuario);
        } catch (Exception e) {
           response.setStatus(HttpStatus.BAD_REQUEST.value());
           return null;
        }
        return usuario;
    
    }
    
    @RequestMapping(value="/api/usuario/nombre/{name}" ,method = RequestMethod.GET)
    public Usuario usuarioByName(@PathVariable("name") String name,HttpServletResponse response){
         Usuario usuario = null;
        try {
            usuario = usuarioService.findByName(name);
        } catch (Exception e) {
           response.setStatus(HttpStatus.BAD_REQUEST.value());
           return null;
        }
        return usuario;
    
    }

   
    /****************************************/
    
    @RequestMapping(value="/api/usuario/update",method=RequestMethod.PUT)
    public ResponseEntity updateUsuario(@RequestBody Usuario usuario,HttpServletResponse response){
         try {
             Usuario usuario2 = new Usuario();
             usuario2 = usuarioService.findByEmail(usuario.getEmail());
             usuario.setPassword(usuario2.getPassword());
             usuario.setId(usuario2.getId());
             usuario = usuarioService.saveOrUpdate(usuario);
        } catch (Exception e) {
             System.out.println(e.toString());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(e.toString(), HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity(usuario, HttpStatus.OK);
    }


    
    @PreAuthorize("hasRole('ROLE_USER') AND @authService.isUserValidate(authentication)")
    @RequestMapping(value = "/api/usuario/email",method=RequestMethod.POST)
    public Usuario usuarioByEmail(@RequestBody Usuario usuario,HttpServletResponse response,Principal principal){
       try{
           System.out.println("EMAIL: "+usuario.getEmail());
            usuario = usuarioService.findByEmail(usuario.getEmail());
            
        }catch(Exception e){
            
        }
       System.out.println(usuario);
        if(usuario==null){
            System.out.println("nulo...");
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }else{
          
                usuario=usuario;
          
        }
        return usuario;
    }

    @PreAuthorize("hasRole('ROLE_USER') AND @authService.isUserValidate(authentication)")
    @RequestMapping(value="/api/cambiarPassword/{password}")
    public String cambiarPassword(@PathVariable("password") String password,HttpServletResponse response,Principal principal){
         Usuario usuario = null;
        try {
            
            usuario = usuarioService.findByEmail(principal.getName());
           
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String pass = bCryptPasswordEncoder.encode(password);
            usuario.setPassword(pass);
           
            usuario.setId(usuario.getId());
            usuario = usuarioService.saveOrUpdate(usuario);
        } catch (Exception e) {
           // System.out.println(e);
           //response.setStatus(HttpStatus.BAD_REQUEST.value());
           //return null;
           return "OK";
        }
        return "cambio de password correcto.";
    
    }
    
    @RequestMapping(path = "/api/sender", method = RequestMethod.POST)
    public String triggerEmail() {
        
       
        try {
         
            return "{\"message\": \"OK\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"message\": \"Error\"}";
        }
    }
    
      
}

