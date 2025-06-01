package com.example.control2TBD.Repository;

import com.example.control2TBD.Entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UsuarioRepository es una interfaz que extiende JpaRepository para manejar operaciones CRUD en la entidad UsuarioEntity.
 */
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    /**
     * Encuentra un usuario por su email.
     *
     * @param nickname El nick del usuario.
     * @return El usuario correspondiente al nick proporcionado.
     */
    UsuarioEntity findByNickname(String nickname);
}