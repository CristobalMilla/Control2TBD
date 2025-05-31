import { login, register } from "@/api/auth";
import { saveToken, removeToken } from "@/utils/jwt";

export const loginUser = async (credentials) => {
  const data = await login(credentials);
  saveToken(data.token); // Guarda el JWT en `localStorage`
  return data.user;
};

export const logoutUser = () => {
  removeToken(); // Elimina el JWT del almacenamiento local
};
