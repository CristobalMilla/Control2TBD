package com.example.control2TBD.Controller;

import com.example.control2TBD.Entity.UsuarioEntity;
import com.example.control2TBD.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioEntity> findAll() {
        return usuarioService.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> findById(@PathVariable Integer id) {
        Optional<UsuarioEntity> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UsuarioEntity save(@RequestBody UsuarioEntity usuario) {
        return usuarioService.save(usuario);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> update(@PathVariable Integer id, @RequestBody UsuarioEntity usuario) {
        if (!usuarioService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        UsuarioEntity updatedUsuario = usuarioService.update(usuario);
        return ResponseEntity.ok(updatedUsuario);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        if (!usuarioService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public boolean existsById(@PathVariable Integer id) {
        return usuarioService.existsById(id);
    }
}