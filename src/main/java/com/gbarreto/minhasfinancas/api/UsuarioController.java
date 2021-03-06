package com.gbarreto.minhasfinancas.api;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gbarreto.minhasfinancas.api.dto.UsuarioDTO;
import com.gbarreto.minhasfinancas.exception.ErroAutenticacao;
import com.gbarreto.minhasfinancas.model.entity.Usuario;
import com.gbarreto.minhasfinancas.service.LancamentoService;
import com.gbarreto.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;
	private final LancamentoService lancamentoService;
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO) {
		try {
			Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		}catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO){
		
		Usuario usuario = Usuario.builder()
				.nome(usuarioDTO.getNome())
				.email(usuarioDTO.getEmail())
				.senha(usuarioDTO.getSenha()).build();
		try {
			Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
		Optional<Usuario> usuario = usuarioService.obterPorId(id);
		
		if (!usuario.isPresent())
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		
		BigDecimal saldo =  lancamentoService.obterSaldoPorUsuario(id);
		
		return ResponseEntity.ok(saldo);
	}
	
}
