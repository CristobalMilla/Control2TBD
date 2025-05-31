<template>
  <div>
    <h1>Prueba de Componente</h1>
    <Button variant="primary">Botón Primario</Button>
    <Button variant="secondary">Botón Secundario</Button>
  </div>
</template>

<script>
import Button from "@/components/Button.vue";

export default {
  components: { Button },
};
</script>
