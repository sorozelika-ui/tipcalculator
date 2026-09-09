# 💰 Tip Calculator — Calculateur de pourboire

## 📌 Présentation du projet

**Tip Calculator** est une application Android développée avec **Kotlin** et **Jetpack Compose**.

L'objectif de cette application est de permettre à un utilisateur de calculer facilement le montant d'un **pourboire** à partir du montant d'une facture et d'un pourcentage choisi.

L'utilisateur peut également choisir d'**arrondir le montant du pourboire à l'entier supérieur** grâce à un bouton bascule (`Switch`).

Ce projet a été réalisé dans le cadre de l'apprentissage du développement d'applications Android avec **Jetpack Compose**.

---

# 🎯 Objectif de l'application

L'application permet à l'utilisateur de :

1. Saisir le montant de sa facture.
2. Saisir le pourcentage du pourboire.
3. Calculer automatiquement le montant du pourboire.
4. Activer ou désactiver l'arrondissement du pourboire.
5. Afficher le résultat sous forme monétaire.

### Exemple

Si la facture est de :

```text
20 000
```

et que le pourcentage du pourboire est :

```text
15 %
```

le calcul est :

```text
Pourboire = 20 000 × 15 / 100

Pourboire = 3 000
```

Le résultat affiché sera donc :

```text
3 000
```

---

# 🛠️ Technologies utilisées

Le projet utilise les technologies suivantes :

| Technologie     | Utilisation                         |
| --------------- | ----------------------------------- |
| Kotlin          | Langage de programmation            |
| Android Studio  | Environnement de développement      |
| Jetpack Compose | Création de l'interface utilisateur |
| Material 3      | Composants et design de l'interface |
| Android SDK     | Développement Android               |
| NumberFormat    | Formatage du résultat en monnaie    |
| ceil()          | Arrondissement à l'entier supérieur |

---

# 📱 Fonctionnement de l'application

L'interface contient principalement quatre éléments :

```text
┌─────────────────────────────────────┐
│        Calculate Tip                │
│                                     │
│  💰 Bill amount                     │
│  ┌───────────────────────────────┐  │
│  │ Montant de la facture         │  │
│  └───────────────────────────────┘  │
│                                     │
│  %  How was the service?            │
│  ┌───────────────────────────────┐  │
│  │ Pourcentage du pourboire      │  │
│  └───────────────────────────────┘  │
│                                     │
│  Round up tip                 OFF   │
│                                     │
│                 $3.00               │
└─────────────────────────────────────┘
```

L'interface est construite avec **Jetpack Compose**, ce qui signifie qu'elle est créée directement en Kotlin à l'aide de fonctions `@Composable`.

---

# 🧩 Structure générale du programme

Le programme est organisé autour de plusieurs fonctions importantes :

```text
MainActivity
     │
     ▼
TipTimeLayout()
     │
     ├── EditNumberField()
     │       ├── Montant de la facture
     │       └── Pourcentage du pourboire
     │
     ├── RoundTheTipRow()
     │       └── Switch d'arrondissement
     │
     └── calculateTip()
             ├── Calcul du pourboire
             ├── Arrondissement
             └── Formatage monétaire
```

Chaque fonction possède donc une responsabilité précise.

---

# 1️⃣ MainActivity

`MainActivity` est le point d'entrée principal de l'application Android.

```kotlin
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}
```

## 🔎 Explication

### `onCreate()`

La méthode `onCreate()` est appelée lorsque l'activité est créée.

### `setContent`

```kotlin
setContent {
```

Cette fonction permet de définir l'interface de l'application avec **Jetpack Compose**.

### `TipCalculatorTheme`

```kotlin
TipCalculatorTheme {
```

Elle permet d'appliquer le thème graphique de l'application.

### `Surface`

```kotlin
Surface(
    modifier = Modifier.fillMaxSize()
)
```

`Surface` fournit une surface sur laquelle l'interface est affichée.

### `TipTimeLayout()`

```kotlin
TipTimeLayout()
```

C'est le composable principal qui contient toute l'interface du calculateur.

---

