package com.pi.comuniShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pi.comuniShop.model.ItemPedido;
import com.pi.comuniShop.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}


