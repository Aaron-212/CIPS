<script setup lang="ts">
import Panel from "primevue/panel"
import Message from "primevue/message"
import Card from "primevue/card"
import Button from "primevue/button"
import Tag from "primevue/tag"
import Menu from "primevue/menu"
import Toast from "primevue/toast"
import { useAxios } from "@vueuse/integrations/useAxios"
import { useDateFormat } from "@vueuse/core"
import { useToast } from "primevue/usetoast"
import { ref } from "vue"
import api from "@/api/index"
import { computeInstanceService } from "@/api/computeInstance"
import { cloudStorageService } from "@/api/cloudStorage"
import { RouterLink } from "vue-router"

const toast = useToast()

const { data: ciData, isFinished: ciIsFinished, execute: ciExecute } = useAxios("/computeInstance/fetch", api)
const {
    data: ciSummaryData,
    isFinished: ciSummaryIsFinished,
    execute: ciSummaryExecute,
} = useAxios("/computeInstance/summary", api)

const { data: csData, isFinished: csIsFinished, execute: csExecute } = useAxios("/cloudStorage/fetch", api)
const {
    data: csSummaryData,
    isFinished: csSummaryIsFinished,
    execute: csSummaryExecute,
} = useAxios("/cloudStorage/summary", api)

const ciMenu = ref()
const ciMenuItems = [
    {
        label: "AWS",
        command: () => computeInstanceCreate("AWS"),
    },
    {
        label: "GCP",
        command: () => computeInstanceCreate("GCP"),
    },
]

const csMenu = ref()
const csMenuItems = [
    {
        label: "AWS",
        command: () => cloudStorageCreate("AWS"),
    },
    {
        label: "GCP",
        command: () => cloudStorageCreate("GCP"),
    },
]

const toggleCiMenu = (event: any) => {
    ciMenu.value.toggle(event)
}

const toggleCsMenu = (event: any) => {
    csMenu.value.toggle(event)
}

async function computeInstanceCreate(provider: string) {
    await computeInstanceService.create(provider)
    ciExecute()
    ciSummaryExecute()
    showSuccessToast("Compute Instance created successfully")
}

async function cloudStorageCreate(provider: string) {
    await cloudStorageService.create(provider)
    csExecute()
    csSummaryExecute()
    showSuccessToast("Cloud Storage created successfully")
}

const showSuccessToast = (content: string) => {
    toast.add({
        severity: "success",
        summary: content,
        life: 3000,
    })
}
</script>

<template>
    <Toast />
    <div class="max-w-300 mx-auto px-10 py-8">
        <div class="grid grid-cols-2 gap-4 justify-between">
            <div class="grow flex flex-col gap-4">
                <Panel>
                    <template #header>
                        <div class="flex items-center gap-2">
                            <i class="pi pi-desktop"></i>
                            <span class="font-bold">Compute Instances</span>
                        </div>
                    </template>
                    <template #icons>
                        <Button
                            icon="pi pi-plus-circle"
                            severity="secondary"
                            rounded
                            text
                            @click="toggleCiMenu"
                            aria-haspopup="true"
                            aria-controls="ci_menu"
                        />
                        <Menu ref="ciMenu" id="ci_menu" :model="ciMenuItems" :popup="true" />
                    </template>
                    <span v-if="ciSummaryIsFinished" class="text-4xl font-mono">
                        {{ ciSummaryData.activeCount }}/{{ ciSummaryData.totalCount }}
                    </span>
                    <span v-else class="text-4xl font-mono">
                        <i class="pi pi-spin pi-spinner"></i>
                    </span>
                </Panel>
                <div v-if="ciIsFinished && ciData.length > 0" class="contents">
                    <span v-for="instance in ciData" :key="instance.id" class="text-lg">
                        <Card>
                            <template #title>
                                <span class="break-all">
                                    <Tag v-if="instance.state === 'RUNNING'" severity="success">Running</Tag>
                                    <Tag v-else-if="instance.state === 'STOPPED'" severity="danger">Stopped</Tag>
                                    <Tag v-else severity="warning">Unknown</Tag>
                                    <RouterLink :to="`/computeInstance/${instance.id}`" class="px-2 hover:underline">
                                        {{ instance.dnsName }}
                                    </RouterLink>
                                </span>
                            </template>
                            <template #content>
                                <p>Provider: {{ instance.provider }}. Model: {{ instance.modelName }}</p>
                                <p>Created at: {{ useDateFormat(instance.createdAt) }}</p>
                            </template>
                        </Card>
                    </span>
                </div>
                <div v-else>
                    <Message severity="secondary">No compute instances found</Message>
                </div>
            </div>

            <div class="grow flex flex-col gap-4">
                <Panel>
                    <template #header>
                        <div class="flex items-center gap-2">
                            <i class="pi pi-database"></i>
                            <span class="font-bold">Cloud Storage</span>
                        </div>
                    </template>
                    <template #icons>
                        <Button
                            icon="pi pi-plus-circle"
                            severity="secondary"
                            rounded
                            text
                            @click="toggleCsMenu"
                            aria-haspopup="true"
                            aria-controls="cs_menu"
                        />
                        <Menu ref="csMenu" id="cs_menu" :model="csMenuItems" :popup="true" />
                    </template>
                    <span v-if="csSummaryIsFinished" class="text-4xl font-mono">
                        {{ csSummaryData.totalCount }}, {{ csSummaryData.totalSize }} MB
                    </span>
                    <span v-else class="text-4xl font-mono">
                        <i class="pi pi-spin pi-spinner"></i>
                    </span>
                </Panel>
                <div v-if="csIsFinished && csData.length > 0" class="contents">
                    <span v-for="instance in csData" :key="instance.id" class="text-lg">
                        <Card>
                            <template #title>
                                <RouterLink :to="`/cloudStorage/${instance.id}`" class="hover:underline">
                                    {{ instance.name }}
                                </RouterLink>
                            </template>
                            <template #content>
                                <p>Provider: {{ instance.provider }}. Size: {{ instance.size }} MB.</p>
                                <p>Created at: {{ useDateFormat(instance.createdAt) }}</p>
                            </template>
                        </Card>
                    </span>
                </div>
                <div v-else>
                    <Message severity="secondary">No cloud storage found</Message>
                </div>
            </div>
        </div>
    </div>
</template>
