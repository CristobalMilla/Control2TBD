<template>
  <main class="main-container-login">
    <div class="moving-gradient-login">
      <div class="split-container-login">
        <!-- Mitad Izquierda -->
        <div class="left-container-login">
          <h1>¡Bienvenido de Nuevo!</h1>
          <h2>Inicia sesión para continuar.</h2>
        </div>
        
        <!-- Mitad Derecha -->
        <div class="right-container-login">
          <div class="content-login">
            <div class="header-login">Inicio de Sesión</div>
            <div class="headerDescription-login">Ingresa tus credenciales para acceder</div>

            <form @submit.prevent="handleLogin" class="inputContainer-login">
              <label for="username">Nombre de Usuario:</label>
              <input id="username" type="text" v-model="username" placeholder="Tu nombre de usuario" required />

              <label for="password">Contraseña:</label>
              <input id="password" type="password" v-model="password" placeholder="Tu contraseña" required />
              
              <p v-if="errorMessage" class="error-message-login">{{ errorMessage }}</p>

              <button type="submit" class="sessionButton-login">Iniciar Sesión</button>
            </form>
            <div class="register-link-login">
              <p>¿No tienes una cuenta? <router-link to="/register">Regístrate aquí</router-link></p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import axios from 'axios'; // Asegúrate de tener axios instalado

export default {
  name: "Login",
  data() {
    return {
      username: "",
      password: "",
      errorMessage: "",
    };
  },
  methods: {
    async handleLogin() {
      this.errorMessage = ""; // Limpiar mensajes de error previos
      if (!this.username || !this.password) {
        this.errorMessage = "Por favor, complete todos los campos.";
        return;
      }

      try {
        const response = await axios.post('http://localhost:8080/api/auth/login', {
          nickname: this.username, // o 'username' si el backend espera eso
          contrasenia: this.password, // o 'password' si el backend espera eso
        });

        if (response.data && response.data.token) {
          localStorage.setItem('jwtToken', response.data.token);
          // Opcional: guardar información del usuario si es necesario
          // localStorage.setItem('userData', JSON.stringify(response.data.user)); 
          this.$router.push('/dashboard');
        } else {
          // Si el backend no devuelve un token pero la respuesta es 2xx
          this.errorMessage = "Respuesta inesperada del servidor.";
        }
      } catch (error) {
        console.error("Error al iniciar sesión:", error);
        if (error.response && error.response.data && error.response.data.message) {
          this.errorMessage = error.response.data.message;
        } else if (error.response && error.response.status === 401) {
          this.errorMessage = "Credenciales incorrectas. Por favor, inténtelo de nuevo.";
        } else {
          this.errorMessage = "Error al conectar con el servidor. Por favor, inténtelo más tarde.";
        }
      }
    },
  },
};
</script>

<style scoped>
/* Estilos generales */
.main-container-login {
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100vh;
    margin: 0;
    font-family: "Arial", sans-serif;
}

/* Contenedor dividido */
.split-container-login {
    display: flex;
    flex:1;
    width: 100%;
    height: 100%;
}

/* Mitad izquierda */
.left-container-login {
  height: 100%;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  padding: 2rem 4rem;
  background-color: transparent;
  box-sizing: border-box;
}

.left-container-login h1 {
  font-size: clamp(2.5rem, 8vw, 4.5rem); /* Ajustado para login */
  color: #ffffff;
  text-align:left;
  margin-bottom: 1rem;
}

.left-container-login h2 {
  font-size: clamp(1rem, 4vw, 1.8rem); /* Ajustado para login */
  font-style: italic;
  color: #ffffff;
  text-align:left;
}

/* Mitad derecha */
.right-container-login {
  flex: 1;
  display: flex;
  height: 100%;
  justify-content: center;
  align-items: center;
  background-color: transparent;
  padding: 1rem;
  box-sizing: border-box;
  overflow-y: auto;
}

.content-login {
    background: #ffffff;
    padding: 2rem 2.5rem; /* Ajustado padding */
    border-radius: 12px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    text-align: center;
    width: 100%;
    max-width: 480px; /* Ajustado max-width */
    box-sizing: border-box;
}

/* Encabezado */
.header-login {
    font-size: 1.8rem; /* Ajustado */
    font-weight: bold;
    margin-bottom: 0.5rem; /* Ajustado */
    color: #143e97;
}

.headerDescription-login {
    font-size: 1rem; /* Ajustado */
    color: #3c57af;
    margin-bottom: 1.5rem; /* Ajustado */
}

/* Inputs y botones */
.inputContainer-login {
    display: flex;
    flex-direction: column;
    gap: 1rem; /* Ajustado gap */
}

.inputContainer-login label {
  font-weight: bold;
  text-align: left;
  font-size: 0.9rem;
  color: #333;
  margin-bottom: -0.25rem;
}

.inputContainer-login input[type="text"],
.inputContainer-login input[type="password"] {
    padding: 0.85rem; /* Ajustado */
    border: 1px solid #ffd54f;
    border-radius: 8px;
    font-size: 1rem;
    box-sizing: border-box;
    width: 100%;
}

.inputContainer-login input:focus {
    outline: none;
    border-color: #ffc107;
    box-shadow: 0 0 0 2px rgba(255, 193, 7, 0.2);
}

.sessionButton-login {
    background: #5863f1;
    color: #ffffff;
    border: none;
    padding: 0.85rem; /* Ajustado */
    border-radius: 8px;
    font-size: 1rem;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s ease;
    margin-top: 0.75rem; /* Ajustado */
}

.sessionButton-login:hover {
  background: #5f74eb;
}

.error-message-login {
  color: red;
  font-size: 0.85rem;
  margin-top: 0.5rem;
  margin-bottom: 0.25rem;
  text-align: left;
}

.register-link-login {
  margin-top: 1.5rem;
  font-size: 0.9rem;
}

.register-link-login a {
  color: #5863f1;
  text-decoration: none;
  font-weight: bold;
}

.register-link-login a:hover {
  text-decoration: underline;
}


@keyframes gradient-login {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.moving-gradient-login {
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, #558ffa, #2c747e, #442fff, #58338a, #5481ff, #532e6b, #491b57);
  background-size: 400% 400%;
  animation: gradient-login 15s ease infinite;
  display: flex; 
}

.inputContainer-login > * {
  position: relative;
  z-index: 1;
}
</style>
