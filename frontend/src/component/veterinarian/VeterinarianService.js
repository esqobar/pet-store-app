import {api} from "../utils/api.js";

export async function getVeterinarians() {
    // eslint-disable-next-line no-useless-catch
    try {
        const result = await api.get('/veterinarians/all');
        return result.data
    } catch (error) {
        throw error;
    }
}

export async function findAvailableVeterinarians(searchParams) {
    // eslint-disable-next-line no-useless-catch
    try {
        const queryParams = new URLSearchParams(searchParams)
        const result = await api.get(`/veterinarians/search-veterinarian?${queryParams}`);
        return result.data
    } catch (error) {
        throw error;
    }
}