package com.oscar.demoacmeap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oscar.demoacmeap.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	public Optional<Cliente> findByCpf(String cpf);
	
}