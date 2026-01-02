import api from "./index"

export interface ComputeInstance {
    id: number
    provider: string
    dnsName: string
    modelName: string
    vCpu: number
    vRam: number
    vDisk: number
    vBandwidth: number
    vQuota: number
    state: string
    createdAt: string
}

export interface ComputeInstanceSummary {
    activeCount: number
    totalCount: number
}

export interface ComputeInstanceCreate {
    provider: string
}

export const computeInstanceService = {
    async getAll() {
        const response = await api.get<ComputeInstance[]>("/computeInstance/fetch")
        return response.data
    },

    async getById(id: number) {
        const response = await api.get<ComputeInstance>(`/computeInstance/fetch/${id}`)
        return response.data
    },

    async summary() {
        const response = await api.get<ComputeInstanceSummary>(`/computeInstance/summary`)
        return response.data
    },

    async getCost(id: number) {
        const response = await api.get<number>(`/computeInstance/cost/${id}`)
        return response.data
    },

    async create(provider: string) {
        const response = await api.post(`/computeInstance/create`, null, {
            params: { provider },
        })
        return response.data
    },

    async destroy(id: number) {
        const response = await api.delete(`/computeInstance/destroy/${id}`)
        return response.data
    },

    async start(id: number) {
        const response = await api.post(`/computeInstance/start/${id}`)
        return response.data
    },

    async stop(id: number) {
        const response = await api.post(`/computeInstance/stop/${id}`)
        return response.data
    },
}
