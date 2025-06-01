<template>
  <v-container fluid class="login-container pa-0 fill-height">
    <v-row no-gutters style="min-height: 100vh;">
      <!-- Panel Izquierdo -->
      <v-col cols="12" md="6" class="gradient-background d-flex align-center">
        <v-container>
          <h1 class="text-h2 font-weight-bold text-white mb-4">
            ¡Bienvenido de Nuevo!
          </h1>
          <h2 class="text-h5 font-weight-regular text-white">
            Inicia sesión para continuar.
          </h2>
        </v-container>
      </v-col>

      <!-- Panel Derecho -->
      <v-col cols="12" md="6" class="d-flex align-center justify-center">
        <v-card
          class="login-card mx-auto"
          max-width="500"
          elevation="8"
          rounded="lg"
        >
          <v-card-item class="pb-0">
            <v-card-title class="text-h4 font-weight-bold text-primary mb-2 text-center">
              Inicio de Sesión
            </v-card-title>
            <v-card-subtitle class="text-body-1 text-medium-emphasis text-center">
              Ingresa tus credenciales para acceder
            </v-card-subtitle>
          </v-card-item>

          <v-card-text class="pt-4">
            <v-form @submit.prevent="handleLogin" v-model="isFormValid">
              <v-text-field
                v-model="nickname"
                label="Nombre de Usuario"
                prepend-inner-icon="mdi-account"
                variant="outlined"
                :rules="[v => !!v || 'El nombre de usuario es requerido']"
                required
              ></v-text-field>

              <v-text-field
                v-model="contrasenia"
                label="Contraseña"
                prepend-inner-icon="mdi-lock"
                :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                :type="showPassword ? 'text' : 'password'"
                variant="outlined"
                @click:append-inner="showPassword = !showPassword"
                :rules="[v => !!v || 'La contraseña es requerida']"
                required
              ></v-text-field>

              <v-alert
                v-if="errorMessage"
                type="error"
                variant="tonal"
                class="mb-4"
              >
                {{ errorMessage }}
              </v-alert>

              <v-btn
                type="submit"
                color="primary"
                size="large"
                block
                :loading="isLoading"
                :disabled="!isFormValid"
                class="mt-2"
              >
                Iniciar Sesión
              </v-btn>
            </v-form>

            <div class="text-center mt-6">
              <span class="text-medium-emphasis">¿No tienes una cuenta?</span>
              <v-btn
                variant="text"
                color="primary"
                class="ml-2"
                @click="$router.push('/register')"
              >
                Regístrate aquí
              </v-btn>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { loginUser } from "@/services/auth";

export default {
  name: "Login",
  data() {
    return {
      nickname: "",
      contrasenia: "",
      errorMessage: "",
      showPassword: false,
      isLoading: false,
      isFormValid: false
    };
  },
  methods: {
    async handleLogin() {
      if (!this.isFormValid) return;

      this.isLoading = true;
      this.errorMessage = "";

      const userData = {
        nickname: this.nickname,
        contrasenia: this.contrasenia
      };

      try {
        const result = await loginUser(userData);
        this.$router.push("/home");
      } catch (error) {
        console.error("Error al iniciar sesión:", error);
        if (error.response?.data?.message) {
          this.errorMessage = error.response.data.message;
        } else if (error.response?.status === 401) {
          this.errorMessage = "Credenciales incorrectas. Por favor, inténtelo de nuevo.";
        } else {
          this.errorMessage = "Error al conectar con el servidor. Por favor, inténtelo más tarde.";
        }
      } finally {
        this.isLoading = false;
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  background-color: #f0f2f5;
}

.gradient-background {
  position: relative;
  overflow: hidden;
  background: radial-gradient(
    circle,
    #558ffa,    /* Azul claro */
    #4a5ab9,    /* Azul medio */
    #3ca6a6,    /* Verde agua */
    #58338a,    /* Morado medio */
    #2c747e,    /* Verde azulado */
    #5481ff     /* Azul brillante */
  );
  background-size: 300% 300%;
  animation: gradient 25s ease infinite;
}

@keyframes gradient {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.gradient-background::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(
    circle at center,
    rgba(255, 255, 255, 0.15) 0%,
    transparent 70%
  );
  animation: shine 10s ease-in-out infinite;
}

@keyframes shine {
  0%, 100% {
    opacity: 0.7;
  }
  50% {
    opacity: 0.3;
  }
}

.login-card {
  width: 100%;
  max-width: 450px;
  margin: 2rem;
  padding: 1rem;
  backdrop-filter: blur(8px);
}

@media (max-width: 600px) {
  .login-card {
    margin: 1rem;
    padding: 0.5rem;
  }
}
</style>
