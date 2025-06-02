<script setup>
import { onMounted, ref } from 'vue'
import axios from 'axios';

const tareasHechasEnCadaSector = ref([]);

const getTareasHechas = async () => {
    try{
      const usuario = JSON.parse(localStorage.getItem("user"));
      
      const response = await axios.get("http://localhost:8000/api/tareas/porSector/" + usuario.id_usuario, {
        headers: {
          Authorization: `Bearer ${usuario.token}`,
        },
      });
      tareasHechasEnCadaSector.value = response.data;
    }
    catch(error){
      console.log(error);
    }
}

onMounted(() => {
  getTareasHechas();
})
</script>

<template>
  <v-container>
    <v-table>
      <thead>
        <tr>
          <th>Sector</th>
          <th>Tareas Hechas</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="tareaSector in tareasHechasEnCadaSector" :key="tareaSector.id_sector">
          <td>{{tareaSector.id_sector}}</td>
          <td>{{tareaSector.tareas_hechas}}</td>
        </tr>
      </tbody>
    </v-table>
  </v-container>
</template>

<script>
export default {
    name: "Query1",
};
</script>

<style>
table {
    width: 100%;
    margin-top: 1rem;
}
td,
th {
    border: 1px solid black;
}
</style>