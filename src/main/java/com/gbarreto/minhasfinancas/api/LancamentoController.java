package com.gbarreto.minhasfinancas.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gbarreto.minhasfinancas.api.dto.LancamentoDTO;
import com.gbarreto.minhasfinancas.exception.RegraNegocioException;
import com.gbarreto.minhasfinancas.model.entity.Lancamento;
import com.gbarreto.minhasfinancas.model.entity.Usuario;
import com.gbarreto.minhasfinancas.model.enumeration.StatusLancamento;
import com.gbarreto.minhasfinancas.model.enumeration.TipoLancamento;
import com.gbarreto.minhasfinancas.service.LancamentoService;
import com.gbarreto.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

	private final LancamentoService lancamentoService;
	private final UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto ) {
		Lancamento lancamento = converter(dto);
		
		try {
			lancamento = lancamentoService.salvar(lancamento);
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	/**
	 * Método para atualizar o lancamento.
	 * @param id
	 * @param dto
	 * @return
	 */
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		return lancamentoService.obterPorId(id).map( entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				lancamento.setDataCadastro(entity.getDataCadastro());
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet( () -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id).map( entity -> {
			lancamentoService.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet( () -> 
			new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity buscar(
			//@RequestParam( java.util.Map<String, String> params) Outra forma de setar os parâmetros e reduzir a quantidade de linhas.
			//                                                     Dessa forma todos os parâmetros são opcionoais. 
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam("usuario") Long idUsuario) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário naõ encontrado.");
		}else
			lancamentoFiltro.setUsuario(usuario.get());
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
		
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService
				.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuário não localizado para o ID informado."));
		
		lancamento.setUsuario(usuario);
		
		if (dto.getTipo() != null)
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		
		if (dto.getStatus() != null)
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		
		return lancamento;
		
	}
}
