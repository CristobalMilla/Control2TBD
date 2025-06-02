import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8000/auth/usuarios",
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor para incluir el token JWT
axiosInstance.interceptors.request.use((config) => {
  const userString = localStorage.getItem("user");
  if (userString) {
    const user = JSON.parse(userString);
    if (user && user.token) {
      config.headers.Authorization = `Bearer ${user.token}`;
    }
  }
  return config;
});

export default axiosInstance;
