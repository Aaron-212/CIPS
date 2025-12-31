import api from "./index"

export interface ComputeInstance {
    id: number
    provider: string
    dnsName: string
    state: string
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
}
