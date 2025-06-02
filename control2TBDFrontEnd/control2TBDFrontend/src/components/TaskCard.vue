<template>
  <div class="task-card" v-if="task && !loading">
    <div class="task-header">
      <h3 class="task-title">{{ editForm.titulo }}</h3>
      <div class="task-actions">
        <button @click="toggleEdit" class="btn-edit" :disabled="loading">
          {{ isEditing ? 'Cancelar' : 'Editar' }}
        </button>
        <button @click="markAsCompleted" class="btn-complete" :disabled="loading || editForm.estado === 'completada'">
          {{'Marcar Completada' }}
        </button>
        <button @click="deleteTask" class="btn-delete" :disabled="loading">
          Eliminar
        </button>
      </div>
    </div>

    <div class="task-content">
      <!-- Modo de visualización -->
      <div v-if="!isEditing" class="view-mode">
        <p class="task-description">{{ editForm.descripcion }}</p>
        <div class="task-details">
          <p><strong>Usuario:</strong> {{ usuarioNombre || 'Cargando...' }}</p>
          <p><strong>Fecha de Vencimiento:</strong> {{ formatDate(editForm.fecha_vencimiento) }}</p>
          <p><strong>Sector:</strong> {{ editForm.id_sector }}</p>
          <p><strong>Estado:</strong> {{ editForm.estado}}</p>
        </div>
      </div>

      <!-- Modo de edición -->
      <div v-else class="edit-mode">
        <div class="form-group">
          <label for="titulo">Título:</label>
          <input
              id="titulo"
              v-model="editForm.titulo"
              type="text"
              class="form-input"
              :disabled="loading"
          />
        </div>

        <div class="form-group">
          <label for="descripcion">Descripción:</label>
          <textarea
              id="descripcion"
              v-model="editForm.descripcion"
              class="form-textarea"
              :disabled="loading"
          ></textarea>
        </div>

        <div class="form-group">
          <label for="fecha">Fecha de Vencimiento:</label>
          <input
              id="fecha"
              v-model="editForm.fecha_vencimiento"
              type="date"
              class="form-input"
              :disabled="loading"
          />
        </div>

        <div class="form-group">
          <label for="sector">Sector:</label>
          <input
              id="sector"
              v-model="editForm.id_sector"
              type="number"
              class="form-input"
              :disabled="loading"
          />
        </div>

        <div class="edit-actions">
          <button @click="saveChanges" class="btn-save" :disabled="loading">
            {{ loading ? 'Guardando...' : 'Guardar' }}
          </button>
          <button @click="cancelEdit" class="btn-cancel" :disabled="loading">
            Cancelar
          </button>
        </div>
      </div>
    </div>
  </div>
  <div v-else-if="loading" class="loading">
    <p>Cargando tarea...</p>
  </div>

  <div v-else class="error">
    <p>Error al cargar la tarea o la tarea no está disponible.</p>
  </div>

</template>

<script>
import {deleteTask, markTaskAsDone, updateTask} from '../api/tasks'
import {getUserById} from '../api/usuarios.js'

