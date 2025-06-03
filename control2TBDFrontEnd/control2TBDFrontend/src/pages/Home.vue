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

        <!-- Se eliminaron los botones "Nueva Tarea" y "Ver Todas" -->

        <v-card class="recent-tasks-section mt-8" elevation="2">
          <v-card-title class="d-flex align-center pa-6 bg-grey-lighten-4">
            <v-icon size="28" color="primary" class="mr-3">mdi-clipboard-text</v-icon>
            <span class="text-h5">Tus tareas</span>
          </v-card-title>

          <v-card-text class="pa-0">
            <v-list v-if="recentTasks.length > 0" class="py-2">
              <v-list-item
                v-for="task in recentTasks"
                :key="task.id_tarea"
                :title="task.titulo"
                :subtitle="task.descripcion"
                class="py-4 px-6"
              >
                <template v-slot:append>
                  <v-btn
                    icon="mdi-pencil"
                    variant="text"
                    color="primary"
                    size="large"
                    class="mr-2"
                    @click="editTask(task.id_tarea)"
                  ></v-btn>
                  <v-btn
                    icon="mdi-check-circle"
                    variant="text"
                    color="success"
                    size="large"
                    @click="completeTask(task.id_tarea)"
                  ></v-btn>
                </template>
              </v-list-item>
            </v-list>
            <v-card-text v-else class="text-center pa-12">
              <v-icon size="72" color="grey-lighten-1" class="mb-4">
                mdi-clipboard-text-outline
              </v-icon>
              <div class="text-grey text-h6">No hay tareas disponibles</div>
            </v-card-text>
          </v-card-text>
        </v-card>

        <!-- Card de la Pregunta 7 -->
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
        <!-- Card de la Pregunta 8 -->
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
        <!-- Card de la pregunta 9 -->
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

      </v-container>
    </v-main>
  </v-container>
</template>

<script>
import { logoutUser } from "@/services/auth";
import NotificationBadge from '@/components/NotificationBadge.vue';
// Pregunta 7
import Question7 from "@/components/QuestionCards/Question7.vue";
import { getAllTasksPerUserPerSector } from "@/api/tasks";
// Pregunta 8
import Question8 from "@/components/QuestionCards/Question8.vue";
import { getSectorMostCompletedByUser } from "@/api/tasks";
// Pregunta 9
import Question9 from "@/components/QuestionCards/Question9.vue";
import { getAverageCompletedDistance } from "@/api/tasks";
// Nuevo: obtener las tareas del usuario
import { getUserTasks } from "@/api/tasks";

export default {
  name: 'HomePage',
  components: {
    NotificationBadge,
    Question7,
    Question8,
    Question9,
  },
  data() {
    return {
      nickname: 'Usuario',
      pendingTasks: 0,
      completedTasks: 0,
      recentTasks: [],
      sectorTasks: [], // Inicializado para evitar errores si Question7 se renderiza antes de que los datos estén listos
      sectorMostCompleted: null,
      averageCompletedDistance: null,
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
      console.log('Completar tarea ID:', id);
      // Considera recargar los datos o actualizar el estado localmente
      // await this.fetchDashboardData(); 
    },
    async fetchDashboardData() {
      const userString = localStorage.getItem("user");
      if (!userString) {
        console.error('Usuario no encontrado en localStorage.');
        this.recentTasks = [];
        this.pendingTasks = 0;
        this.completedTasks = 0;
        return;
      }

      const user = JSON.parse(userString);
      if (!user || typeof user.id_usuario === 'undefined') {
        console.error('ID de usuario (id_usuario) no encontrado en el objeto de usuario de localStorage.');
        this.recentTasks = [];
        this.pendingTasks = 0;
        this.completedTasks = 0;
        return;
      }
      
      const userId = user.id_usuario;

      // Cargar tareas del usuario (primordial)
      try {
        const userTasks = await getUserTasks(userId);
        if (Array.isArray(userTasks)) {
          this.recentTasks = userTasks;
          this.pendingTasks = userTasks.filter(task => task.estado && task.estado.toLowerCase() === 'pendiente').length;
          this.completedTasks = userTasks.filter(task => task.estado && task.estado.toLowerCase() === 'completada').length;
        } else {
          console.warn('getUserTasks no devolvió un array:', userTasks);
          this.recentTasks = [];
          this.pendingTasks = 0;
          this.completedTasks = 0;
        }
      } catch (error) {
        console.error('Error al obtener las tareas del usuario:', error);
        this.recentTasks = []; // Si falla la carga de tareas, se vacían
        this.pendingTasks = 0;
        this.completedTasks = 0;
        // Puedes decidir si continuar con las otras llamadas o retornar aquí
      }
      
      // Cargar datos adicionales del dashboard.
      // Los errores aquí no deberían limpiar this.recentTasks.
      try {
        const tasksPerSector = await getAllTasksPerUserPerSector(); 
        this.sectorTasks = tasksPerSector;
      } catch (error) {
        console.error('Error al obtener tareas por sector:', error);
        this.sectorTasks = []; // Resetear solo esta parte o manejar como prefieras
      }
      
      try {
        this.sectorMostCompleted = await getSectorMostCompletedByUser(userId);
      } catch (error) {
        console.error('Error al obtener sector con más tareas completadas (sospechado):', error);
        this.sectorMostCompleted = null; // Resetear solo esta parte
      }
      
      try {
        this.averageCompletedDistance = await getAverageCompletedDistance(userId);
      } catch (error) {
        console.error('Error al obtener promedio de distancia:', error);
        this.averageCompletedDistance = null; // Resetear solo esta parte
      }
    }
  },
  mounted() {
    const userString = localStorage.getItem("user");
    if (userString) {
      const storedUser = JSON.parse(userString);
      if (storedUser && storedUser.nickname) {
        this.nickname = storedUser.nickname;
      }
    } else {
      // Opcional: Redirigir a login si no hay usuario en localStorage al montar
      // this.$router.push('/login');
    }
    this.fetchDashboardData();
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