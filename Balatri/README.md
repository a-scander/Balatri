Pour compiler le projet:
```make build```  

Pour compiler et run la version par défaut:
```make run```  

Pour run le projet compilé avec des arguments:
```java -cp "../lib/zen-6.0.jar:../bin" Main $args```  

arguments:

    - console: Pour ne lancer que la vue console  
    - zen: Pour ne lancer que la vue zen  


Tasks done:  
- Cards, Deck, Blinds, and other domain classes
- Main event system with controller
- GamState which holds the blinds and game logic
- View interface, ConsoleView and Zen6View
- Planets logic and computation logic
- Active discard without inputs connexion
- Placeholders in GameState, Blind and HandEvaluator to welcome Hand modifying Jokers 

Remaining Tasks:

- Les jokers -> trois phases d'évaluation de la main
- Les extensions(extension 2 Fait !)
- Traduire tous les messages console en anglais
- Remplacer toutes les valeurs brutes par des constantes
- Ajouter des commentaires sur toutes les classes et méthodes
- Gérer toutes les erreurs (requireNonNull, exceptions)
- Définir des blinds cohérents(valeurs actuelles = test) -> blindes infinis

GameController:  
startBlind: this is only a test, should be replaced by the actual blind initialization logic  
onAction: shop  
onAction: calculate money won and apply jokers that execute on last hand   

GameState.java:  
Line 57: if size is good select else message  
Line 70: appliquer les jokers qui s'executent avant  
Line 71: return a handresult with the handtype and the scoring cards  
Line 76: routine de scoring des cards et des jokers qui s'executent pendant  
Line 77: appliquer les jokers qui s'executent apres  
Line 109: saving the current state and game  
