<script setup lang="ts">
import { RouterView } from "vue-router"
import Menubar from "primevue/menubar"
</script>

<template>
    <header class="sticky top-0 z-50 p-4">
        <Menubar>
            <template #start>
                <p>Dashboard</p>
            </template>
        </Menubar>
    </header>
    <RouterView />
</template>
