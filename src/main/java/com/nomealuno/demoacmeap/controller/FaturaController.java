package com.nomealuno.demoacmeap.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nomealuno.demoacmeap.domain.Cliente;
import com.nomealuno.demoacmeap.domain.Fatura;
import com.nomealuno.demoacmeap.domain.Instalacao;
import com.nomealuno.demoacmeap.repository.ClienteRepository;
import com.nomealuno.demoacmeap.repository.FaturaRepository;
import com.nomealuno.demoacmeap.repository.InstalacaoRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Acme AP Fatura Service", produces = MediaType.APPLICATION_JSON_VALUE)
public class FaturaController {

	@Autowired
	private FaturaRepository faturaRepository;

	@Autowired
	private InstalacaoRepository instalacaoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@ApiOperation(value = "Mostra a lista de faturas")
	// Controle de versão explicito na URI
	@GetMapping("v1/faturas")
	public List<Fatura> getAllFaturas() {

		ArrayList<Fatura> listaFaturas = new ArrayList<Fatura>();
		listaFaturas = (ArrayList<Fatura>) faturaRepository.findAll();

		return listaFaturas;
	}

	@ApiOperation(value = "Consulta uma fatura pelo código")
	@GetMapping("v1/faturas/{codigo}")
	public Optional<Fatura> getFatura(@PathVariable String codigo) {

		return faturaRepository.findByCodigo(codigo);
	}

	@ApiOperation(value = "Consulta as faturas pelo CPF do cliente")
	@GetMapping("v1/faturas/cpf/{cpf}")
	public List<Fatura> getFaturasPorCPF(@PathVariable String cpf) {
		Optional<Cliente> cliente = clienteRepository.findByCpf(cpf);
		List<Instalacao> listaInstalacao = instalacaoRepository.findByCliente(cliente.get());

		List<Fatura> listaFaturasCliente = new ArrayList<Fatura>();

		listaInstalacao.stream()
				.forEach(item -> item.getListaFatura().stream().forEach(fatura -> listaFaturasCliente.add(fatura)));

		return listaFaturasCliente;
	}

	@ApiOperation(value = "Gerar uma nova fatura")
	@PostMapping("v1/faturas")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> gerarFatura(@RequestBody Fatura fatura) {
		
		Optional<Instalacao> instalacaoRecuperada = instalacaoRepository.findByCodigo(fatura.getInstalacao().getCodigo());
		
		fatura.setInstalacao(instalacaoRecuperada.get());
		
		Fatura faturaCriada = faturaRepository.save(fatura);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(faturaCriada.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

}
