package com.pi.comuniShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pi.comuniShop.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteIdOrderByCriadoEmDesc(Long clienteId);

}


