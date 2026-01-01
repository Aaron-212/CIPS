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
        {
            path: "/computeInstance/:id",
            name: "Compute Instance",
            component: () => import("@/views/ComputeInstance.vue"),
        },
        {
            path: "/cloudStorage/:id",
            name: "Cloud Storage",
            component: () => import("@/views/CloudStorage.vue"),
        },
    ],
})

export default router