# 2️⃣ TipTimeLayout()

La fonction `TipTimeLayout()` contient la logique principale de l'application.

```kotlin
@Composable
fun TipTimeLayout() {
```

Elle contient notamment les **états** de l'application.

---

# 3️⃣ Gestion des états

L'application utilise trois variables principales.

## Montant de la facture

```kotlin
var amountInput by remember { mutableStateOf("") }
```

Cette variable contient ce que l'utilisateur saisit dans le champ du montant de la facture.

Au départ :

```text
amountInput = ""
```

Si l'utilisateur écrit :

```text
20000
```

alors :

```text
amountInput = "20000"
```

Il s'agit d'une chaîne de caractères (`String`) car les données saisies dans un `TextField` sont des textes.

---

## Pourcentage du pourboire

```kotlin
var tipInput by remember { mutableStateOf("") }
```

Cette variable contient le pourcentage saisi par l'utilisateur.

Par exemple :

```text
tipInput = "15"
```

---

## Option d'arrondissement

```kotlin
var roundUp by remember { mutableStateOf(false) }
```

Cette variable indique si l'utilisateur souhaite arrondir le pourboire.

Deux possibilités :

```text
false → arrondissement désactivé
true  → arrondissement activé
```

Au lancement de l'application :

```text
roundUp = false
```

Le bouton est donc désactivé.

---

# 4️⃣ Pourquoi utiliser remember et mutableStateOf ?

Jetpack Compose fonctionne avec un système de **gestion d'état**.

```kotlin
remember { mutableStateOf(...) }
```

permet de conserver une valeur pendant les recompositions de l'interface.

Par exemple :

```kotlin
var amountInput by remember { mutableStateOf("") }
```

Lorsque l'utilisateur saisit un nouveau montant, la valeur change.

Compose détecte cette modification et **recompose automatiquement l'interface**.

C'est ce qui permet au résultat de se mettre à jour automatiquement.

---

# 5️⃣ Conversion des données

Les valeurs provenant des `TextField` sont des chaînes de caractères.

Par exemple :

```text
amountInput = "20000"
tipInput = "15"
```

Pour effectuer un calcul mathématique, il faut convertir ces valeurs en `Double`.

Le programme utilise :

```kotlin
val amount = amountInput.toDoubleOrNull() ?: 0.0
```

et :

```kotlin
val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
```

## Pourquoi `toDoubleOrNull()` ?

Cette fonction essaie de convertir le texte en nombre décimal.

Exemple :

```text
"20000" → 20000.0
```

Mais si l'utilisateur saisit quelque chose qui n'est pas un nombre :

```text
"abc"
```

la conversion échoue.

`toDoubleOrNull()` retourne alors `null`.

Le programme utilise :

```kotlin
?: 0.0
```

pour remplacer `null` par `0.0`.

Cela évite une erreur dans l'application.

---

# 6️⃣ Calcul du pourboire

Le résultat est calculé avec :

```kotlin
val tip = calculateTip(amount, tipPercent, roundUp)
```

La fonction `calculateTip()` reçoit donc trois informations :

```text
amount
    ↓
montant de la facture

tipPercent
    ↓
pourcentage du pourboire

roundUp
    ↓
faut-il arrondir ?
```

---

# 7️⃣ Fonction calculateTip()

La fonction principale du calcul est :

```kotlin
private fun calculateTip(
    amount: Double,
    tipPercent: Double = 15.0,
    roundUp: Boolean
): String {

    var tip = tipPercent / 100 * amount

    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }

    return NumberFormat
        .getCurrencyInstance()
        .format(tip)
}
```

---

# 8️⃣ Explication du calcul

La formule utilisée est :

```text
Pourboire = Pourcentage × Montant / 100
```

Dans le code :

```kotlin
var tip = tipPercent / 100 * amount
```

### Exemple

```text
amount = 20 000
tipPercent = 15
```

Le calcul devient :

```text
tip = 15 / 100 × 20 000

tip = 0,15 × 20 000

tip = 3 000
```

Le pourboire est donc de :

```text
3 000
```

---

