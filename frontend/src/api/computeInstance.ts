import api from "./index"

export interface ComputeInstance {
    id: number
    provider: string
    dnsName: string
    state: string
    createdAt: Date
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

    async create(provider: string) {
        const response = await api.post(`/computeInstance/create`, null, {
            params: { provider },
        })
        return response.data
    },
}
