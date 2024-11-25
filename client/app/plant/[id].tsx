import React, { useState, useEffect } from 'react';
import { ThemedView } from "@/components/ThemedView";
import { ThemedText } from "@/components/ThemedText";
import { Button, StyleSheet, Switch, Linking } from "react-native";
import {Stack, useLocalSearchParams} from "expo-router";
import SliderComponent from "@/app/plant/Slider";

export default function PlantDetail() {
    const [plante, setPlante] = useState<any>(null);
    const [id, setId] = useState<string | null>(useLocalSearchParams().toString());


    useEffect(() => {
        if (id) {
            getPlant();
        }
    }, [id]);

    function getPlant() {
        if (!id) return;

        const url = `http://localhost:8080/plant/${id}`;
        fetch(url)
            .then(response => response.json())
            .then(data => setPlante(data))
            .catch(error => console.error("Erreur lors de la récupération des données de la plante", error));
    }


    const handleChange = () => { };

    function deletePlante() {
        console.log(useLocalSearchParams().toString());
    }

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
                        src={plante.imageUrl || "https://fastly.picsum.photos/id/305/4928/3264.jpg?hmac=s2FLjeAIyYH0CZl3xuyOShFAtL8yEGiYk31URLDxQCI"}
                        alt="Image d'illustration de la plante"
                    />
                    <SliderComponent />
                </div>
                <ThemedText type="subtitle" style={{ paddingTop: 10 }}>
                    {plante.nom || "Nom de la plante"}
                </ThemedText>
                <ThemedText type="default" style={{ marginBottom: 10 }}>
                    {plante.description || "Description de la plante"}
                </ThemedText>
                <label style={{ marginBottom: 10 }}>
                    <ThemedText type="subtitle" style={{ marginBottom: 10 }}>
                        Arrosage automatique
                    </ThemedText>
                    <Switch onChange={handleChange} />
                </label>
                <label style={styles.labelWithInput}>
                    <ThemedText type="subtitle">Taux d'humidité journalier</ThemedText>
                    <input
                        type="number"
                        readOnly={true}
                        value={plante.humidity || 50}
                        style={styles.input}
                    />
                </label>
                <div style={styles.waterButton}>
                    <Button title={"Arroser"} />
                </div>
                <div style={styles.deleteButton}>
                    <Button onPress={deletePlante} title={"Supprimer"} color={"red"} />
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
