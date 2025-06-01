<template>
  <div class="task-details">
    <div class="header-section">
      <h1>Lista de Tareas</h1>
      <button class="btn-create-task" @click="openCreateModal">
        <span class="plus-icon">+</span>
        Crear Tarea
      </button>
    </div>


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

    <div v-else-if="filteredTasks && filteredTasks.length === 0" class="no-filtered-tasks">
      <p>No se encontraron tareas que coincidan con los filtros aplicados</p>
      <button @click="clearAllFilters" class="btn-clear-filters">
        Limpiar Filtros
      </button>
    </div>


    <div v-else class="task-list">
      <TaskCard
          v-for="task in displayTasks"
          :key="task.id_tarea"
          :tarea="task"
          @task-updated="handleTaskUpdated"
          @task-completed="handleTaskCompleted"
          @task-deleted="handleTaskDeleted"
      />
    </div>

    <TaskCreateModal
        :is-visible="showCreateModal"
        @close="closeCreateModal"
        @task-created="handleTaskCreated"
    />
  </div>

</template>

<script>
import TaskCard from "@/components/TaskCard.vue";
import { getTasks } from '../api/tasks';
import TaskFilter from "@/components/TaskFilter.vue";
import TaskCreateModal from "@/components/TaskCreateModal.vue";

export default {
  name: "TaskDetails",
  components: {
    TaskCreateModal,
    TaskFilter,
    TaskCard
  },
  data() {
    return {
      tasks: [],
      filteredTasks: [],
      loading: false,
      showCreateModal: false

    }
  },
  mounted() {
    this.fetchTasks()
  },
  computed: {
    displayTasks() {
      // Mostrar tareas filtradas si existen, sino mostrar todas
      return this.filteredTasks.length > 0 ? this.filteredTasks : this.tasks
    }
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
    openCreateModal() {
      this.showCreateModal = true
    },

    closeCreateModal() {
      this.showCreateModal = false
    },
    async handleTaskCreated(newTask) {
      console.log('Nueva tarea creada:', newTask)

      // Recargar todas las tareas para obtener la lista actualizada
      await this.fetchTasks()

      if (this.$refs.taskFilter) {
        this.$refs.taskFilter.clearFilters()
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
    },
    clearAllFilters() {
      if (this.$refs.taskFilter) {
        this.$refs.taskFilter.clearFilters()
      }
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
