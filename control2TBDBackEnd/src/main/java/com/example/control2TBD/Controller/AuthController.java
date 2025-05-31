package com.example.control2TBD.Controller;

import com.example.control2TBD.Config.JwtUtil;
import com.example.control2TBD.dto.LoginDto;
import com.example.control2TBD.dto.RegisterDto;
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
@RequestMapping("/auth")
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
            UsuarioEntity user = usuarioRepository.findByEmail(loginDto.getEmail());
            if (user == null) {
                // Error 400
                System.out.println("No se encontró el usuario con el correo proporcionado.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El correo no está registrado. Por favor, regístrese primero.");
            }

            // Se autentica al usuario con los datos proporcionados
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            );

            System.out.println("Datos del login: \n\tcorreo: " + loginToken.getPrincipal().toString());
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
            String jwt = jwtUtil.createToken(loginDto.getEmail());

            // Se construye la respuesta con el token y el ID del usuario
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("userId", user.getIdUsuario());
            response.put("role", user.getRole());

            System.out.println("Se generó con éxito");
            System.out.println("token: " + jwt);
            System.out.println("userId: " + user.getIdUsuario());
            System.out.println("role: " + user.getRole());

            return ResponseEntity.ok(response); // Devolver el token y el ID del usuario

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
        // Se verifica si el usuario ya existe
        UsuarioEntity foundUser = usuarioRepository.findByEmail(registerDto.getEmail());
        if (foundUser != null) {
            // 409 La solicitud no se puede completar por un conflicto (ya existe)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya se encuentra registrado.\n");
        }
        // Se puede implementar la validación de datos aquí y dar error 400 en caso de no cumplirse

        try {
            // Asignar rol por defecto si no se proporciona
            String rol = (registerDto.getRole() == null || registerDto.getRole().isEmpty()) ? "CLIENTE" : registerDto.getRole();
            // Cifrar la contraseña
            String hashedPassword = passwordEncoder.encode(registerDto.getPassword());
            // Se crea un nuevo usuario
            UsuarioEntity newUser = new UsuarioEntity(
                    registerDto.getRut(),
                    registerDto.getNameParam(),
                    registerDto.getEmail(),
                    registerDto.getPhone(),
                    registerDto.getBirthdate(),
                    hashedPassword,
                    rol);

            usuarioRepository.save(newUser);
            // 201 Solicitud exitosa
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario registrado con éxito.\n");

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