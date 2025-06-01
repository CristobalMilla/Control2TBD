package com.example.control2TBD.Service;


import com.example.control2TBD.Entity.UsuarioEntity;
import com.example.control2TBD.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService es una clase de servicio que implementa la interfaz UserDetailsService necesaria para integrar Spring Security.
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    // Dependencia para interactuar con la base de datos y recuperar información del usuario.
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Constructor que recibe el repositorio como dependencia (inyección de dependencia).
     *
     * @param usuarioRepository El repositorio de usuarios.

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }*/

    /**
     * Carga los detalles del usuario por su nick.
     *
     * @param nick El nick del usuario.
     * @return Los detalles del usuario.
     * @throws UsernameNotFoundException Si el nick no se encuentra en el sistema.
     */
    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {

        // Se busca al usuario con el repositorio
        UsuarioEntity usuario = usuarioRepository.findByNickname(nick);

        if (usuario == null) {
            throw new UsernameNotFoundException("Nick no encontrado en el sistema: " + nick);
        }

        System.out.println("funcion: loadUserByUsername\nUsuario encontrado: " + usuario.getNickname());

        // Si existe, se crea un objeto UserDetails utilizando la clase User de Spring Security
        return User.builder()
                .username(usuario.getNickname())
                .password(usuario.getContrasenia())
                .roles(usuario.getRol())
                .build();

    }
}