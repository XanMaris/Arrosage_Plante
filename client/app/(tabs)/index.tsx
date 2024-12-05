import React, { useState, useEffect } from 'react';
import { StyleSheet, View, ActivityIndicator } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

import { HelloWave } from '@/components/HelloWave';
import ParallaxScrollView from '@/components/ParallaxScrollView';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { PlantHeaderList } from '@/components/PlantHeaderList';
import Login from '../../components/API_Auth/Login';

export default function HomeScreen() {
    const [loading, setLoading] = useState(true);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const checkToken = async () => {
            try {
                const token = await AsyncStorage.getItem('token');
                if (token) {
                    setIsLoggedIn(true);
                } else {
                    setIsLoggedIn(false);
                }
            } catch (error) {
                console.error('Erreur lors de la récupération du token', error);
                setIsLoggedIn(false);
            }
            setLoading(false);
        };

        checkToken();
    }, []);

    if (loading) {
        return (
            <View style={styles.loadingContainer}>
                <ActivityIndicator size="large" color="#0000ff" />
            </View>
        );
    }

    if (isLoggedIn) {
        return (
            <ParallaxScrollView>
                <ThemedView style={styles.titleContainer}>
                    <ThemedText type="title">Liste des plantes</ThemedText>
                    <HelloWave />
                </ThemedView>
                <ThemedView style={styles.plantsHeaderContainer}>
                    <PlantHeaderList />
                </ThemedView>
            </ParallaxScrollView>
        );
    }

    return <Login />;
}

const styles = StyleSheet.create({
    titleContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8,
    },
    plantsHeaderContainer: {
        gap: 8,
        marginBottom: 8,
    },
    loadingContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
});
