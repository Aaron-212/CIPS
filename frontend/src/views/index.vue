<script setup lang="ts">
import Panel from "primevue/panel"
import Message from "primevue/message"
import { useAxios } from "@vueuse/integrations/useAxios"
import api from "@/api/index"

const { data, isLoading, isFinished, error } = useAxios("/computeInstance/fetch", api)
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
                    <span class="text-4xl font-mono">0/0</span>
                </Panel>
                <div v-if="isFinished && data.length > 0">
                    <span v-for="instance in data" :key="instance.id" class="text-lg">{{ instance.dnsName }}</span>
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
                            <span class="font-bold">Cloud Storage Buckets</span>
                        </div>
                    </template>
                    <p class="m-0">Monitor your object storage and data buckets in one place.</p>
                </Panel>
            </div>
        </div>
    </div>
</template>
