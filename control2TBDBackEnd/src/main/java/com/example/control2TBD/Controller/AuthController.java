package com.example.control2TBD.Controller;

import com.example.control2TBD.Config.JwtUtil;
import com.example.control2TBD.Entity.UsuarioEntity;
import com.example.control2TBD.Repository.UsuarioRepository;
import com.example.control2TBD.dto.LoginDto;
import com.example.control2TBD.dto.RegisterDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthController es un controlador que maneja las solicitudes HTTP relacionadas con la autenticación y registro de usuarios.
 */
@RestController
@RequestMapping("/auth/usuarios")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Realiza el login del usuario.
     *
     * @param loginDto Los datos de inicio de sesión del usuario.
     * @return La respuesta HTTP con el token y el ID del usuario o un mensaje de error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        // Se imprimen los datos recibidos en el backend
        System.out.println("Datos recibidos en el backend: " + loginDto);

        try {
            // Verificar si el usuario existe en la base de datos antes de autenticar
            UsuarioEntity user = usuarioRepository.findByNickname(loginDto.getNickname());
            if (user == null) {
                // Error 400
                System.out.println("No se encontró el usuario con el nickname proporcionado.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El nickname no está registrado. Por favor, regístrese primero.");
            }

            // Se autentica al usuario con los datos proporcionados
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getNickname(),
                    loginDto.getContrasenia()
            );

            System.out.println("Datos del login: \n\tnickname: " + loginToken.getPrincipal().toString());
            try {
                authenticationManager.authenticate(loginToken);
                System.out.println("Autenticación exitosa.");
            } catch (Exception e) {
                System.out.println("Error en el AuthenticationManager: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
            }

            // Si la autenticación es exitosa, se genera JWT
            System.out.println("\nSe llama a jwtUtil para crear el token.");
            String jwt = jwtUtil.createToken(loginDto.getNickname());

            // Se construye la respuesta con el token y el ID del usuario
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("id_usuario", user.getId_usuario());
            response.put("nickname", user.getNickname());
            response.put("role", user.getRol());

            System.out.println("LOGIN----");
            System.out.println("id_usuario: " + user.getId_usuario());
            System.out.println("nickname: "+ user.getNickname());
            System.out.println("role: " + user.getRol());

            return ResponseEntity.ok(response); // Devolver el token +id+nick+rol

        } catch (BadCredentialsException e) {
            // Maneja credenciales incorrectas
            // 401 Unauthorized
            System.out.println("Credenciales incorrectas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas. Verifique su correo y contraseña.");

        } catch (Exception e) {
            // Cualquier otro error
            System.out.println("Error en el proceso de autenticación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno en el servidor.");
        }
    }

    /**
     * Registra un nuevo usuario.
     *
     * @param registerDto Los datos de registro del nuevo usuario.
     * @return La respuesta HTTP con el mensaje de éxito o error.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        // Se verifica si el usuario ya existe con ese nickname
        UsuarioEntity foundUser = usuarioRepository.findByNickname(registerDto.getNickname());

        if (foundUser != null) {
            // 409 La solicitud no se puede completar por un conflicto (ya existe)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El nick ya se encuentra registrado.\n");
        }

        // Se puede implementar la validación de datos aquí y dar error 400 en caso de no cumplirse
        try {
            // Asignar rol por defecto si no se proporciona
            String rol = (registerDto.getRol() == null || registerDto.getRol().isEmpty()) ? "CLIENTE" : registerDto.getRol();

            // Cifrar la contraseña
            String hashedPassword = passwordEncoder.encode(registerDto.getContrasenia());

            // Se crea un punto geográfico usando coordenadas de latitud y longitud del DTO
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
            Point ubicacion = geometryFactory.createPoint(
                    new Coordinate(registerDto.getLng(), registerDto.getLat())
            );

            // Se crea un nuevo usuario
            UsuarioEntity newUser = new UsuarioEntity(
                    registerDto.getNickname(),
                    hashedPassword,
                    ubicacion,
                    rol
            );

            usuarioRepository.save(newUser);

            // Generamos el token de inmediato para devolverlo al front
            String jwt = jwtUtil.createToken(newUser.getNickname());

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("id_usuario", newUser.getId_usuario());
            response.put("nickname", newUser.getNickname());
            response.put("role", newUser.getRol());

            System.out.println("REGISTER----");
            System.out.println("id_usuario: " + newUser.getId_usuario());
            System.out.println("nickname: "+ newUser.getNickname());
            System.out.println("role: " + newUser.getRol());

            // 201 Solicitud exitosa
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            // 500 Error en la lógica del backend
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno en el servidor.\n");
        }
    }

    /**
     * Verifica la validez de un token JWT.
     *
     * @param token El token JWT a verificar.
     * @return La respuesta HTTP con el mensaje de validez del token.
     */
    @PostMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestBody String token) {
        try {
            if (token == null || !token.startsWith("Bearer")) {
                // 403 El cliente no tiene permisos para acceder al recurso solicitado
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El token no es válido.");
            }
            // Se elimina el prefijo "Bearer"
            token = token.replace("Bearer ", "");
            boolean isValid = jwtUtil.isValid(token);

            if (!isValid) {
                // 403 El cliente no tiene permisos para acceder al recurso solicitado
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El token no es válido.");
            }
            return ResponseEntity.ok(Map.of("message", "Token válido."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el servidor.\n");
        }
    }
}