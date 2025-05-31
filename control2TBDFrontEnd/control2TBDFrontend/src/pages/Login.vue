<template>
  <div>
    <h1>Inicio de Sesión</h1>
    <form @submit.prevent="handleLogin">
      <div>
        <label for="username">Usuario:</label>
        <input id="username" v-model="username" type="text" />
      </div>
      <div>
        <label for="password">Contraseña:</label>
        <input id="password" v-model="password" type="password" />
      </div>
      <button type="submit">Iniciar Sesión</button>
    </form>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    handleLogin() {
      console.log("Usuario:", this.username, "Contraseña:", this.password);
    },
  },
};
</script>
