<template>
  <!-- Modal Overlay -->
  <div v-if="isVisible" class="modal-overlay" @click="handleOverlayClick">
    <div class="modal-container" @click.stop>
      <div class="modal-header">
        <h2>Crear Nueva Tarea</h2>
        <button class="close-button" @click="cancelCreate">&times;</button>
      </div>

      <form @submit.prevent="confirmCreate" class="modal-form">
        <div class="form-group">
          <label for="titulo">Título *</label>
          <input
              id="titulo"
              v-model="formData.titulo"
              type="text"
              class="form-input"
              placeholder="Ingrese el título de la tarea"
              required
          />
        </div>

        <div class="form-group">
          <label for="descripcion">Descripción *</label>
          <textarea
              id="descripcion"
              v-model="formData.descripcion"
              class="form-textarea"
              placeholder="Ingrese la descripción de la tarea"
              rows="4"
              required
          ></textarea>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="fecha_vencimiento">Fecha de Vencimiento *</label>
            <input
                id="fecha_vencimiento"
                v-model="formData.fecha_vencimiento"
                type="date"
                class="form-input"
                required
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="id_usuario">ID Usuario *</label>
            <input
                id="id_usuario"
                v-model.number="formData.id_usuario"
                type="number"
                class="form-input"
                placeholder="ID del usuario asignado"
                min="1"
                required
            />
          </div>

          <div class="form-group">
            <label for="id_sector">ID Sector *</label>
            <input
                id="id_sector"
                v-model.number="formData.id_sector"
                type="number"
                class="form-input"
                placeholder="ID del sector"
                min="1"
                required
            />
          </div>
        </div>

        <div class="modal-footer">
          <button
              type="button"
              class="btn-cancel"
              @click="cancelCreate"
              :disabled="isSubmitting"
          >
            Cancelar
          </button>
          <button
              type="submit"
              class="btn-confirm"
              :disabled="isSubmitting || !isFormValid"
          >
            {{ isSubmitting ? 'Creando...' : 'Crear Tarea' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { createTask } from '../api/tasks'

export default {
  name: 'TaskCreateModal',
  props: {
    isVisible: {
      type: Boolean,
      default: false
    }
  },
  emits: ['close', 'task-created'],
  data() {
    return {
      isSubmitting: false,
      formData: {
        titulo: '',
        descripcion: '',
        fecha_vencimiento: '',
        id_usuario: null,
        id_sector: null,
        estado: 'Pendiente'
      }
    }
  },
  computed: {
    isFormValid() {
      return this.formData.titulo &&
          this.formData.descripcion &&
          this.formData.fecha_vencimiento &&
          this.formData.id_usuario &&
          this.formData.id_sector &&
          this.formData.estado
    }
  },
  watch: {
    isVisible(newValue) {
      if (newValue) {
        this.resetForm()
        // Auto-focus primer campo cuando se abre el modal
        this.$nextTick(() => {
          const firstInput = this.$el.querySelector('#titulo')
          if (firstInput) {
            firstInput.focus()
          }
        })
      }
    }
  },
  methods: {
    resetForm() {
      this.formData = {
        titulo: '',
        descripcion: '',
        fecha_vencimiento: '',
        id_usuario: null,
        id_sector: null,
        estado: 'Pendiente'
      }
      this.isSubmitting = false
    },

    cancelCreate() {
      this.$emit('close')
    },

    handleOverlayClick() {
      // Cerrar modal al hacer click fuera
      this.cancelCreate()
    },

    async confirmCreate() {
      if (!this.isFormValid || this.isSubmitting) {
        return
      }

      this.isSubmitting = true

      try {
        // Preparar datos para envío
        const taskData = {
          titulo: this.formData.titulo.trim(),
          descripcion: this.formData.descripcion.trim(),
          fecha_vencimiento: new Date(this.formData.fecha_vencimiento).toISOString().split('T')[0],
          id_usuario: this.formData.id_usuario,
          id_sector: this.formData.id_sector,
          estado: this.formData.estado,
        }

        // Enviar petición al backend
        const response = await createTask(taskData)

        // Emitir evento con la nueva tarea
        this.$emit('task-created', response)

        // Cerrar modal
        this.$emit('close')

        // Mostrar mensaje de éxito
        alert('¡Tarea creada exitosamente!')

      } catch (error) {
        console.error('Error al crear la tarea:', error)

        // Mostrar mensaje de error más específico
        let errorMessage = 'Error al crear la tarea'
        if (error.response?.data?.message) {
          errorMessage = error.response.data.message
        } else if (error.message) {
          errorMessage = error.message
        }

        alert(`Error: ${errorMessage}`)
      } finally {
        this.isSubmitting = false
      }
    }
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #e5e7eb;
}

.modal-header h2 {
  margin: 0;
  color: #1f2937;
  font-size: 1.5rem;
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  color: #6b7280;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-button:hover {
  background-color: #f3f4f6;
  color: #374151;
}

.modal-form {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #374151;
  font-size: 14px;
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #e5e7eb;
}

.btn-cancel,
.btn-confirm {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel {
  background-color: #f3f4f6;
  color: #374151;
}

.btn-cancel:hover:not(:disabled) {
  background-color: #e5e7eb;
}

.btn-confirm {
  background-color: #3b82f6;
  color: white;
}

.btn-confirm:hover:not(:disabled) {
  background-color: #2563eb;
}

.btn-confirm:disabled,
.btn-cancel:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Responsive */
@media (max-width: 640px) {
  .modal-container {
    width: 95%;
    margin: 10px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .modal-header {
    padding: 16px 20px;
  }

  .modal-form {
    padding: 20px;
  }
}
</style>