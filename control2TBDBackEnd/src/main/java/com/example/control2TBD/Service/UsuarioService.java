package com.example.control2TBD.Service;

import com.example.control2TBD.Entity.UsuarioEntity;
import com.example.control2TBD.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<UsuarioEntity> findAll() {
        return usuarioRepository.findAll();
    }


    public Optional<UsuarioEntity> findById(Integer id) {
        return usuarioRepository.findById(id);
    }


    public UsuarioEntity save(UsuarioEntity usuario) {
        return usuarioRepository.save(usuario);
    }


    public UsuarioEntity update(UsuarioEntity usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }


    public boolean existsById(Integer id) {
        return usuarioRepository.existsById(id);
    }


}