import {Button, Pressable, View, ViewProps} from "react-native";
import {useThemeColor} from "@/hooks/useThemeColor";
import {ThemedText} from "@/components/ThemedText";

import {StyleSheet} from 'react-native';
import {Link} from "expo-router";


type PlantProps = {
    id: string,
    name: string,
    humidityRate: number,
    imageUrl: string
}

export type ThemedViewProps = ViewProps & {
    lightColor?: string;
    darkColor?: string;
} & PlantProps;

export function PlantHeader({
                                style,
                                lightColor,
                                darkColor,
                                id,
                                name,
                                humidityRate,
                                imageUrl,
                                ...otherProps
                            }: ThemedViewProps) {
    const backgroundColor = useThemeColor({light: lightColor, dark: darkColor}, 'background');

    const humidityRateColor = humidityRate > 60 ? "red" : "white";

    return (
        <View style={[{backgroundColor}, style, styles.container]} {...otherProps}>
            <ThemedText style={styles.humidityRate} type="defaultSemiBold">
                Taux d'humidité: <span color={humidityRateColor}>{humidityRate}%</span>
            </ThemedText>
            <img src={imageUrl} alt={name}/>

            <div style={styles.bottomAction}>
                <ThemedText type="subtitle">{name}</ThemedText>

                <Link href={{pathname: '/plant/[id]', params: {id: id}}}>
                    <ThemedText type="link">Voir la plante</ThemedText>
                </Link>
            </div>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        position: "relative",
        display: "flex",
        flexDirection: "column",
        gap: 8,
    },
    humidityRate: {
        backgroundColor: "rgb(10,110,110)",
        borderRadius: 4,
        padding: 4,
        position: "absolute",
        top: 8,
        right: 8,
    },
    plantTitle: {
        fontSize: 24,
        fontWeight: "bold",
        textAlign: "center",
    },
    button: {
        backgroundColor: "rgb(10,110,110)",
        padding: 8,
        borderRadius: 4,
        alignItems: "center",
        width: "50%",
    },
    bottomAction: {
        display: "flex",
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "space-between",
    }
});