import {Button, Pressable, View, ViewProps} from "react-native";
import {useThemeColor} from "@/hooks/useThemeColor";
import {ThemedText} from "@/components/ThemedText";

import {StyleSheet} from 'react-native';
import {Link} from "expo-router";


type PlantProps = {
    id: string,
    name: string,
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
                                ...otherProps
                            }: ThemedViewProps) {
    const backgroundColor = useThemeColor({light: lightColor, dark: darkColor}, 'background');
    const urlImage = `http://localhost:8080/api/plant/${id}/image`;

    return (
        <View style={[{backgroundColor}, style, styles.container]} {...otherProps}>
            <img src={urlImage || "https://fastly.picsum.photos/id/305/4928/3264.jpg?hmac=s2FLjeAIyYH0CZl3xuyOShFAtL8yEGiYk31URLDxQCI"} alt={name}/>

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