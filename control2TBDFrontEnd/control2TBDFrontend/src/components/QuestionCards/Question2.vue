<script setup>
import { ref, watch, onMounted, nextTick } from 'vue'
import axios from 'axios'

const masCercana = ref({})
const sector = ref({})
const map = ref(null)

const getTareasHechas = async () => {
  try {
    const usuario = JSON.parse(localStorage.getItem("user"))

    const response = await axios.get("http://localhost:8000/api/tareas/masCercana/" + usuario.id_usuario, {
      headers: {
        Authorization: `Bearer ${usuario.token}`,
      },
    })

    masCercana.value = response.data
  } catch (error) {
    console.error("Error obteniendo tarea más cercana", error)
  }
}

const getSector = async (sectorId) => {
  try {
    const usuario = JSON.parse(localStorage.getItem("user"))

    const response = await axios.get("http://localhost:8000/api/sectores/" + sectorId, {
      headers: {
        Authorization: `Bearer ${usuario.token}`,
      },
    })

    sector.value = response.data
  } catch (error) {
    console.error("Error obteniendo sector", error)
  }
}

const initMap = async () => {
  await nextTick()

  const center = [-33.4489, -70.6693]
  map.value = L.map("map").setView(center, 15)

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: '&copy; OpenStreetMap contributors',
  }).addTo(map.value)

  if (sector.value.ubicacion) {
    const geoJson = JSON.parse(sector.value.ubicacion)
    L.geoJSON(geoJson).addTo(map.value)
  }
}

onMounted(async () => {
  await getTareasHechas()
})

watch(() => masCercana.value.id_sector, async (newVal) => {
  if (newVal) {
    await getSector(newVal)
    await initMap()
  }
})
</script>


    <template>
    <v-container>
        <v-row>
        <v-col cols="12" md="4">
            <div class="text-h4">
                {{masCercana.titulo}}
            </div>
            <div class="text-body-1">
                {{masCercana.descripcion}}
            </div>
            <div class="text-body-1">
                {{masCercana.fecha_vencimiento}}
            </div>
            <div class="text-body-1">
                Sector: {{masCercana.id_sector}}
            </div>
        </v-col> 
        <v-col cols="12" md="8">
            mapa: 
            <div id="map" class="rounded mb-2"></div>
        </v-col>
        </v-row>
    </v-container>
    </template>

    <script>
    export default {
        name: "Query2",
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
    #map {
    height: 400px;
    width: 100%;
    border: 1px solid black;
    }

    </style>