# 9️⃣ Pourquoi `tip` est un `var` ?

Le code utilise :

```kotlin
var tip
```

et non :

```kotlin
val tip
```

La raison est que la valeur peut être modifiée lorsque l'utilisateur active l'arrondissement.

Par exemple :

```kotlin
var tip = ...
```

puis :

```kotlin
tip = kotlin.math.ceil(tip)
```

La variable `tip` reçoit donc une nouvelle valeur.

Une variable déclarée avec `val` ne peut pas être réassignée.

---

# 🔟 Arrondissement avec ceil()

L'arrondissement est effectué avec :

```kotlin
if (roundUp) {
    tip = kotlin.math.ceil(tip)
}
```

La condition signifie :

```text
SI roundUp est vrai
ALORS arrondir le pourboire
```

La fonction :

```kotlin
ceil()
```

arrondit toujours vers l'entier supérieur.

### Exemples

```text
10,2 → 11
10,7 → 11
15,1 → 16
20,0 → 20
```

---

# 1️⃣1️⃣ Le bouton Switch

Le bouton permettant d'activer l'arrondissement est créé dans :

```kotlin
RoundTheTipRow()
```

Le composant est appelé ainsi :

```kotlin
RoundTheTipRow(
    roundUp = roundUp,
    onRoundUpChanged = {
        roundUp = it
    },
    modifier = Modifier.padding(bottom = 32.dp)
)
```

Le composant reçoit deux éléments importants :

```kotlin
roundUp
```

et :

```kotlin
onRoundUpChanged
```

---

# 1️⃣2️⃣ State Hoisting

Le projet utilise le principe de **State Hoisting**.

L'état :

```kotlin
roundUp
```

est conservé dans le composant parent :

```text
TipTimeLayout
```

et non directement dans :

```text
RoundTheTipRow
```

Le parent transmet la valeur :

```kotlin
roundUp = roundUp
```

et transmet également une fonction permettant de modifier cette valeur :

```kotlin
onRoundUpChanged = {
    roundUp = it
}
```

Cela permet de garder la gestion de l'état dans le composant parent.

---

# 1️⃣3️⃣ RoundTheTipRow()

Cette fonction crée la ligne contenant le texte et le bouton bascule.

```kotlin
@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
)
```

Elle utilise un `Row` :

```kotlin
Row(
    modifier = modifier
        .fillMaxWidth()
        .size(48.dp),
    verticalAlignment = Alignment.CenterVertically
)
```

Le `Row` permet de placer les éléments horizontalement.

Il contient :

```text
Round up tip                         Switch
```

---

# 1️⃣4️⃣ Fonctionnement du Switch

Le bouton est défini avec :

```kotlin
Switch(
    checked = roundUp,
    onCheckedChange = onRoundUpChanged
)
```

### `checked`

```kotlin
checked = roundUp
```

indique l'état actuel du bouton.

Si :

```text
roundUp = false
```

le bouton est désactivé.

Si :

```text
roundUp = true
```

le bouton est activé.

### `onCheckedChange`

```kotlin
onCheckedChange = onRoundUpChanged
```

Cette fonction est appelée lorsque l'utilisateur clique sur le bouton.

---

# 1️⃣5️⃣ Champ de saisie réutilisable

Le projet possède une fonction :

```kotlin
EditNumberField()
```

Elle permet de créer les champs de saisie.

```kotlin
@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
)
```

Cette fonction est réutilisable pour plusieurs champs.

Elle évite donc de répéter le même code.

---

# 1️⃣6️⃣ TextField

À l'intérieur de `EditNumberField()`, on utilise :

```kotlin
TextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    singleLine = true,
    label = {
        Text(stringResource(label))
    },
    keyboardOptions = keyboardOptions,
    leadingIcon = {
        Icon(
            painter = painterResource(id = leadingIcon),
            null
        )
    }
)
```

### `value`

Contient la valeur actuelle du champ.

### `onValueChange`

Est appelé chaque fois que l'utilisateur modifie le texte.

### `singleLine`

```kotlin
singleLine = true
```

empêche le champ de devenir un champ multiligne.

