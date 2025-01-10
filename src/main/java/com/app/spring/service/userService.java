package com.app.spring.service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import com.app.spring.controller.dto.UsuarioRegistroDTO;
import com.app.spring.model.Rol;
import com.app.spring.model.Usuario;
import com.app.spring.repository.UsuarioRepositorio;
import com.app.spring.service.Interface.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService implements UsuarioServicio {

	
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public userService(UsuarioRepositorio usuarioRepositorio) {
		super();
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Override
	public Usuario guardar(UsuarioRegistroDTO registroDTO) {
		String regex="^[a-zA-Z0-9._%+-]+@metacontrol\\.cl$\n";
		if  (registroDTO.getEmail().matches(regex)){
		Usuario usuario = new Usuario(
				registroDTO.getNombre(),
				registroDTO.getApellido(),
				registroDTO.getEmail(),
				passwordEncoder.encode(registroDTO.getPassword()),
				Collections.emptyList() // Lista vacía en lugar de roles
		);
			return usuarioRepositorio.save(usuario);
		}
		throw new IllegalArgumentException("El correo no pertenece al dominio @metacontrol.cl");
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorio.findByEmail(username);
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuario o password inválidos");
		}
		return new User(usuario.getEmail(),usuario.getPassword(), mapearAutoridadesRoles(usuario.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
	}

	
	/*
	@Override
	public List<Usuario> listarUsuarios() {
		return usuarioRepositorio.findAll();
	}
	*/
}
