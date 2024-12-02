// Définir les pins utilisés
const int pumpPin = 14;        // Contrôle de la pompe via un relais ou un MOSFET sur GPIO14

// Paramètres de la pompe
const float waterFlowRate = 0.077;  // Débit de la pompe en litres par seconde (ajustez selon votre pompe)

// Paramètres liés à la terre
const float soilWaterRetentionFactor = 0.75;  // Facteur de rétention d'eau du sol (par exemple, 75% de la capacité du sol est utilisée)


// Fonction pour arroser
void arroser(float potVolume, float humidityTarget, float humidityInitial) {
  // Calcul de la différence d'humidité
  float humidityDifference = humidityTarget - humidityInitial;

  // Si l'humidité cible est déjà atteinte ou dépassée, on ne fait rien
  if (humidityDifference <= 0) {
    Serial.println("Humidité cible déjà atteinte, pas besoin d'ajouter d'eau.");
    return;
  }

  // Calcul du volume d'eau nécessaire en fonction de la différence d'humidité
  float waterNeeded = (humidityDifference / soilWaterRetentionFactor) * potVolume;

  // Ajuster le volume d'eau en fonction de la capacité de rétention du sol

  // Afficher le volume d'eau à ajouter
  Serial.print("Volume d'eau à ajouter : ");
  Serial.print(waterNeeded);
  Serial.println(" litres");

  // Calcul du temps d'arrosage nécessaire
  unsigned long pumpDuration = waterNeeded / waterFlowRate * 1000; // Durée en millisecondes

  // Activer la pompe pour ajouter de l'eau
  digitalWrite(pumpPin, HIGH);  // Allumer la pompe
  delay(pumpDuration);          // Laisser la pompe fonctionner pendant le temps calculé
  digitalWrite(pumpPin, LOW);   // Éteindre la pompe

  // Afficher un message après avoir arrosé
  Serial.println("Arrosage terminé.");
}


void setup() {
  // Initialisation de la communication série
  Serial.begin(115200);

  // Configurer le pin de la pompe comme sortie
  pinMode(pumpPin, OUTPUT);
  digitalWrite(pumpPin, LOW);  // Assurez-vous que la pompe est éteinte au départ
}

void loop() {
  // Exemple d'appel de la fonction arroser
  float potVolume = 1.0;  // Volume du pot en litres
  float humidityTarget = 0.60;  // Humidité cible (en pourcentage)
  float humidityInitial = 0.40; // Humidité initiale (en pourcentage)

  // Appeler la fonction arroser
  arroser(potVolume, humidityTarget, humidityInitial);

  // Attendre 10 secondes avant le prochain arrosage (ou ajustez selon votre logique)
  delay(10000);
}
