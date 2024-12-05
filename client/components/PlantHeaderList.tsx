import React, { useState, useEffect } from "react";
import { Animated, StyleSheet, View, ActivityIndicator, useWindowDimensions } from "react-native";
import { PlantHeader } from "@/components/PlantHeader";
import AsyncStorage from "@react-native-async-storage/async-storage";

import { fetchPlant } from "./API_Auth/api";

const FlatList = Animated.FlatList;

export function PlantHeaderList() {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const { width } = useWindowDimensions();
    const numColumns = width < 768 ? 1 : 3;

    type Plant = {
        id: string;
        name: string;
    };

    useEffect(() => {
        const getPlants = async () => {
            try {
                setLoading(true);
                const response = await fetchPlant(); // Appel API pour récupérer les plantes
                console.log(response.data);
                setData(response.data);
            } catch (err) {
                await AsyncStorage.removeItem("token");
                window.location.reload();
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        getPlants();
    }, []);

    const renderItem = ({ item }: { item: Plant }) => (
        <PlantHeader id={item.id} name={item.name} style={styles.headerImage} />
    );

    if (loading) {
        return (
            <View style={styles.loadingContainer}>
                <ActivityIndicator size="large" color="#0000ff" />
            </View>
        );
    }

    return (
        <FlatList
            data={data}
            key={numColumns}
            keyExtractor={(item) => item.id}
            renderItem={renderItem}
            numColumns={numColumns}
            columnWrapperStyle={numColumns > 1 ? styles.row : undefined}
            contentContainerStyle={styles.list}
        />
    );
}

const styles = StyleSheet.create({
    list: {
        gap: 8,
    },
    row: {
        justifyContent: "space-between",
    },
    headerImage: {
        flex: 1,
        margin: 4,
    },
    loadingContainer: {
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
    },
});
