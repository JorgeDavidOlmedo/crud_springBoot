/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jorge.crud.repositories;

import java.util.ArrayList;
import java.util.List;
import jorge.crud.models.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author william
 */
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Integer>{
    public Usuario findByNombre(String nombre);
    public Usuario findByEmail(String email);
   public Usuario findUsuarioByEmail(String mail);
    public Usuario findById(int id);
    
    @Query("Select v from Usuario v where id = ?1")
    Usuario validateUser(Integer idUsuario);
    
    @Modifying
    @Transactional
    @Query("Update Usuario set password=?1 where email=?2")
    void updatePassword(String password,String email);
       
}
