/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jorge.crud;

import java.util.ArrayList;
import java.util.List;
import jorge.crud.models.Usuario;
import jorge.crud.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @author jorge
 */
@org.springframework.context.annotation.Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class Configuracion extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private UsuarioService usuarioService;
    
     @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                      List<GrantedAuthority> roles = new ArrayList<>();
                        Usuario usuario=null;
                       
                            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                            Usuario u = usuarioService.usuarioByEmail(authentication.getPrincipal().toString());
                            if(u!=null){
                                if(passwordEncoder.matches(authentication.getCredentials().toString(),u.getPassword())){
                           
                                    roles.add(new SimpleGrantedAuthority("ROLE_USER"));
                          
                                    usuario = new Usuario(u.getEmail(),"",roles);
                                    usuario.setId(u.getId());
                                    usuario.setNombre(u.getNombre());

                                }else{
                                    throw new AuthenticationCredentialsNotFoundException("Credenciales incorrectas");
                                }
                            }else{
                                throw new AuthenticationCredentialsNotFoundException("Credenciales incorrectas");
                            }

                        
                        return usuario;
            }
            

            @Override
            public boolean supports(Class<?> type) {
                return true;
            }
        });
    }
    
    
      @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
               
                .authorizeRequests()
                .antMatchers("/css/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .hasAnyRole("USER")
                
                .and()
                .formLogin()
                .loginPage("/login")
                .successForwardUrl("/admin/inicio")
                .permitAll()
                .and()
                .csrf().disable()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
                ;
      
    }
  
    
    //api security
   
    @org.springframework.context.annotation.Configuration
    @Order(1)
    public static class ApiWebSecurityConfiguration extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception{
          
                    http 
                       
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest().fullyAuthenticated()
                    .and()
                    .httpBasic()
                    .and()
                     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .csrf().disable(); 
                   
        }
    }
    
    @org.springframework.context.annotation.Configuration
    @Order(2)
    public static class ApiWebSecurityConfiguration2 extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception{
          
                    http 
         
                    .antMatcher("/credenciales/**")
                    .authorizeRequests()
                    .anyRequest().permitAll()
                    .and()         
                    .httpBasic()
                    .and()
                     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .csrf().disable();   
                   
        }
    }
    
    @org.springframework.context.annotation.Configuration
    @Order(3)
    public static class ApiWebSecurityConfiguration3 extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception{
          
                    http 
         
                    .antMatcher("/admin/**")
                    .authorizeRequests()
                    .anyRequest().fullyAuthenticated()
                    .and()
                    .csrf().disable();   
                   
        }
    }
    
    @org.springframework.context.annotation.Configuration
    @Order(4)
    public static class ApiWebSecurityConfiguration4 extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception{
          
                    http 
         
                    .antMatcher("/js/**")
                    .authorizeRequests()
                    .anyRequest().permitAll()
                    .and()         
                    .httpBasic()
                    .and()
                     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .csrf().disable();   
                   
        }
    }
    
    @org.springframework.context.annotation.Configuration
    @Order(5)
    public static class ApiWebSecurityConfiguration5 extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception{
          
                    http 
         
                    .antMatcher("/recuperar/**")
                    .authorizeRequests()
                    .anyRequest().permitAll()
                    .and()         
                    .httpBasic()
                    .and()
                     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .csrf().disable();   
                   
        }
    }
    
    
}
