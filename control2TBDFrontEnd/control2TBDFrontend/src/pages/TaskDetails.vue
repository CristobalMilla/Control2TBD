<template>
  <div class="task-details">
    <h1>Lista de Tareas</h1>

    <TaskFilter
        v-if="!loading && tasks.length > 0"
        :tasks="tasks"
        @filtered-tasks="handleFilteredTasks"
        ref="taskFilter"
    />

    <div v-if="loading" class="loading">
      <p>Cargando tareas...</p>
    </div>

    <div v-else-if="tasks.length === 0" class="no-tasks">
      <p>No hay tareas disponibles</p>
    </div>

    <div v-else class="task-list">
      <TaskCard
          v-for="task in tasks"
          :key="task.id_tarea"
          :tarea="task"
          @task-updated="handleTaskUpdated"
          @task-completed="handleTaskCompleted"
          @task-deleted="handleTaskDeleted"
      />
    </div>
  </div>
</template>

<script>
import TaskCard from "@/components/TaskCard.vue";
import { getTasks } from '../api/tasks';
import TaskFilter from "@/components/TaskFilter.vue";

export default {
  name: "TaskDetails",
  components: {
    TaskFilter,
    TaskCard
  },
  data() {
    return {
      tasks: [],
      loading: false

    }
  },
  mounted() {
    this.fetchTasks()
  },

  methods: {
    async fetchTasks() {
      this.loading = true
      try {
        const response = await getTasks()
        this.tasks = response.data
      } catch (error) {
        console.error('Error fetching tasks:', error)
        alert('Error al cargar las tareas')
      } finally {
        this.loading = false
      }
    },

    handleTaskUpdated(task) {
      console.log('Tarea actualizada:', task)
    },

    handleTaskCompleted(taskId) {
      console.log('Tarea completada:', taskId)
    },
    handleFilteredTasks(filtered) {
      this.filteredTasks = filtered
    },

    handleTaskDeleted(taskId) {
      console.log('Tarea eliminada:', taskId)
      // Remover de la lista
      this.tasks = this.tasks.filter(task => task.id_tarea !== taskId)
    }
  }
}
</script>

<style scoped>
.task-details {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading, .no-tasks {
  text-align: center;
  padding: 40px;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin: 20px 0;
}

.loading {
  background-color: #f8f9fa;
  color: #6c757d;
}

.no-tasks {
  background-color: #fff3cd;
  color: #856404;
  border-color: #ffeaa7;
}

.task-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
</style>
