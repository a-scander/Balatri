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


L'IA
startGame() uses a tight while (true) loop -> la boucle while arrête l'execution mauvais MVC

In a GUI-based MVC design, control should be event-driven rather than a blocking loop.
The view should drive user actions and the controller should react, instead of the controller continuously looping.

GameState exposes getSelectedCards(), getHand(), getDeck(), etc. -> La logic de certaines fonctions devrait se trouver dans GameState et se faire appeler dans Controller
Business logic partly in controller -> L'utilisation de HandEvaluator est uniquement à faire dans gameState


emit(AppEvent event, state) couples view updates to controller -> emit fonctionne correctement mais pourrait être améliorer en n'envoyant uniquement ce qui à changer dans le state plutot que le state en entier  
It works, but a cleaner MVC design would let the view observe model changes or receive more focused events rather than being passed the whole state every time.
