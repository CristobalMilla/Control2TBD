<script setup>

import { ref, onMounted } from 'vue';
import axios from 'axios'
//Pregunta 8
const sectorMostCompleted = ref(null);
onMounted(async () => {
  // Pregunta 8
  const usuario = JSON.parse(localStorage.getItem("user"));
  const response = await axios.get('http://localhost:8000/api/tareas/SectorMostCompletedByUser/' + usuario.id_usuario, {
      headers: {
        Authorization: `Bearer ${usuario.token}`,
      },
    })
  sectorMostCompleted.value = response.data;
});
</script>



<template>
  <div>
    <v-card class="sector-completed-card pa-6">
      <v-card-title class="d-flex align-center">
        <v-icon size="28" color="success" class="mr-3">mdi-trophy</v-icon>
        <span class="text-h5">Sector cercano con Más Tareas Completadas</span>
      </v-card-title>
      <v-card-text v-if="sectorMostCompleted">
        <p>El ID del sector con más tareas completadas cerca de ti es: <strong>{{ sectorMostCompleted }}</strong></p>
      </v-card-text>
      <v-card-text v-else>
        <p>No se encontró un sector cercano con tareas completadas.</p>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "Question8",
  props: {
    sectorId: {
      type: Number,
      default: null,
    },
  },
};
</script>
