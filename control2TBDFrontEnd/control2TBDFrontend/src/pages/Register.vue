<template>
  <div>
    <h1>Registro de Usuario</h1>
    <form @submit.prevent="registerUser">
      <label for="nickname">Nombre de Usuario (Nickname):</label>
      <input id="nickname" type="text" v.model="nickname" placeholder="Ingrese su nickname" required />

      <label for="password">Contraseña:</label>
      <input id="password" type="password" v.model="contrasenia" placeholder="Ingrese su contraseña" required />

      <label>Ubicación:</label>
      <div id="map" style="height: 400px; width: 100%;"></div>
      <p v-if="latitude && longitude">
        Latitud: {{ latitude.toFixed(6) }}, Longitud: {{ longitude.toFixed(6) }}
      </p>
      <p v-else>
        Por favor, haga clic en el mapa para seleccionar su ubicación.
      </p>

      <button type="submit">Registrar</button>
    </form>
  </div>
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
form {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 400px;
  margin: auto;
}
label {
  font-weight: bold;
}
input, button {
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
button {
  background-color: #4CAF50;
  color: white;
  cursor: pointer;
}
button:hover {
  background-color: #45a049;
}
#map {
  border: 1px solid #ccc;
  border-radius: 4px;
  z-index: 0;
}
</style>
