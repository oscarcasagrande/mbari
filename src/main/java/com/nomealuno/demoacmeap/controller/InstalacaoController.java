package com.nomealuno.demoacmeap.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nomealuno.demoacmeap.domain.Cliente;
import com.nomealuno.demoacmeap.domain.Instalacao;
import com.nomealuno.demoacmeap.repository.ClienteRepository;
import com.nomealuno.demoacmeap.repository.InstalacaoRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Acme AP Instalação Service")
public class InstalacaoController {
	
	@Autowired
	private InstalacaoRepository instalacaoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	
	@ApiOperation(value = "Mostra a lista de instalações")
	//Controle de versão explicito na URI
	@GetMapping("v1/instalacoes")
	public List<Instalacao> getAllInstalacoes()
	{
		
		ArrayList<Instalacao> listaInstalacoes = new ArrayList<Instalacao>();
		listaInstalacoes = (ArrayList<Instalacao>) instalacaoRepository.findAll();
			
		return listaInstalacoes;
	}
	
	
	@ApiOperation(value = "Consulta uma instalação pelo código")
	@GetMapping("v1/instalacoes/{codigo}")
	public Optional<Instalacao> getInstalacao(@PathVariable String codigo)
	{

		return instalacaoRepository.findByCodigo(codigo);
	}
	
	@ApiOperation(value = "Consulta uma instalação pelo CPF")
	@GetMapping("v1/instalacoes/cpf/{cpf}")
	public List<Instalacao> getInstalacaoPorCPF(@PathVariable String cpf)
	{
		Optional<Cliente> cliente = clienteRepository.findByCpf(cpf);
		
		return instalacaoRepository.findByCliente(cliente.get());
	}
	
	@ApiOperation(value = "Cadastrar uma nova instalação")
	@PostMapping("v1/instalacoes")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> cadastrarInstalacao(@RequestBody Instalacao instalacao)
	{
		Instalacao instalacaoCriada = instalacaoRepository.save(instalacao);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(instalacaoCriada.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	
	
}
