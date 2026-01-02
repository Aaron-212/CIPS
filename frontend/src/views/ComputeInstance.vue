<script setup lang="ts">
import { ref, onMounted } from "vue"
import { useRoute, useRouter } from "vue-router"
import { computeInstanceService, type ComputeInstance } from "@/api/computeInstance"
import { useDateFormat } from "@vueuse/core"
import Card from "primevue/card"
import Button from "primevue/button"
import Tag from "primevue/tag"
import Panel from "primevue/panel"
import Skeleton from "primevue/skeleton"
import Message from "primevue/message"

const route = useRoute()
const router = useRouter()
const instanceId = Number(route.params.id)

const instance = ref<ComputeInstance | null>(null)
const cost = ref<number | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)

const fetchInstance = async () => {
    try {
        loading.value = true
        const [instanceData, costData] = await Promise.all([
            computeInstanceService.getById(instanceId),
            computeInstanceService.getCost(instanceId),
        ])
        instance.value = instanceData
        cost.value = costData
    } catch (err) {
        console.error("Failed to fetch instance:", err)
        error.value = "Failed to load compute instance details."
    } finally {
        loading.value = false
    }
}

const startInstance = async () => {
    try {
        await computeInstanceService.start(instanceId)
        await fetchInstance()
    } catch (err) {
        console.error("Failed to start instance:", err)
    }
}

const stopInstance = async () => {
    try {
        await computeInstanceService.stop(instanceId)
        await fetchInstance()
    } catch (err) {
        console.error("Failed to stop instance:", err)
    }
}

const destroyInstance = async () => {
    if (confirm("Are you sure you want to destroy this instance?")) {
        try {
            await computeInstanceService.destroy(instanceId)
            router.push("/")
        } catch (err) {
            console.error("Failed to destroy instance:", err)
        }
    }
}

onMounted(fetchInstance)
</script>

<template>
    <div class="max-w-4xl mx-auto px-10 py-8">
        <div v-if="instance" class="mb-6 flex items-center gap-4">
            <Button v-if="instance.state === 'RUNNING'" icon="pi pi-stop" label="Stop" severity="contrast"
                @click="stopInstance" class="grow" />
            <Button v-else icon="pi pi-play" label="Start" severity="primary" @click="startInstance" class="grow" />
            <Button icon="pi pi-trash" label="Destroy" severity="danger" outlined @click="destroyInstance"
                class="grow" />
        </div>

        <div v-if="loading">
            <Skeleton height="20rem" class="mb-4" />
        </div>

        <div v-else-if="error">
            <Message severity="error">{{ error }}</Message>
        </div>

        <div v-else-if="instance">
            <Card>
                <template #title>
                    <div class="flex items-center justify-between gap-4">
                        <div class="flex items-center gap-2">
                            <i class="pi pi-desktop text-2xl"></i>
                            <span class="text-2xl font-bold break-all">{{ instance.dnsName }}</span>
                        </div>
                        <div>
                            <Tag v-if="instance.state === 'RUNNING'" severity="success" value="Running" />
                            <Tag v-else-if="instance.state === 'STOPPED'" severity="danger" value="Stopped" />
                            <Tag v-else severity="secondary" value="Unknown" />
                        </div>
                    </div>
                </template>
                <template #subtitle>
                    <div class="flex flex-col gap-1 mt-2">
                        <span class="text-sm text-surface-500">ID: {{ instance.id }}</span>
                        <span class="text-sm text-surface-500">Provider: {{ instance.provider }}</span>
                        <span class="text-sm text-surface-500">Model: {{ instance.modelName }}</span>
                        <span class="text-sm text-surface-500">
                            Created At: {{ useDateFormat(instance.createdAt, "YYYY-MM-DD HH:mm:ss").value }}
                        </span>
                    </div>
                </template>
                <template #content>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
                        <Panel header="Specifications">
                            <div class="flex flex-col gap-4">
                                <div class="flex justify-between items-center">
                                    <span class="text-surface-500 font-medium">vCPU</span>
                                    <span class="font-bold">{{ instance.vCpu }} Cores</span>
                                </div>
                                <div class="flex justify-between items-center">
                                    <span class="text-surface-500 font-medium">vRAM</span>
                                    <span class="font-bold">{{ instance.vRam }} MB</span>
                                </div>
                                <div class="flex justify-between items-center">
                                    <span class="text-surface-500 font-medium">vDisk</span>
                                    <span class="font-bold">{{ instance.vDisk }} MB</span>
                                </div>
                            </div>
                        </Panel>

                        <Panel header="Network & Usage">
                            <div class="flex flex-col gap-4">
                                <div class="flex justify-between items-center">
                                    <span class="text-surface-500 font-medium">Bandwidth</span>
                                    <span class="font-bold">{{ instance.vBandwidth }} Mbps</span>
                                </div>
                                <div class="flex justify-between items-center">
                                    <span class="text-surface-500 font-medium">Quota</span>
                                    <span class="font-bold">{{ instance.vQuota }} MB</span>
                                </div>
                                <div class="flex justify-between items-center">
                                    <span class="text-surface-500 font-medium">Total Accrued Cost</span>
                                    <span class="font-bold text-primary text-xl" v-if="cost !== null">
                                        ${{ cost.toFixed(4) }}
                                    </span>
                                    <span v-else>Loading...</span>
                                </div>
                            </div>
                        </Panel>
                    </div>
                </template>
            </Card>
        </div>
    </div>
</template>
