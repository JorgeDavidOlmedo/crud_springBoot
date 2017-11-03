/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jorge.crud.controllers;
import java.security.Principal;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import jorge.crud.models.Usuario;
import jorge.crud.repositories.UsuarioRepository;
import jorge.crud.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author william
 */
@RestController
public class CredencialesController {
    
    @Autowired
    private UsuarioService usuarioService;


    @RequestMapping(value="/credenciales/usuario/create",method=RequestMethod.POST)
    public ResponseEntity createUsuario(@RequestBody Usuario usuario,HttpServletResponse response){
         try {
            
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String password = bCryptPasswordEncoder.encode(usuario.getPassword());
            usuario.setPassword(password);

            usuario.setId(0);
            usuario = usuarioService.saveOrUpdate(usuario);
        } catch (Exception e) {
            System.out.println(e.toString());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(e.toString(), HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity(usuario, HttpStatus.OK);
    }
    
   
  
      
}


