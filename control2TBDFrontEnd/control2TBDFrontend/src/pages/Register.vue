<template>
  <div>
    <h1>Registro de Usuario</h1>
    <form>
      <label for="username">Nombre de Usuario:</label>
      <input id="username" type="text" placeholder="Ingrese su nombre de usuario" />

      <label for="password">Contraseña:</label>
      <input id="password" type="password" placeholder="Ingrese su contraseña" />

      <label for="address">Dirección Geográfica:</label>
      <input id="address" type="text" placeholder="Ingrese su dirección" />

      <button type="submit">Registrar</button>
    </form>
  </div>
</template>

<script>
export default {
  name: "Register",
};
</script>
