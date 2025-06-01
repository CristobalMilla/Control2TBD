<template>
  <div>
    <v-list v-if="tasks.length > 0">
      <v-list-item
        v-for="task in tasks"
        :key="task.id"
        class="py-4 px-6"
      >
        <v-list-item-content>
          <v-list-item-title>{{ task.title }}</v-list-item-title>
          <v-list-item-subtitle>{{ task.sector }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <div v-else class="text-center text-grey pa-6">
      No hay tareas disponibles para este sector.
    </div>
  </div>
</template>

<script>
export default {
  name: "Question7",
  props: {
    tasks: {
      type: Array,
      default: () => [],
    },
  },
};
</script>
