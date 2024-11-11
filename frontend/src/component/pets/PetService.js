import {api} from "../utils/api.js";
export async function getAllPetTypes() {
    try {
        const result = await api.get("/pets/get-types");
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function getAllPetColors() {
    try {
        const result = await api.get("/pets/get-colors");
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function getAllPetBreeds(petType) {
    try {
        const result = await api.get(`/pets/get-breeds?petType=${petType}`);
        return result.data;
    } catch (error) {
        throw error;
    }
}