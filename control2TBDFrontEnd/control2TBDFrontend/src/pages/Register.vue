<template>
  <v-container fluid class="register-container pa-0 fill-height">
    <v-row no-gutters style="min-height: 100vh;">
      <!-- Panel Izquierdo -->
      <v-col cols="12" md="6" class="gradient-background d-flex align-center">
        <v-container>
          <h1 class="text-h2 font-weight-bold text-white mb-4">
            ¡Bienvenido!
          </h1>
          <h2 class="text-h5 font-weight-regular text-white">
            ¡Regístrate para administrar tus tareas!
          </h2>
        </v-container>
      </v-col>

      <!-- Panel Derecho -->
      <v-col cols="12" md="6" class="d-flex align-center justify-center">
        <v-card class="register-card mx-auto" max-width="600" elevation="8" rounded="lg">
          <v-card-item class="pb-0">
            <v-card-title class="text-h4 font-weight-bold text-primary mb-2 text-center">
              Crear una cuenta
            </v-card-title>
            <v-card-subtitle class="text-body-1 text-medium-emphasis text-center">
              Complete los campos para el registro
            </v-card-subtitle>
          </v-card-item>

          <v-card-text class="pt-4">
            <v-form @submit.prevent="handleRegister" v-model="isFormValid">
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

              <v-label class="text-subtitle-1 font-weight-medium mb-2">Ubicación:</v-label>
              <div id="map" class="rounded mb-2"></div>
              <v-label v-if="latitude && longitude" class="text-caption text-medium-emphasis">
                Latitud: {{ latitude.toFixed(6) }}, Longitud: {{ longitude.toFixed(6) }}
              </v-label>
              <v-label v-else class="text-caption text-medium-emphasis">
                Por favor, haga clic en el mapa para seleccionar su ubicación.
              </v-label>

              <v-alert
                v-if="error"
                type="error"
                variant="tonal"
                class="mt-4 mb-4"
              >
                {{ error }}
              </v-alert>

              <v-btn
                type="submit"
                color="primary"
                size="large"
                block
                :loading="loading"
                :disabled="!isFormValid || !latitude || !longitude"
                class="mt-4"
              >
                Registrar
              </v-btn>

              <div class="text-center mt-6">
                <span class="text-medium-emphasis">¿Ya tienes una cuenta?</span>
                <v-btn
                  variant="text"
                  color="primary"
                  class="ml-2"
                  @click="$router.push('/login')"
                >
                  Inicia sesión aquí
                </v-btn>
              </div>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { registerUser } from "@/services/auth";

export default {
  name: "Register",
  data() {
    return {
      nickname: "",
      contrasenia: "",
      latitude: null,
      longitude: null,
      map: null,
      marker: null,
      rol: "",
      error: "",
      loading: false,
      showPassword: false,
      isFormValid: false
    };
  },

  mounted() {
    if (typeof L !== "undefined") {
      this.initLeafletMap();
    } else {
      console.error("Leaflet is not loaded. Make sure it's included in your HTML or imported correctly.");
    }
  },

  methods: {
    initLeafletMap() {
      const initialPosition = [-33.4489, -70.6693]; // Posicion inicial = Santiago, Chile
      this.map = L.map("map").setView(initialPosition, 12);

      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      }).addTo(this.map);

      this.map.on("click", (event) => {
        const latlng = event.latlng;
        this.latitude = latlng.lat;
        this.longitude = latlng.lng;

        if (this.marker) {
          this.marker.setLatLng(latlng);
        } else {
          this.marker = L.marker(latlng).addTo(this.map);
        }
      });
    },

    async handleRegister() {
      if (!this.isFormValid) return;
      if (!this.latitude || !this.longitude) {
        this.error = "Por favor, seleccione su ubicación en el mapa.";
        return;
      }

      this.loading = true;
      this.error = "";

      const userData = {
        nickname: this.nickname,
        contrasenia: this.contrasenia,
        lat: this.latitude,
        lng: this.longitude,
        rol: this.rol || "",
      };

      try {
        await registerUser(userData);
        this.$router.push("/login");
      } catch (err) {
        console.error("Error en el registro:", err);
        if (err.response?.status === 409) {
          this.error = "El nickname ya está en uso.";
        } else {
          this.error = "Error al registrar usuario. Intenta de nuevo.";
        }
      } finally {
        this.loading = false;
      }
    },
  },

  beforeUnmount() {
    if (this.map) {
      this.map.remove();
      this.map = null;
    }
  },
};
</script>

<style scoped>
.register-container {
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

.register-card {
  width: 100%;
  margin: 2rem;
  padding: 1rem;
  backdrop-filter: blur(8px);
}

#map {
  height: 250px;
  width: 100%;
  border: 1px solid rgba(0, 0, 0, 0.12);
  border-radius: 8px;
  margin-bottom: 8px;
}

@media (max-width: 600px) {
  .register-card {
    margin: 1rem;
    padding: 0.5rem;
  }
  
  #map {
    height: 200px;
  }
}
</style>
