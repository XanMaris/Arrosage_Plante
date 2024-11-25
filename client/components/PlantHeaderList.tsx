import React, { useState, useEffect } from "react";
import { Animated, StyleSheet, useWindowDimensions } from "react-native";
import { PlantHeader } from "@/components/PlantHeader";
import {number} from "prop-types";

const FlatList = Animated.FlatList;

export function PlantHeaderList() {
    const [data, setData] = useState([]);
    const { width } = useWindowDimensions();
    const numColumns = width < 768 ? 1 : 3;

    type plant = {
        id: string,
        name : string,
        urlImage : string,
    }

    useEffect(() => {
        fetch('http://localhost:8080/api/plant')
            .then((response) => response.json())
            .then((data) => setData(data))
            .catch((error) => console.error("Erreur lors de la récupération des plantes", error));
    }, []);

    const renderItem = ({ item }: { item:  plant }) =>  (
        <PlantHeader
            id={item.id}
            name={item.name}
            urlImage={item.urlImage}
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
        margin: 4,
    },
});
