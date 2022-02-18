package com.gbarreto.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.gbarreto.minhasfinancas.model.enumeration.StatusLancamento;
import com.gbarreto.minhasfinancas.model.enumeration.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="TB_LANCAMENTO", schema="financas")
public class Lancamento {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LANCAMENTO")
	@SequenceGenerator(name = "SEQ_LANCAMENTO", sequenceName = "financas.SEQ_LANCAMENTO", allocationSize = 1)
	private Long id;
	
	@Column(name="descricao", length = 100, nullable = false )
	private String descricao;
	
	@Column(name="mes", nullable = false)
	private Integer mes;
	
	@Column(name="ano", nullable = false)
	private Integer ano;
	
	@ManyToOne
	@JoinColumn(name = "IDUSUARIO", nullable = false)
	private Usuario usuario;
	
	@Column(name = "VALOR", nullable = false)
	private BigDecimal valor;
	
	@Column(name = "DATACADASTRO", nullable = false)
	@DateTimeFormat(pattern = "yyyyy-mm-dd HH:mm:ss")
	private Calendar dataCadastro;
	
	@Column(name = "TIPO")
	@Enumerated(value = EnumType.STRING)
	private TipoLancamento tipo;
	
	@Column(name = "STATUS")
	@Enumerated(value = EnumType.STRING)
	private StatusLancamento status;

}
