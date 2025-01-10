package com.app.spring.service.Interface;

import com.app.spring.controller.dto.UsuarioRegistroDTO;
import com.app.spring.model.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UsuarioServicio extends UserDetailsService{
	//metodo para registrar un usuario
	public Usuario guardar(UsuarioRegistroDTO registroDTO);

}
