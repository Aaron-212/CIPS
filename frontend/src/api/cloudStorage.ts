import api from "./index"

export interface CloudStorage {
    id: number
    name: string
    provider: string
    size: number // MB
    createdAt: Date
}

export interface CloudStorageSummary {
    totalCount: number
    totalSize: number // MB
}

export const cloudStorageService = {
    async getAll() {
        const response = await api.get<CloudStorage[]>("/cloudStorage/fetch")
        return response.data
    },

    async getById(id: number) {
        const response = await api.get<CloudStorage>(`/cloudStorage/fetch/${id}`)
        return response.data
    },

    async summary() {
        const response = await api.get<CloudStorageSummary>("/cloudStorage/summary")
        return response.data
    },

    async create(provider: string) {
        const response = await api.post("/cloudStorage/create", null, {
            params: { provider },
        })
        return response.data
    },
}
