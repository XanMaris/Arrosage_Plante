// Déclaration des broches
const int pinA2 = A2;  // Pin pour mesurer l'humidité ou la moisissure (peut être ajusté selon votre capteur)
int sensorValueA2 = 0;  // Variable pour stocker la lecture analogique du capteur A2
int sensorValueA3 = 0;  // Variable pour stocker la lecture analogique du capteur A3

// Fonction pour obtenir le pourcentage d'humidité en fonction de l'ID
float getHumidityPercentage(int sensorPin, int ID) {
  sensorValueA2 = analogRead(sensorPin);  // Lire la valeur analogique sur la broche
  float voltage = sensorValueA2 * (3.3 / 4095.0);  // Convertir la lecture en tension (pour un ESP32)
  
  // Conversion de la tension en pourcentage (cette formule dépend de votre capteur spécifique)
  float humidityPercentage = map(sensorValueA2, 0, 4095, 0, 100); // Conversion basique de 0-4095 à 0-100%
  
  // Affichage de l'ID et du pourcentage calculé
  Serial.print("Capteur ID: ");
  Serial.print(ID);
  Serial.print(" - Humidité: ");
  Serial.print(humidityPercentage);
  Serial.println("%");
  
  return humidityPercentage;  // Retourner le pourcentage d'humidité
}

void setup() {
  // Initialisation de la communication série
  Serial.begin(115200);
  
  // Définir les broches comme entrée (pas nécessaire pour l'ESP32, mais pour la clarté)
  pinMode(pinA2, INPUT);
}

void loop() {
  // Appel de la fonction avec un exemple d'ID
  float humidityA2 = getHumidityPercentage(pinA2, 1);  // ID 1 pour A2
  
  // Attendre un peu avant de refaire une mesure
  delay(2000);
}
