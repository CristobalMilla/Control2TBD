<template>
  <v-container fluid class="dashboard pa-0">
    <v-app-bar color="primary" dark elevation="2">
      <v-app-bar-title class="d-flex align-center">
        <v-btn variant="text" @click="$router.push('/dashboard')" class="d-flex align-center">
          <v-icon size="24" class="mr-2">mdi-view-dashboard</v-icon>
          Dashboard
        </v-btn>
      </v-app-bar-title>
      <v-spacer></v-spacer>
      <v-btn variant="text" @click="goToTaskDetails" class="mr-2" prepend-icon="mdi-clipboard-text">
        Detalles de Tareas
      </v-btn>
      <notification-badge class="mr-2" />
      <v-btn @click="logout" variant="text" prepend-icon="mdi-logout">
        Cerrar Sesión
      </v-btn>
    </v-app-bar>

    <v-main>
      <v-container class="py-8">
        <div class="welcome-section">
          <h2 class="d-flex align-center mb-2">
            <v-icon size="32" color="primary" class="mr-2">mdi-account</v-icon>
            Bienvenido, {{ nickname }}
          </h2>
          <p class="date d-flex align-center text-grey">
            <v-icon size="20" class="mr-2">mdi-calendar</v-icon>
            {{ currentDate }}
          </p>
        </div>

        <v-row class="mt-6">
          <v-col cols="12" sm="6" md="4">
            <v-card class="stat-card" elevation="2">
              <v-card-item class="pa-6">
                <div class="d-flex align-center">
                  <v-avatar color="primary" size="64" class="mr-6">
                    <v-icon size="32" color="white">mdi-clock-outline</v-icon>
                  </v-avatar>
                  <div>
                    <div class="text-subtitle-1 text-grey mb-1">Tareas Pendientes</div>
                    <div class="text-h3">{{ pendingTasks }}</div>
                  </div>
                </div>
              </v-card-item>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6" md="4">
            <v-card class="stat-card" elevation="2">
              <v-card-item class="pa-6">
                <div class="d-flex align-center">
                  <v-avatar color="success" size="64" class="mr-6">
                    <v-icon size="32" color="white">mdi-check-circle</v-icon>
                  </v-avatar>
                  <div>
                    <div class="text-subtitle-1 text-grey mb-1">Completadas</div>
                    <div class="text-h3">{{ completedTasks }}</div>
                  </div>
                </div>
              </v-card-item>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6" md="4">
            <v-card class="stat-card" elevation="2">
              <v-card-item class="pa-6">
                <div class="d-flex align-center">
                  <v-avatar color="info" size="64" class="mr-6">
                    <v-icon size="32" color="white">mdi-chart-box</v-icon>
                  </v-avatar>
                  <div>
                    <div class="text-subtitle-1 text-grey mb-1">Total</div>
                    <div class="text-h3">{{ totalTasks }}</div>
                  </div>
                </div>
              </v-card-item>
            </v-card>
          </v-col>
        </v-row>

        <div class="actions-section my-8">
          <v-btn
            color="primary"
            size="x-large"
            prepend-icon="mdi-plus-circle"
            class="px-6"
            @click="createTask"
          >
            Nueva Tarea
          </v-btn>
          <v-btn
            color="secondary"
            size="x-large"
            variant="outlined"
            prepend-icon="mdi-format-list-bulleted"
            class="px-6"
            @click="viewAllTasks"
          >
            Ver Todas
          </v-btn>
        </div>

        <v-card class="recent-tasks-section" elevation="2">
          <v-card-title class="d-flex align-center pa-6 bg-grey-lighten-4">
            <v-icon size="28" color="primary" class="mr-3">mdi-clipboard-text</v-icon>
            <span class="text-h5">Tareas Recientes</span>
          </v-card-title>

          <v-card-text class="pa-0">
            <v-list v-if="recentTasks.length > 0" class="py-2">
              <v-list-item
                v-for="task in recentTasks"
                :key="task.id"
                :title="task.title"
                :subtitle="task.description"
                class="py-4 px-6"
              >
                <template v-slot:append>
                  <v-btn
                    icon="mdi-pencil"
                    variant="text"
                    color="primary"
                    size="large"
                    class="mr-2"
                    @click="editTask(task.id)"
                  ></v-btn>
                  <v-btn
                    icon="mdi-check-circle"
                    variant="text"
                    color="success"
                    size="large"
                    @click="completeTask(task.id)"
                  ></v-btn>
                </template>
              </v-list-item>
            </v-list>
            <v-card-text v-else class="text-center pa-12">
              <v-icon
                size="72"
                color="grey-lighten-1"
                class="mb-4"
              >mdi-clipboard-text-outline</v-icon>
              <div class="text-grey text-h6">No hay tareas recientes</div>
            </v-card-text>
          </v-card-text>
        </v-card>
      </v-container>

      <!--
      <v-container>
        <v-card class="sector-tasks-section mt-8" elevation="2">
          <v-card-title class="d-flex align-center pa-6 bg-grey-lighten-4">
            <v-icon size="28" color="primary" class="mr-3">mdi-format-list-bulleted</v-icon>
            <span class="text-h5">Tareas por Sector</span>
          </v-card-title>
          <v-card-text>
            <Question7 :tasks="sectorTasks" />
          </v-card-text>
        </v-card>
      </v-container>
      -->
      <!-- Card de la Pregunta 8 
      <v-container>
        <v-card class="mt-8">
          <v-card-title>
            <span class="text-h5">Sector con Más Tareas Completadas</span>
          </v-card-title>
          <v-card-text>
            <Question8 :sectorId="sectorMostCompleted" />
          </v-card-text>
        </v-card>
      </v-container>
      -->
      <!-- Card de la pregunta 9 
      <v-container>
        <v-card class="mt-8">
          <v-card-title>
            <span class="text-h5">Promedio de Distancia a Tareas Completadas</span>
          </v-card-title>
          <v-card-text>
            <Question9 :averageDistance="averageCompletedDistance" />
          </v-card-text>
        </v-card>
      </v-container>
      -->

    </v-main>
  </v-container>
