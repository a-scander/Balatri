Tasks:

- GameController (revoir la responsabilité de certaines fonctions)
- Zen6View (A enrichir)


- Les planètes 
- Les jokers -> trois phases d'évaluation de la main
- Les extensions(extension 2 Fait !)
- Corriger l'affichage double au début de la console
- Traduire tous les messages console en anglais
- Remplacer toutes les valeurs brutes par des constantes
- Ajouter des commentaires sur toutes les classes et méthodes
- Gérer toutes les erreurs (requireNonNull, exceptions)
- Définir des blinds cohérents(valeurs actuelles = test) -> blindes infinis
- Etats de jeu (Menu de démarrage/En Jeu, Shop(Entre Blind)/En Blind/En pause)

GameController:
startBlind: this is only a test, should be replaced by the actual blind initialization logic
onAction: blind changing logic and shop
onAction: calculate money won and apply jokers that execute on last hand

GameState.java:
Line 57: if size is good select else message
Line 70: appliquer les jokers qui s'executent avant
Line 71: return a handresult with the handtype and the scoring cards
Line 76: routine de scoring des cards et des jokers qui s'executent pendant
Line 77: appliquer les jokers qui s'executent apres
Line 109: saving the current state and game

Zen6View.java:
Line 28: make a constructor instead