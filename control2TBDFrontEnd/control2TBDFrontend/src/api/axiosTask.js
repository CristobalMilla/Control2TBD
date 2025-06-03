import axios from "axios";

const axiosTasks = axios.create({
  baseURL: "http://localhost:8000/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor para incluir el token JWT si es necesario
axiosTasks.interceptors.request.use((config) => {
  const userString = localStorage.getItem("user");
  if (userString) {
    const user = JSON.parse(userString);
    if (user && user.token) {
      config.headers.Authorization = `Bearer ${user.token}`;
    }
  }
  return config;
});

export default axiosTasks;