### `label`

Permet d'afficher le texte descriptif du champ.

### `leadingIcon`

Permet d'afficher une icône à gauche du champ.

---

# 1️⃣7️⃣ Configuration du clavier

Pour le montant de la facture :

```kotlin
keyboardOptions = KeyboardOptions.Default.copy(
    keyboardType = KeyboardType.Number,
    imeAction = ImeAction.Next
)
```

Cela permet d'afficher un clavier adapté aux nombres.

L'action :

```kotlin
ImeAction.Next
```

permet de passer au champ suivant.

Pour le pourcentage :

```kotlin
keyboardOptions = KeyboardOptions.Default.copy(
    keyboardType = KeyboardType.Number,
    imeAction = ImeAction.Done
)
```

L'action :

```kotlin
ImeAction.Done
```

indique que l'utilisateur a terminé sa saisie.

---

# 1️⃣8️⃣ Affichage du résultat

Le résultat est affiché avec :

```kotlin
Text(
    text = stringResource(R.string.tip_amount, tip),
    style = MaterialTheme.typography.displaySmall
)
```

La variable :

```kotlin
tip
```

contient le résultat calculé.

L'interface est donc automatiquement mise à jour lorsque :

* le montant change ;
* le pourcentage change ;
* l'utilisateur active le `Switch`.

---

# 1️⃣9️⃣ Formatage monétaire

Le résultat est transformé en format monétaire avec :

```kotlin
NumberFormat
    .getCurrencyInstance()
    .format(tip)
```

Cela permet d'afficher le nombre sous une forme adaptée à la monnaie du système.

Par exemple, selon la configuration régionale de l'appareil :

```text
$3.00
```

ou un autre format monétaire.

---

# 🔄 Cycle complet de fonctionnement

Le fonctionnement de l'application peut être résumé ainsi :

```text
L'utilisateur saisit le montant
              ↓
       amountInput
              ↓
    Conversion en Double
              ↓
           amount
              ↓
L'utilisateur saisit le pourcentage
              ↓
          tipInput
              ↓
    Conversion en Double
              ↓
        tipPercent
              ↓
      calculateTip()
              ↓
   Calcul du pourboire
              ↓
    roundUp est vérifié
              ↓
       Si true → ceil()
              ↓
     NumberFormat()
              ↓
      Résultat affiché
```

---

# 📊 Exemple complet

Supposons que l'utilisateur saisisse :

```text
Montant de la facture : 15 000
Pourcentage : 10 %
Arrondir : Désactivé
```

Le programme effectue :

```text
15 000 × 10 / 100
```

Résultat :

```text
1 500
```

Maintenant, si le résultat était :

```text
1 502,40
```

et que l'utilisateur activait :

```text
Round up tip
```

le programme utiliserait :

```kotlin
ceil(1502.40)
```

pour obtenir :

```text
1 503
```

---

# 📜 Gestion du défilement

La colonne principale utilise :

```kotlin
.verticalScroll(rememberScrollState())
```

Cette fonctionnalité permet à l'utilisateur de faire défiler l'écran lorsque le contenu ne tient pas entièrement sur l'écran.

---

# 🎨 Organisation de l'interface

L'interface principale utilise un `Column` :

```kotlin
Column(
    modifier = Modifier
        .statusBarsPadding()
        .padding(horizontal = 40.dp)
        .verticalScroll(rememberScrollState()),

    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
)
```

Le `Column` permet d'organiser verticalement :

```text
Titre
 ↓
Champ montant
 ↓
Champ pourcentage
 ↓
Switch
 ↓
Résultat
```

---

# 👁️ Preview

Le projet possède également une fonction `Preview` :

```kotlin
@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipCalculatorTheme {
        TipTimeLayout()
    }
}
```

Elle permet de visualiser l'interface directement dans Android Studio sans avoir besoin de lancer l'application sur un émulateur.

---

# 📂 Organisation logique du code

