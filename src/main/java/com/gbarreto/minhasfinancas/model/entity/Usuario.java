package com.gbarreto.minhasfinancas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="TB_USUARIO", schema="financas")
public class Usuario {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
	@SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "financas.SEQ_USUARIO", allocationSize = 1)
	private Long id;
	
	@Column(name="NOME", length = 150)
	private String nome;
	
	@Column(name="EMAIL", length = 100)
	private String email;
	
	@Column(name="SENHA", length = 20)
	private String senha;

	
}
