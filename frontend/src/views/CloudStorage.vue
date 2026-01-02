<script setup lang="ts">
import { ref, onMounted } from "vue"
import { useRoute, useRouter } from "vue-router"
import { cloudStorageService, type CloudStorage } from "@/api/cloudStorage"
import { useDateFormat } from "@vueuse/core"
import Card from "primevue/card"
import Button from "primevue/button"
import Panel from "primevue/panel"
import Skeleton from "primevue/skeleton"
import Message from "primevue/message"
import InputNumber from "primevue/inputnumber"
import Toast from "primevue/toast"
import ConfirmDialog from "primevue/confirmdialog"
import { useToast } from "primevue/usetoast"
import { useConfirm } from "primevue/useconfirm"

const route = useRoute()
const router = useRouter()
const storageId = Number(route.params.id)

const storage = ref<CloudStorage | null>(null)
const cost = ref<number | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)
const increaseAmount = ref(1000)

const confirm = useConfirm()
const toast = useToast()

const fetchStorage = async () => {
    try {
        loading.value = true
        const [storageData, costData] = await Promise.all([
            cloudStorageService.getById(storageId),
            cloudStorageService.getCost(storageId),
        ])
        storage.value = storageData
        cost.value = costData
    } catch {
        showFailureToast("Failed to load cloud storage")
    } finally {
        loading.value = false
    }
}

const increaseSize = async () => {
    try {
        await cloudStorageService.increaseSize(storageId, increaseAmount.value)
        await fetchStorage()
        showSuccessToast(`Successfully increased storage size by ${increaseAmount.value} MB`)
    } catch {
        showFailureToast("Failed to increase storage size")
    }
}

const confirmDeleteStorage = () => {
    confirm.require({
        message: "Are you sure you want to delete this storage?",
        header: "Delete Storage",
        icon: "pi pi-exclamation-triangle",
        rejectProps: {
            label: "Cancel",
            severity: "secondary",
            outlined: true,
        },
        acceptProps: {
            label: "Delete",
            severity: "danger",
            outlined: true,
        },
        accept: async () => {
            await cloudStorageService.destroy(storageId)
            router.push("/")
        },
    })
}

const showSuccessToast = (content: string) => {
    toast.add({
        severity: "success",
        summary: content,
        life: 3000,
    })
}

const showFailureToast = (content: string) => {
    toast.add({
        severity: "danger",
        summary: content,
        life: 3000,
    })
}

onMounted(fetchStorage)
</script>

<template>
    <ConfirmDialog />
    <Toast />
    <div class="max-w-4xl mx-auto px-10 py-8">
        <div v-if="storage" class="mb-6 flex items-center gap-4">
            <InputNumber v-model="increaseAmount" :min="1" suffix=" MB" class="basis-1/2" />
            <Button icon="pi pi-plus" label="Increase Size" @click="increaseSize" class="basis-1/4" />
            <Button
                icon="pi pi-trash"
                label="Destroy"
                severity="danger"
                outlined
                @click="confirmDeleteStorage"
                class="basis-1/4"
            />
        </div>

        <div v-if="loading">
            <Skeleton height="20rem" class="mb-4" />
        </div>

        <div v-else-if="error">
            <Message severity="error">{{ error }}</Message>
        </div>

        <div v-else-if="storage">
            <Card>
                <template #title>
                    <div class="flex items-center gap-2">
                        <i class="pi pi-database text-2xl"></i>
                        <span class="text-2xl font-bold break-all">{{ storage.name }}</span>
                    </div>
                </template>
                <template #subtitle>
                    <div class="flex flex-col gap-1 mt-2">
                        <span class="text-sm text-surface-500">ID: {{ storage.id }}</span>
                        <span class="text-sm text-surface-500">Provider: {{ storage.provider }}</span>
                        <span class="text-sm text-surface-500">
                            Created At: {{ useDateFormat(storage.createdAt, "YYYY-MM-DD HH:mm:ss").value }}
                        </span>
                    </div>
                </template>
                <template #content>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
                        <Panel header="Storage Details">
                            <div class="flex flex-col gap-4">
                                <div class="flex justify-between items-center">
                                    <span class="text-surface-500 font-medium">Capacity</span>
                                    <span class="font-bold">{{ storage.size }} MB</span>
                                </div>
                                <div class="flex justify-between items-center">
                                    <span class="text-surface-500 font-medium">Accrued Cost</span>
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