```text
MainActivity.kt
│
├── MainActivity
│
├── TipTimeLayout()
│   │
│   ├── Gestion des états
│   ├── Conversion des données
│   ├── EditNumberField()
│   ├── RoundTheTipRow()
│   └── Affichage du résultat
│
├── EditNumberField()
│   └── TextField
│
├── RoundTheTipRow()
│   └── Switch
│
├── calculateTip()
│   ├── Calcul
│   ├── Arrondissement
│   └── Formatage monétaire
│
└── TipTimeLayoutPreview()
```

---

# 🚀 Installation et exécution

## Prérequis

Pour exécuter le projet, il faut disposer de :

* Android Studio ;
* JDK ;
* Android SDK ;
* un émulateur Android ou un téléphone Android.

## Installation

1. Ouvrir **Android Studio**.
2. Ouvrir le projet `TipCalculator`.
3. Attendre la synchronisation de Gradle.
4. Sélectionner un appareil Android ou un émulateur.
5. Cliquer sur **Run ▶️**.
6. L'application démarre.

---

# 🧪 Tests à effectuer

Pour vérifier que l'application fonctionne correctement, plusieurs tests peuvent être réalisés.

### Test 1 — Calcul simple

```text
Facture : 10 000
Pourboire : 10 %
Arrondissement : OFF
```

Résultat attendu :

```text
1 000
```

### Test 2 — Pourcentage différent

```text
Facture : 20 000
Pourboire : 20 %
Arrondissement : OFF
```

Résultat attendu :

```text
4 000
```

### Test 3 — Arrondissement activé

```text
Facture : 10 000
Pourboire : 15,5 %
Arrondissement : ON
```

Calcul :

```text
10 000 × 15,5 / 100
= 1 550
```

Le résultat reste :

```text
1 550
```

Si le résultat contient des décimales, `ceil()` l'arrondira à l'entier supérieur.

---

# 📚 Concepts appris grâce à ce projet

Ce projet m'a permis de comprendre et de mettre en pratique plusieurs concepts importants du développement Android :

* création d'une interface avec **Jetpack Compose** ;
* utilisation des fonctions `@Composable` ;
* gestion des états ;
* `remember` ;
* `mutableStateOf` ;
* recomposition ;
* State Hoisting ;
* création de composants réutilisables ;
* utilisation de `TextField` ;
* utilisation de `Switch` ;
* gestion du clavier ;
* conversion `String` → `Double` ;
* calcul mathématique ;
* utilisation de `ceil()` ;
* formatage monétaire avec `NumberFormat` ;
* utilisation de `Modifier` ;
* utilisation de `Column` et `Row` ;
* création d'un `Preview`.

---

# 💡 Ce que j'ai particulièrement appris

Le concept important de ce projet est la **gestion de l'état dans Jetpack Compose**.

Par exemple :

```kotlin
var roundUp by remember { mutableStateOf(false) }
```

Lorsque l'utilisateur active le bouton :

```text
roundUp = false
        ↓
Utilisateur clique sur Switch
        ↓
roundUp = true
        ↓
Compose détecte le changement
        ↓
calculateTip() est recalculée
        ↓
Le résultat est mis à jour
```

Cela montre le principe fondamental de Jetpack Compose :

> Lorsque l'état de l'application change, l'interface utilisateur se met automatiquement à jour.

---

# 👩🏽‍💻 Auteur

**Zelika Soro**

Projet réalisé dans le cadre de l'apprentissage du développement d'applications Android avec **Kotlin et Jetpack Compose**.

---

# 📌 Conclusion

Le projet **Tip Calculator** est une application simple, mais il permet de comprendre plusieurs notions fondamentales du développement Android moderne.

L'application ne se limite pas au calcul d'un pourboire. Elle permet surtout de comprendre comment :

* récupérer les données saisies par l'utilisateur ;
* stocker ces données dans un état ;
* transformer les données pour effectuer un calcul ;
* modifier le résultat selon une option choisie ;
* afficher automatiquement le résultat dans l'interface ;
* créer des composants réutilisables ;
* séparer la gestion de l'état de l'affichage.

Ce projet constitue ainsi une bonne introduction à la création d'interfaces Android modernes avec **Jetpack Compose et Kotlin**.