export default {
  name: 'TaskCard',
  props: {
    tarea: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      task: null, // Se usará para el v-if y como copia local de la tarea
      isEditing: false,
      loading: true, // Iniciar como true, se pondrá en false cuando todo esté listo
      usuarioNombre: '',
      editForm: {
        id_tarea: '',
        titulo: '',
        descripcion: '',
        fecha_vencimiento: '',
        id_sector: '',
        id_usuario: '', // Asegúrate de que id_usuario esté en editForm si lo necesitas
        estado: '',
      }
    }
  },
  mounted() {
    if (this.tarea) {
      this.task = { ...this.tarea }; // Copia la tarea de la prop a la data local
      // Inicializa editForm con los datos de la tarea para el modo de visualización
      this.initializeEditForm();
      this.fetchUsuarioNombre().finally(() => {
        this.loading = false; // Terminar la carga después de obtener el nombre del usuario
      });
    } else {
      console.error("TaskCard: La prop 'tarea' es indefinida o nula.");
      this.loading = false; // No hay nada que cargar
    }
  },
  methods: {
    initializeEditForm() {
      if (this.task) {
        this.editForm = {
          id_tarea: this.task.id_tarea,
          titulo: this.task.titulo,
          descripcion: this.task.descripcion,
          fecha_vencimiento: this.task.fecha_vencimiento, // Mantener formato original para visualización
          id_sector: this.task.id_sector,
          id_usuario: this.task.id_usuario,
          estado: this.task.estado,
        };
      }
    },
    async fetchUsuarioNombre() {
      if (!this.task || !this.task.id_usuario) {
        this.usuarioNombre = 'N/A';
        // No es necesario establecer loading a false aquí si se hace en el finally del mounted
        return;
      }
      try {
        // getUserById devuelve directamente el objeto de datos del usuario
        const usuarioData = await getUserById(this.task.id_usuario);
        this.editForm.id_usuario = this.task.id_usuario; // Asegurar que id_usuario en editForm esté seteado
        if (usuarioData && typeof usuarioData.nickname !== 'undefined') {
          this.usuarioNombre = usuarioData.nickname;
        } else {
          this.usuarioNombre = 'Usuario desconocido';
          // console.warn('Datos de usuario o nickname no encontrados para ID:', this.task.id_usuario, usuarioData);
        }
      } catch (error) {
        console.error('Error fetching usuario:', error); // Este es el error que ves en la consola
        this.usuarioNombre = 'Error al cargar usuario';
      }
    },

    toggleEdit() {
      if (this.isEditing) {
        this.cancelEdit();
      } else {
        this.startEdit();
      }
    },

    startEdit() {
      this.isEditing = true;
      // Al editar, copia los datos actuales de 'task' y formatea la fecha para el input
      this.editForm = {
        ...this.task, // Copia todos los campos del estado actual de la tarea
        fecha_vencimiento: this.formatDateForInput(this.task.fecha_vencimiento),
      };
    },

    cancelEdit() {
      this.isEditing = false;
      // Restaura editForm al estado actual de 'this.task' (datos de visualización)
      this.initializeEditForm();
      // No es necesario llamar a fetchUsuarioNombre aquí a menos que el usuario pueda cambiar
    },

    async saveChanges() {
      this.loading = true; // Indicar proceso de guardado
      try {
        // El payload para updateTask debe coincidir con lo que espera la API.
        // editForm.fecha_vencimiento ya está formateada para el input (YYYY-MM-DD)
        const payload = {
            titulo: this.editForm.titulo,
            descripcion: this.editForm.descripcion,
            fecha_vencimiento: this.editForm.fecha_vencimiento, // Asegúrate que este formato sea el esperado por el backend
            id_sector: this.editForm.id_sector,
            // Incluye otros campos si son editables, ej: estado, id_usuario (si es editable)
        };
        const updatedTaskDataFromServer = await updateTask(this.task.id_tarea, payload);

        // Actualiza el estado local 'task' con la respuesta del servidor
        this.task = { ...this.task, ...updatedTaskDataFromServer };
        // Actualiza editForm para reflejar el estado guardado (para modo visualización)
        this.initializeEditForm(); // Esto usará el this.task actualizado

        this.$emit('task-updated', this.task);
        this.isEditing = false;
        alert('Tarea actualizada exitosamente');
      } catch (error) {
        console.error('Error updating task:', error);
        alert('Error al actualizar la tarea');
      } finally {
        this.loading = false;
      }
    },

    async markAsCompleted() {
      if (this.task.estado && this.task.estado.toLowerCase() === 'completada') {
        alert('La tarea ya está completada.');
        return;
      }
      this.loading = true;
      try {
        // Asumimos que markTaskAsDone devuelve la tarea actualizada
        const updatedTask = await markTaskAsDone(this.task.id_tarea); // Asegúrate que markTaskAsDone esté correcta
        this.task.estado = updatedTask.estado; // Actualiza el estado local
        this.editForm.estado = updatedTask.estado; // Y el de editForm

        this.$emit('task-completed', this.task.id_tarea); // O emite la tarea actualizada
        alert('Tarea marcada como completada');
      } catch (error) {
        console.error('no se pudo marcar como completada por:', error);
        alert('Error al marcar la tarea como completada');
      } finally {
        this.loading = false;
      }
    },

    async deleteTask() {
      if (!confirm('¿Estás seguro de que quieres eliminar esta tarea?')) {
        return;
      }

      this.loading = true;
      try {
        await deleteTask(this.task.id_tarea); // Usa this.task.id_tarea

        // Emit event to parent component
        this.$emit('task-deleted', this.task.id_tarea);

        // No necesitas hacer nada más aquí, el componente será eliminado por el padre
        // alert('Tarea eliminada exitosamente'); // Opcional, ya que el componente desaparecerá
      } catch (error) {
        console.error('Error deleting task:', error);
        alert('Error al eliminar la tarea');
      } finally {
        this.loading = false;
      }
    },

    formatDate(dateString) {
      if (!dateString) return 'No especificada'
      const date = new Date(dateString)
      return date.toLocaleDateString('es-ES')
    },

    formatDateForInput(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toISOString().split('T')[0]
    }
  }
}
</script>

<style scoped>
.task-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  margin: 16px 0;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}

.task-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.task-title {
  margin: 0;
  color: #333;
  font-size: 1.2em;
}

.task-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.task-actions button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.task-actions button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-edit {
  background-color: #007bff;
  color: white;
}

.btn-edit:hover:not(:disabled) {
  background-color: #0056b3;
}

.btn-complete {
  background-color: #28a745;
  color: white;
}

.btn-complete:hover:not(:disabled) {
  background-color: #1e7e34;
}

.btn-complete:disabled {
  background-color: #6c757d;
}

.btn-delete {
  background-color: #dc3545;
  color: white;
}

.btn-delete:hover:not(:disabled) {
  background-color: #c82333;
}

.task-content {
  margin-top: 12px;
}

.task-description {
  margin: 8px 0;
  color: #666;
  line-height: 1.4;
}

.task-details {
  margin-top: 12px;
}

.task-details p {
  margin: 4px 0;
  font-size: 14px;
}

.edit-mode {
  border-top: 2px solid #007bff;
  padding-top: 12px;
}

.form-group {
  margin-bottom: 12px;
}

.form-group label {
  display: block;
  margin-bottom: 4px;
  font-weight: bold;
  color: #333;
}

.form-input, .form-textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-textarea {
  min-height: 60px;
  resize: vertical;
}

.form-input:focus, .form-textarea:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.edit-actions {
  display: flex;
  gap: 8px;
  margin-top: 16px;
}

.btn-save {
  background-color: #28a745;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-save:hover:not(:disabled) {
  background-color: #1e7e34;
}

.btn-cancel {
  background-color: #6c757d;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel:hover:not(:disabled) {
  background-color: #545b62;
}

@media (max-width: 768px) {
  .task-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .task-actions {
    margin-top: 8px;
    width: 100%;
  }

  .task-actions button {
    flex: 1;
    min-width: 80px;
  }
}
</style>