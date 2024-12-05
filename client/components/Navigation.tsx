import React, { useContext } from 'react';
import { AuthContext } from './API_Auth/AthContext';
import { ActivityIndicator, View, Text } from 'react-native';
import LoginScreen from './API_Auth/Login';
import Tabs from '../app/(tabs)/_layout'; // Vos Tabs actuels

const Navigation = () => {
    const { user, loading } = useContext(AuthContext);

    // Afficher un spinner pendant le chargement de l'état utilisateur
    if (loading) {
        return (
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                <ActivityIndicator size="large" />
                <Text>Chargement...</Text>
            </View>
        );
    }

    // Si l'utilisateur est connecté, afficher les Tabs
    if (user) {
        return <Tabs />;
    }

    // Sinon, afficher l'écran de connexion
    return <LoginScreen />;
};

export default Navigation;
