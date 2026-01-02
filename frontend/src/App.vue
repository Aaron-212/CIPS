<script setup lang="ts">
import { RouterView, useRoute, useRouter } from "vue-router"
const route = useRoute()
const router = useRouter()
import Menubar from "primevue/menubar"
import Button from "primevue/button"
</script>

<template>
    <header class="sticky top-0 z-50 p-4">
        <Menubar>
            <template #start>
                <div class="h-10 px-0 flex flex-row gap-2 items-center">
                    <Button
                        v-if="route.name !== 'Dashboard'"
                        icon="pi pi-chevron-left"
                        severity="secondary"
                        text
                        @click="router.back()"
                    />

                    <p class="text-xl font-semibold">{{ route.name }}</p>
                </div>
            </template>
        </Menubar>
    </header>
    <RouterView />
</template>
