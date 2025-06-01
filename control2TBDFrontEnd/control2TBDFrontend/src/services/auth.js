import { login, register } from "@/auth/auth";
import { saveToken } from "./jwt";

export const loginUser = async (credentials) => {
    try {
        const data = await login(credentials);
        saveToken(data.token); // Guarda el JWT en `localStorage`

        // Crea un objeto `user` con los datos del usuario
        const user = {
            token: data.token,
            id_usuario: data.id_usuario,
            nickname: data.nickname,
            role: data.role
        };

        // Guarda en localStorage
        localStorage.setItem("user", JSON.stringify(user));

        // Imprime en consola
        console.log("LocalStorage.\nUser logged in:", user);

        // Devuelve el usuario
        return data.user;

    } catch (error) {
    console.error("Error en loginUser:", error);
    throw error;
  }
};


export const logoutUser = () => {
  localStorage.removeItem("user"); // Elimina el usuario del localStorage
};

export const registerUser = async (userData) => {
    try {
        const data = await register(userData); // usamos la de la carpeta api
        if (data.token) {
            saveToken(data.token);
        }

        // Crea un objeto `user` con los datos del usuario
        const user = {
            token: data.token,
            id_usuario: data.id_usuario,
            nickname: data.nickname,
            role: data.role
        };

        // Guarda en localStorage
        localStorage.setItem("user", JSON.stringify(user));

        // Imprime en consola
        console.log("LocalStorage.\nUser registered:", user);

        // Devuelve el usuario
        return data.user;

    } catch (error) {
    console.error("Error en loginUser:", error);
    throw error;
  }
};
