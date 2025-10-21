package com.pi.comuniShop.repository;

import com.pi.comuniShop.model.ImagemCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImagemCatalogoRepository extends JpaRepository<ImagemCatalogo, Long> {
    List<ImagemCatalogo> findByCatalogoId(Long catalogoId);
    int countByCatalogoId(Long catalogoId); // 👈 ESSA LINHA É OBRIGATÓRIA
}

