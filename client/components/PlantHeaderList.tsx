import React from "react";
import { Animated, StyleSheet, useWindowDimensions } from "react-native";
import { PlantHeader } from "@/components/PlantHeader";

const FlatList = Animated.FlatList;

// TODO: Remplacer par les données du serveur
const data = [
    {
        id: "1",
        name: "Plante 1",
        humidityRate: 50,
        imageUrl: "https://fastly.picsum.photos/id/305/4928/3264.jpg?hmac=s2FLjeAIyYH0CZl3xuyOShFAtL8yEGiYk31URLDxQCI",
    },
    {
        id: "2",
        name: "Plante 2",
        humidityRate: 60,
        imageUrl: "https://fastly.picsum.photos/id/305/4928/3264.jpg?hmac=s2FLjeAIyYH0CZl3xuyOShFAtL8yEGiYk31URLDxQCI",
    },
    {
        id: "3",
        name: "Plante 3",
        humidityRate: 70,
        imageUrl: "https://fastly.picsum.photos/id/305/4928/3264.jpg?hmac=s2FLjeAIyYH0CZl3xuyOShFAtL8yEGiYk31URLDxQCI",
    },
    {
        id: "4",
        name: "Plante 4",
        humidityRate: 80,
        imageUrl: "https://fastly.picsum.photos/id/305/4928/3264.jpg?hmac=s2FLjeAIyYH0CZl3xuyOShFAtL8yEGiYk31URLDxQCI",
    },
];

export function PlantHeaderList() {
    const { width } = useWindowDimensions();
    const numColumns = width < 768 ? 1 : 3;

    const renderItem = ({ item }: { item: typeof data[0] }) => (
        <PlantHeader
            id={item.id}
            name={item.name}
            imageUrl={item.imageUrl}
            style={styles.headerImage}

        />
    );

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
        margin: 4
    },
});
