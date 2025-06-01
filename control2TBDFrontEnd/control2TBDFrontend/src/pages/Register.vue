<template>
  <main class="main-container-register">
    <div class="moving-gradient-register">
      <div class="split-container-register">
        <!-- Mitad Izquierda -->
        <div class="left-container-register">
          <h1>¡Bienvenido!</h1>
          <h2>¡Regístrate para administrar tus tareas!</h2>
        </div>
        
        <!-- Mitad Derecha -->
        <div class="right-container-register">
          <div class="content-register">
            <!-- You can add a logo here if you have one -->
            <!-- <img class="image-register" src="path/to/your/logo.png"> -->
            <div class="header-register">Crear una cuenta</div>
            <div class="headerDescription-register">Complete los campos para el registro</div>

            <form @submit.prevent="registerUser" class="inputContainer-register">
              <label for="nickname">Nombre de Usuario (Nickname):</label>
              <input id="nickname" type="text" v-model="nickname" placeholder="Ingrese su nickname" required />

              <label for="password">Contraseña:</label>
              <input id="password" type="password" v-model="contrasenia" placeholder="Ingrese su contraseña" required />

              <label>Ubicación:</label>
              <div id="map" style="height: 250px; width: 100%;"></div> <!-- Adjusted map height -->
              <p v-if="latitude && longitude" class="location-display-register">
                Latitud: {{ latitude.toFixed(6) }}, Longitud: {{ longitude.toFixed(6) }}
              </p>
              <p v-else class="location-display-register">
                Por favor, haga clic en el mapa para seleccionar su ubicación.
              </p>

              <button type="submit" class="sessionButton-register">Registrar</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
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
      rol: "CLIENTE", // Rol fijo por mientras se implementa la lógica de roles
    };
  },
  mounted() {
    if (typeof L !== 'undefined') {
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
    async registerUser() {
      if (!this.nickname || !this.contrasenia) {
        alert("Por favor, complete el nickname y la contraseña.");
        return;
      }
      if (this.latitude === null || this.longitude === null) {
        alert("Por favor, seleccione su ubicación en el mapa.");
        return;
      }

      const userData = {
        nickname: this.nickname,
        contrasenia: this.contrasenia,
        ubicacion: {
          type: "Point",
          coordinates: [this.longitude, this.latitude], 
        },
        rol: this.rol,
      };

      console.log("Datos a enviar:", JSON.stringify(userData, null, 2));
      // llamada al backend para registrar el usuario
      // try {
      //   const response = await axios.post('URL_DEL_BACKEND/register', userData);
      //   console.log('Usuario registrado:', response.data);
      //   // Redirigir o mostrar mensaje de éxito
      // } catch (error) {
      //   console.error('Error al registrar usuario:', error);
      //   alert('Error al registrar usuario. Por favor, inténtelo de nuevo.');
      // }
      alert("Revisa la consola para ver los datos que se enviarían.");
    },
  },
  beforeUnmount() {
    if (this.map) {
      this.map.remove();
      this.map = null;
    }
  }
};
</script>

<style scoped>
/* Adapted styles from the example, with '-register' suffix to avoid potential global conflicts if body styles were used */
/* Estilos generales */
.main-container-register {
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%; /* Adjusted from 104% to prevent overflow issues */
    height: 100vh; /* Adjusted from 103% */
    margin: 0; /* Removed negative margins */
    font-family: "Arial", sans-serif; /* Copied from example body */
}

/* Contenedor dividido */
.split-container-register {
    display: flex;
    flex:1;
    width: 100%;
    height: 100%;
}

/* Mitad izquierda */
.left-container-register {
  height: 100%;
  flex: 1;
  display: flex;
  flex-direction: column; /* To stack h1 and h2 */
  justify-content: center; /* Center vertically */
  align-items: flex-start; /* Align text to the left */
  padding: 2rem 4rem; /* Added more horizontal padding */
  background-color: transparent;
  box-sizing: border-box;
}

