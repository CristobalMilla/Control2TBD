import axiosInstance from "./axiosInstance";

export const getTasks = async () => {
  const response = await axiosInstance.get("/tasks");
  return response.data;
};

export const getTaskById = async (taskId) => {
  const response = await axiosInstance.get(`/tasks/${taskId}`);
}

export const createTask = async (taskData) => {
  const response = await axiosInstance.post("/tasks", taskData);
  return response.data;
};

// Puedes incluir consultas específicas para las preguntas del punto 6:
export const getTasksBySector = async (sectorId) => {
  const response = await axiosInstance.get(`/statistics/tasks-by-sector/${sectorId}`);
  return response.data;
};

export const markTaskAsDone = async (taskId) => {
  const response = await axiosInstance.put(`/tasks/${taskId}/done`);
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


