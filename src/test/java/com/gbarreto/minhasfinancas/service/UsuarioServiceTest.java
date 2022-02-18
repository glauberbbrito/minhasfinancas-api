package com.gbarreto.minhasfinancas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gbarreto.minhasfinancas.model.entity.Usuario;
import com.gbarreto.minhasfinancas.repository.UsuarioRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
// TO DO: VER AULAS 36, 42 A 51
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Test
	public void deveValidarEmail() {
		//cenário
		usuarioRepository.deleteAll();
		
		//ação
		usuarioService.validarEmail("email@email.com");
		
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenário
		Usuario usuario = Usuario.builder().nome("Teste Usuário").email("e-mail@testeunitario.com.br").build();
		usuarioRepository.save(usuario);
		
		//ação
		usuarioService.validarEmail("e-mail@testeunitario.com.br");
		
	}
}