.left-container-register h1 {
  font-size: clamp(2.5rem, 8vw, 5rem); /* Responsive font size */
  color: #ffffff;
  text-align:left;
  margin-bottom: 1rem; /* Space between h1 and h2 */
}

.left-container-register h2 {
  font-size: clamp(1rem, 4vw, 2rem); /* Responsive font size */
  font-style: italic;
  color: #ffffff;
  text-align:left;
}

/* Mitad derecha */
.right-container-register {
  flex: 1;
  display: flex;
  height: 100%;
  justify-content: center; /* Center content horizontally */
  align-items: center; /* Center content vertically */
  background-color: transparent;
  padding: 1rem; /* Reduced padding */
  box-sizing: border-box;
  overflow-y: auto; /* Allow scrolling if content overflows */
}

.content-register {
    background: #ffffff;
    padding: 1.5rem 2rem; /* Adjusted padding */
    border-radius: 12px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    text-align: center;
    width: 100%;
    max-width: 600px; /* Increased from 450px, adjust as needed */
    box-sizing: border-box;
}

/* Imagen (optional) */
.image-register {
    max-width: 80px; /* Adjusted size */
    margin: 1rem auto 0.5rem; /* Adjusted margins */
}

/* Encabezado */
.header-register {
    font-size: 1.5rem; /* Kept from example */
    font-weight: bold;
    margin-bottom: 0.25rem; /* Reduced margin */
    color: #143e97; /* Kept from example */
}

.headerDescription-register {
    font-size: 0.9rem; /* Adjusted size */
    color: #3c57af; /* Kept from example */
    margin-bottom: 1rem; /* Adjusted margin */
}

/* Inputs y botones */
.inputContainer-register {
    display: flex;
    flex-direction: column;
    gap: 0.75rem; /* Adjusted gap */
}

.inputContainer-register label {
  font-weight: bold;
  text-align: left;
  font-size: 0.9rem;
  color: #333; /* Darker label color for better readability */
  margin-bottom: -0.25rem; /* Pull label closer to input */
}

.inputContainer-register input[type="text"],
.inputContainer-register input[type="password"] {
    padding: 0.75rem;
    border: 1px solid #ffd54f; /* Kept from example */
    border-radius: 8px;
    font-size: 1rem; /* Kept from example */
    box-sizing: border-box;
    width: 100%;
}

.inputContainer-register input:focus {
    outline: none;
    border-color: #ffc107; /* Kept from example */
    box-shadow: 0 0 0 2px rgba(255, 193, 7, 0.2); /* Kept from example */
}

.sessionButton-register {
    background: #5863f1; /* Kept from example */
    color: #ffffff;
    border: none;
    padding: 0.75rem;
    border-radius: 8px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s ease;
    margin-top: 0.5rem; /* Added some top margin */
}

.sessionButton-register:hover {
  background: #5f74eb; /* Kept from example */
}

/* Map specific styles */
#map {
  border: 1px solid #ccc;
  border-radius: 8px; /* Match input border radius */
  z-index: 0;
  background-color: #f9f9f9; /* Light background for map placeholder */
  /* The width is 100% of its parent (.inputContainer-register), 
     so it will expand with .content_register.
     The height is set inline in the template: style="height: 250px; width: 100%;"
     You can increase the 250px value there if you want the map to be taller as well. */
}

.location-display-register {
  font-size: 0.85rem;
  color: #555;
  margin-top: 0.5rem;
  margin-bottom: 0.25rem;
  text-align: left;
}

/* Moving Gradient Background */
@keyframes gradient-register {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.moving-gradient-register {
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, #558ffa, #2c747e, #442fff, #58338a, #5481ff, #532e6b, #491b57);
  background-size: 400% 400%;
  animation: gradient-register 15s ease infinite;
  display: flex; 
}

/* Ensure form elements are above the map if any overlap issues, though usually not needed with z-index 0 on map */
.inputContainer-register > * {
  position: relative;
  z-index: 1;
}
</style>
