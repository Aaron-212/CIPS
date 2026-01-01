<script setup lang="ts">
import Panel from "primevue/panel"
import Message from "primevue/message"
import Card from "primevue/card"
import Button from "primevue/button"
import Tag from "primevue/tag"
import { useAxios } from "@vueuse/integrations/useAxios"
import { useDateFormat } from "@vueuse/core"
import api from "@/api/index"
import { computeInstanceService } from "@/api/computeInstance"

const { data: ciData, isFinished: ciIsFinished } = useAxios("/computeInstance/fetch", api)
const { data: ciSummaryData, isFinished: ciSummaryIsFinished } = useAxios("/computeInstance/summary", api)

const { data: csData, isFinished: csIsFinished } = useAxios("/cloudStorage/fetch", api)
const { data: csSummaryData, isFinished: csSummaryIsFinished } = useAxios("/cloudStorage/summary", api)

function computeInstanceCreate(provider: string) {
    computeInstanceService.create(provider)
}
</script>

<template>
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
                        <Button icon="pi pi-plus-circle" severity="secondary" rounded text />
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
                                    {{ instance.dnsName }}
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
                        <Button icon="pi pi-plus-circle" severity="secondary" rounded text />
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
                                <span class="break-all">{{ instance.size }} MB</span>
                            </template>
                            <template #content>
                                <p>Provider: {{ instance.provider }}</p>
                                <p>Created at: {{ useDateFormat(instance.createdAt) }}</p>
                            </template>
                        </Card>
                    </span>
                </div>
                <div v-else>
                    <Message severity="secondary">No compute instances found</Message>
                </div>
            </div>
        </div>
    </div>
</template>
