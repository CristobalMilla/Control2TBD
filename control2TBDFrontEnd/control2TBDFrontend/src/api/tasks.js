import axiosInstance from "./axiosInstance";

export const getTasks = async () => {
  const response = await axiosInstance.get("/tareas");
  return response.data;
};

export const getTaskById = async (taskId) => {
  const response = await axiosInstance.get(`/tareas/${taskId}`);
  return response.data; // Asegúrate de retornar los datos
}

export const createTask = async (taskData) => {
  const response = await axiosInstance.post("/tareas", taskData);
  return response.data;
};

// Puedes incluir consultas específicas para las preguntas del punto 6:
export const getTasksBySector = async (sectorId) => {
  const response = await axiosInstance.get(`/statistics/tasks-by-sector/${sectorId}`);
  return response.data;
};

//Pregunta 7

export const getAllTasksPerUserPerSector = async () => {
  const response = await axiosInstance.get("/tareas/allTareasPerUserPerSector");
  return response.data; // Asegúrate de que el backend devuelve los datos en el formato esperado
};

//Pregunta 8

export const getSectorMostCompletedByUser = async (id_usuario) => {
  const response = await axiosInstance.get(`/tareas/SectorMostCompletedByUser/` + id_usuario);
  return response.data;
};

//Pregunta 9

export const getAverageCompletedDistance = async (id_usuario) => {
  const response = await axiosInstance.get(`/tareas/AverageCompletedUser/` + id_usuario);
  return response.data;
};

export const getAverageDistance = async (userId) => {
    try {
        const response = await axiosInstance.get(`/tareas/promedio-distancia/${userId}`);
        return response.data;
    } catch (error) {
        console.error('Error al obtener el promedio de distancia:', error);
        throw error;
    }
};

export const markTaskAsDone = async (taskId) => {
  // Corregir el endpoint para que coincida con el backend
  const response = await axiosInstance.put(`/tareas/${taskId}/complete`);
  return response.data;
}

export const updateTask = async (taskId, taskData) => {
  const response = await axiosInstance.put(`/tareas/${taskId}`, taskData);
  return response.data;
};

export const deleteTask = async (taskId) => {
  const response = await axiosInstance.delete(`/tareas/${taskId}`);
  return response.data;
};


