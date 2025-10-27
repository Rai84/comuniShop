package com.pi.comuniShop.repository;

import com.pi.comuniShop.model.Negocio;
import com.pi.comuniShop.model.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Long> {
    List<Negocio> findByDonoId(Long donoId);

    Optional<Negocio> findByEmailComercial(String email);
    Negocio findByDono(Usuario dono);
}