</template>

<script>
import { logoutUser } from "@/services/auth";
import NotificationBadge from '@/components/NotificationBadge.vue';

//Pregunta 7
import Question7 from "@/components/QuestionCards/Question7.vue";
import { getAllTasksPerUserPerSector } from "@/api/tasks";
//Pregunta 8
import Question8 from "@/components/QuestionCards/Question8.vue";
import { getSectorMostCompletedByUser } from "@/api/tasks";
//Pregunta 9
import Question9 from "@/components/QuestionCards/Question9.vue";
import { getAverageCompletedDistance } from "@/api/tasks";

export default {
  name: 'HomePage',
  components: {
    NotificationBadge,
  },
  data() {
    return {
      nickname: 'Usuario',
      pendingTasks: 0,
      completedTasks: 0,
      recentTasks: [],
    }
  },
  computed: {
    currentDate() {
      return new Date().toLocaleDateString('es-ES', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    },
    totalTasks() {
      return this.pendingTasks + this.completedTasks
    }
  },
  methods: {
    logout() {
      logoutUser();
      this.$router.push('/login')
    },
    goToTaskDetails() {
      this.$router.push('/taskdetails')
    },
    createTask() {
      this.$router.push('/create-task')
    },
    viewAllTasks() {
      this.$router.push('/tasks')
    },
    editTask(id) {
      this.$router.push(`/edit-task/${id}`)
    },
    async completeTask(id) {
      // Implementar lógica de completar tarea
    },
    async fetchDashboardData() {
      try {
        const userId = JSON.parse(localStorage.getItem("user")).id;

        // Pregunta 7
        //const tasksPerSector = await getAllTasksPerUserPerSector();
        //this.sectorTasks = tasksPerSector;
        //Pregunta 8
        // Obtener el sector con más tareas completadas
        //this.sectorMostCompleted = await getSectorMostCompletedByUser(userId);
        //Pregunta 9
        // Obtener el promedio de distancia
        //this.averageCompletedDistance = await getAverageCompletedDistance(userId);

        // Aquí irá la llamada a tu API
        // Por ahora usamos datos de ejemplo
        this.pendingTasks = 5
        this.completedTasks = 3
        this.recentTasks = [
          { id: 1, title: 'Tarea de ejemplo', description: 'Esta es una tarea de ejemplo' }
        ]
      } catch (error) {
        console.error('Error al obtener datos:', error)
      }
    }
  },
  mounted() {
    this.fetchDashboardData()
    const storedUser = JSON.parse(localStorage.getItem("user"))
    if (storedUser && storedUser.nickname) {
      this.nickname = storedUser.nickname
    }
  }
}
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background: #f0f2f5;
}

.welcome-section h2 {
  font-size: 2rem;
  font-weight: 600;
  margin: 0;
}

.date {
  font-size: 1.1rem;
}

.actions-section {
  display: flex;
  gap: 1.5rem;
}

.stat-card {
  transition: transform 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
}

@media (max-width: 600px) {
  .actions-section {
    flex-direction: column;
  }

  .actions-section .v-btn {
    width: 100%;
  }
}
</style>