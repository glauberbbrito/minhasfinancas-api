package com.gbarreto.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.gbarreto.minhasfinancas.exception.RegraNegocioException;
import com.gbarreto.minhasfinancas.model.entity.Lancamento;
import com.gbarreto.minhasfinancas.model.enumeration.StatusLancamento;
import com.gbarreto.minhasfinancas.repository.LancamentoRepository;
import com.gbarreto.minhasfinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository lancamentoRepository;
	
	public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
		this.lancamentoRepository = lancamentoRepository;
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		lancamento.setStatus(StatusLancamento.PENDENTE);
		lancamento.setDataCadastro(Calendar.getInstance());
		validar(lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId()); //Verifica se já existe na base.
		validar(lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId()); //Verifica se já existe na base.
		lancamentoRepository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		
		
		return lancamentoRepository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		Objects.requireNonNull(lancamento.getId()); //Verifica se já existe na base.
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamento lancamento) {
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals(""))
			throw new RegraNegocioException("Informe uma descrição válida.");
		
		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12)
			throw new RegraNegocioException("Informe um mês válido.");
		
		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4)
			throw new RegraNegocioException("Informe um Ano válido.");
		
		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null)
			throw new RegraNegocioException("É necessário informar um usuário.");
		
		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1)
			throw new RegraNegocioException("Informe um valor maior do que zero.");
		
		if(lancamento.getTipo() == null)
			throw new RegraNegocioException("Informe um tipo de lançamento.");
		
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		return lancamentoRepository.findById(id);
	}

}
