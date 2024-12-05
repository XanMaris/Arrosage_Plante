import axios from 'axios';

const API = axios.create({ baseURL: 'http://localhost:8080/api' });


// Ajouter un interceptateur pour injecter le token dans les headers
API.interceptors.request.use((config) => {
    const token = localStorage.getItem('token'); // Utilisez SecureStore pour Expo (voir plus bas)
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const signup = (userData) => API.post('/auth/signup', userData);
export const login = (credentials) => API.post('/auth/login', credentials);

export const fetchPlant = () => API.get('/plant/');

export const infoPlant = (id) => API.get(`/plant/${id}`);

export const arroserPlante = (id, plante) => {
    const url = `/plant/${id}/arroser`;
    const data = {
        waterByDayPurcentage: plante.waterByDayPurcentage,
        automaticWatering: plante.automaticWatering,
    };

    // Utilisation de axios pour effectuer la requête POST
    return API.post(url, data)
        .then((response) => {
            console.log('La plante a été arrosée');
            return response;
        })
        .catch((error) => {
            console.error('Erreur lors de l\'arrosage de la plante :', error);
            throw error; // Rejeter l'erreur pour être géré par le composant appelant
        });
};


export const deletePlante = (id, plante) => {
    if (!id) {
        console.log("ID de la plante introuvable.");
        return;
    }

    const url = `/plant/${id}`;

    return API.delete(url)
        .then((response) => {
            console.log(`${plante.name} supprimée avec succès.`);
            return response;
        })
        .then(()=>{  setTimeout(() => {
            // Rafraîchit la page après une brève attente
            window.location.reload();
        }, 500)})
        .catch((error) => {
            console.error('Erreur lors de la suppression de la plante :', error);
            throw error;
        });
};
