import { createRouter, createWebHistory } from "vue-router"
import Dashboard from "@/views/index.vue"

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "Dashboard",
            component: Dashboard,
        },
    ],
})

export default router
