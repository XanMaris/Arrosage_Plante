import { StyleSheet } from 'react-native';

import { HelloWave } from '@/components/HelloWave';
import ParallaxScrollView from '@/components/ParallaxScrollView';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import {PlantHeaderList} from "@/components/PlantHeaderList";

export default function HomeScreen() {
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
});
