import React, { useState, useEffect } from 'react';
import { ThemedView } from "@/components/ThemedView";
import { ThemedText } from "@/components/ThemedText";
import { Button, StyleSheet, Switch, Linking } from "react-native";
import {Stack, useLocalSearchParams} from "expo-router";
import SliderComponent from "@/app/plant/Slider";
import { useNavigation } from '@react-navigation/native';
import {arroserPlante, deletePlante, infoPlant} from "@/components/API_Auth/api";

export default function PlantDetail() {
    const [plante, setPlante] = useState<any>(null);
    const { id } = useLocalSearchParams();


    useEffect(() => {
        const fetchPlantDetails = async () => {
            try {
                const response = await infoPlant(id); // Appel de l'API
                setPlante(response.data); // Mise à jour des données
            } catch (error) {
                console.error('Erreur lors de la récupération des données de la plante', error);
            }
        };

        fetchPlantDetails();
    }, [id]);

    const handleArroser = async () => {
        try {
            await arroserPlante(id, plante); // Appel de la fonction arroserPlante
        } catch (error) {
            alert('Erreur lors de l\'arrosage de la plante');
        }
    };
    const handleChange = () => { };

    const handleDelete = async () => {
        try {
            await deletePlante(id, plante); // Appel de la fonction deletePlante
        } catch (error) {
            alert('Erreur lors de la suppression de la plante');
        }
    };

    if (!plante) {
        return (
            <ThemedView style={styles.container}>
                <ThemedText type="subtitle">Chargement des informations de la plante...</ThemedText>
            </ThemedView>
        );
    }

    return (
        <>
            <Stack.Screen options={{ title: "Détail d'une plante" }} />
            <ThemedView style={styles.container}>
                <div style={styles.header}>
                    <img
                        style={styles.headerImage}
                        src={plante.urlImage || "https://fastly.picsum.photos/id/305/4928/3264.jpg?hmac=s2FLjeAIyYH0CZl3xuyOShFAtL8yEGiYk31URLDxQCI"}
                        alt="Image d'illustration de la plante"
                    />
                    <SliderComponent humidityRate={plante.humidityRate} />
                </div>
                <ThemedText type="subtitle" style={{ paddingTop: 10 }}>
                    {plante.name || "Nom de la plante"}
                </ThemedText>
                <ThemedText type="default" style={{ marginBottom: 10 }}>
                    {plante.description || "Description de la plante"}
                </ThemedText>
                <label style={{ marginBottom: 10 }}>
                    <ThemedText type="subtitle" style={{ marginBottom: 10 }}>
                        Arrosage automatique
                    </ThemedText>
                    <Switch onChange={handleChange} value={plante.automaticWatering} />
                </label>
                <label style={styles.labelWithInput}>
                    <ThemedText type="subtitle">Taux d'humidité journalier</ThemedText>
                    <input
                        type="number"
                        readOnly={true}
                        value={plante.waterByDayPurcentage}
                        style={styles.input}
                    />
                </label>
                <div style={styles.waterButton}>
                    <Button title={"Arroser"} onPress={handleArroser} />
                </div>
                <div style={styles.deleteButton}>
                    <Button onPress={handleDelete} title={"Supprimer"} color={"red"} />
                </div>
            </ThemedView>
        </>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 20,
        flexDirection: 'column',
        gap: 8,
    },
    header: {
        display: 'flex',
        flexDirection: 'row',
        gap: 8,
        justifyContent: 'center',
        paddingTop: 8,
        paddingBottom: 16
    },
    labelWithInput: {
        display: 'flex',
        flexDirection: 'column',
        gap: 4
    },
    input: {
        borderRadius: 8,
        height: 40,
        paddingBottom: 8,
        paddingTop: 8,
        paddingLeft: 16,
        paddingRight: 16,
    },
    headerImage: {
        maxWidth: "70%",
        objectFit: 'cover',
        marginLeft: -20,
        height: 300,
        borderRadius: 8,
    },
    waterButton: {
        position: 'absolute',
        bottom: 8,
        right: 8,
    },
    deleteButton: {
        position: 'absolute',
        backgroundColor: "red",
        bottom: 8,
        left: 8,
    }
});
