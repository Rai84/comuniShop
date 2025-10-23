package com.pi.comuniShop.repository;

import com.pi.comuniShop.model.Negocio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Long> {
    List<Negocio> findByDonoId(Long donoId);
}
