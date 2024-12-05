import React, { createContext, useState, useEffect } from 'react';
import * as SecureStore from 'expo-secure-store';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null); // null = non connecté, { token } = connecté
    const [loading, setLoading] = useState(true); // État de chargement initial


    const loadUserFromToken = async () => {
        try {
            console.log("tototo")
            const token = await SecureStore.getItemAsync('token');
            if (token) {
                setUser({ token }); // Si un token existe, l'utilisateur est connecté
            } else {
                console.log('Aucun token trouvé');
            }
        } catch (error) {
            console.error('Erreur lors de la récupération du token:', error);
        }
        setLoading(false); // Chargement terminé
    };


    const login = async (token) => {
        await SecureStore.setItemAsync('token', token); // Enregistrer le token
        setUser({ token }); // Mettre à jour l'état utilisateur
    };

    const logout = async () => {
        await SecureStore.deleteItemAsync('token'); // Supprimer le token
        setUser(null); // Réinitialiser l'utilisateur
    };

    useEffect(() => {
        loadUserFromToken(); // Charger l'utilisateur dès le démarrage
    }, []);

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};
