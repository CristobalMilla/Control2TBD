import axiosInstance from "./axiosInstance";

export const getTasks = async () => {
  const response = await axiosInstance.get("/tasks");
  return response.data;
};

export const createTask = async (taskData) => {
  const response = await axiosInstance.post("/tasks", taskData);
  return response.data;
};

// Puedes incluir consultas específicas para las preguntas del punto 6:
export const getTasksBySector = async (sectorId) => {
  const response = await axiosInstance.get(`/statistics/tasks-by-sector/${sectorId}`);
  return response.data;
};
