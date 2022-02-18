package com.gbarreto.minhasfinancas.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gbarreto.minhasfinancas.model.entity.Usuario;

// TODO: FAZER AULAS 39,40, 41

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@eutesteemail.com.xp").senha("123456").build();
		entityManager.persist(usuario);
		
		//ação/execução
		boolean result = usuarioRepository.existsByEmail("usuario@eutesteemail.com.xp");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalseQuandoNaoHouverUsuarioCadastradoComOEmail() {
		//cenário

		//ação
		boolean result = usuarioRepository.existsByEmail("usuario@eutesteemail.com.xp");
		
		//verificação
		Assertions.assertThat(result).isFalse();
		
	}
	
	
}
