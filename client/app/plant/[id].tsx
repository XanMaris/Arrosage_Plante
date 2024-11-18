import {ThemedView} from "@/components/ThemedView";
import {ThemedText} from "@/components/ThemedText";
import {Button, StyleSheet} from "react-native";

export default function PlantDetail() {
    return (
        <>
            <Stack.Screen options={{title: "Détail d'une plante"}}/>
            <ThemedView style={styles.container}>
                <div style={styles.header}>
                    <img style={styles.headerImage}
                         src="https://fastly.picsum.photos/id/305/4928/3264.jpg?hmac=s2FLjeAIyYH0CZl3xuyOShFAtL8yEGiYk31URLDxQCI"
                         alt="Image d'illustration de la plante"/>
                    <div style={styles.humidityRate}></div>
                </div>

                <ThemedText type="subtitle">PlantDetail</ThemedText>
                <ThemedText type="default">
                    Lorem ipsum dolor sit amet, consectetur adipisicing elit. Cumque dolorem id maxime mollitia odio
                    quam sed sint. Consequatur doloremque error nemo neque perferendis quod sint sunt? A eligendi esse
                    sint!
                </ThemedText>

                <label>
                    <ThemedText type="defaultSemiBold">Arrosage automatique</ThemedText>
                    <input type="checkbox"/>
                </label>

                <label style={styles.labelWithInput}>
                    <ThemedText type="defaultSemiBold">Taux d'humidité journalier</ThemedText>
                    <input type="number" readOnly={true} value={50} style={styles.input}></input>
                </label>

                <div style={styles.waterButton}>
                    <Button title={"Arroser"}></Button>
                </div>
            </ThemedView>
        </>
    );
}

import {Stack} from "expo-router";

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
        justifyContent: 'center'
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
        maxWidth: "50%",
        objectFit: 'cover',
        height: 300,
        borderRadius: 8,
    },
    humidityRate: {
        height: 300,
        width: 50,
        backgroundColor: 'blue',
    },
    waterButton: {
        position: 'absolute',
        bottom: 8,
        right: 8,
    }
});