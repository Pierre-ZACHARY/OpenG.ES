# Rendu Projet OPEN GL 

Lien du git : https://github.com/Pierre-ZACHARY/OpenG.ES

## Pierre ZACHARY

Pour ce projet j'ai proposé une solution pour répondre au sujet en utilisant OpenGL.

Vous trouverez avec ce README un fichier video MP4 correspondant à une démonstration du jeu.

Voici les points auxquels j'ai répondu : 
- [x] Proposer au moins un niveau de jeu avec une grille 9×9, 7 pions différents et des alignements à 5 pions (et plus). Les pions peuvent être des carrés.
  - Correspond au niveau 1 dans le jeu
  - Les 7 pions utilisent des images provenant de candy crush
- [x] Vous pouvez également améliorer en proposant d’autres niveaux par exemple débutant avec une grille 7×7, 5 pions différents et des alignements à 4 pions.
  - Correspond au niveau 2 dans le jeu
- [x] Lorsque le déplacement n’est pas possible 
  - Il y a une image de séléction qui s'affiche sur un pion lorsqu'on le sélectionne. Cette image disparait au moment du déplacement ou si celui-ci est impossible.
- [x] Intégrer un affichage particulier lorsque le taux de remplissage de la grille est trop important (+ de 70%). Vous pouvez utiliser de la lumière ou un fond diférent etc.
  - Pour ma part j'ai choisit de modifier la scène totalement ( scène de gameover ), lorsque la grille est rempli, je ne sais pas si ça compte comme une modification de background.
- [x] Proposer plusieurs représentations des pions combinant des formes différentes et des couleurs.
  - Pour l'affichage des images j'ai eu besoin d'utiliser des textures, et donc de dessiner deux triangles sur lesquels je vient poser ma texture. La gestion d'une texture se fait dans la classe "SpriteRenderer" contenu dans view.component.renderer .
- [x] Intégrer l’affichage du score.
  - Pour implémenter l'affichage du score, j'ai à nouveau utiliser des textures, sauf que celle-ci est généré via la classe Canvas de android.graphics. Cette classe ainsi que la classe Paint me permettent de générer un bitmap, chaque fois que le texte change, il ne reste alors plus qu'a chargé ce bitmap sur le gpu et à l'afficher sur mes triangles comme je le fais pour mes autres textures.
  - L'inconvénient de mon implémentation est que l'espace disponible sur la texture ne s'adapte pas au texte : celui-ci est toujours centré sur la texture, mais la texture ne s'agrandi pas si le texte dépasse. Cependant je n'ai pas eu besoin de plus d'espace pour mon implémentation.
  - Vous pouvez retrouver cela dans la classe TextRenderer contenu dans view.component.renderer

Points que j'aimerai ajouté :
  - Le jeu s'affiche correctement même après une rotation d'écran
  - 100% de l'affichage provient d'OpenGL, il n'y a pas d'éléments provenant de l'api Android native, y compris les boutons ou le texte ... 

## Explications : 

Tout d'abord pour le fonctionnement du jeu, il y a un modèle qui s'occupe de la grille de pions, de placer chaque pions aléatoirement, ou encore des déplacements de chaque pions ( via algorithme A*, vu en cours d'IA ).

Pour mon implémentation j'ai choisi de reprendre l'architecture de Unity, vu au précédent semestre. 
Ainsi le Jeu se découpe en "Scène", avec une scène de Menu, une scène principale, et une scène de game over ( voir view.scene ).
Chaque Scène possède une liste de "GameObject", correspondant chacun à des éléments du jeu, comme des cases, du texte ou encore des sprites.
Chaque GameObject possède sa matrice de Transform : sa rotation, son scale, et sa translation; Ainsi qu'une liste de "Components". 
Chaque Component peut intervenir à différent moment de l'affichage du jeu :
 - Au moment du chargement de la scène via la fonction Load(), utile pour charger les images en mémoire
 - Après le chargement de la scène via la fonction Start
 - Au moment de chaque Draw via la fonction du même nom
 - Après chaque Draw via la fonction Update 
 - Au moment du clic via la fonction OnTouchEvent
De plus on peut sélectionner la scène courante via le singleton "SceneDispatcher" ( view.scene ) qui correspond au renderer de notre (seule) activitée, il a simplement pour rôle d'appeler les méthodes : onSurfaceCreated, onSurfaceChanged, onDrawFrame et onTouchEvent sur la bonne scene ( celle courante ).

Ainsi, l'affichage du jeu se déroule de la manière suivante :
Tout d'abord l'activité est créer, l'activité principale (OpenGLES30Activity) définit sa surfaceView (MyGLSurfaceView), et la surface view définit son Renderer : SceneDispatcher.
SceneDispatcher va alors instancier toutes les scènes, et appeler onSurfaceCreated sur chaque.
Chaque scène va suite à cela instancier tous leurs gameobject, et leurs components.
Ensuite, il appelle Load sur la première scène, (Menu, par défaut).
Cette Scène va appeler Load sur tous les gameobject, qui vont à leur tour appeler Load sur tous leurs components, dont les renderer, qui vont alors charger leurs images en mémoire ( en passant par la scene via la fonction loadImage pour une ressource ou loadBitmap, pour les textRenderer par exemple ).
Une fois que le load est effectué, la fonction Start est appelée, dans cette fonction peut être définit le placement des différents gameobject, car à ce niveau là du chargement on connait les dimensions de l'écran.

Ensuite à chaque Draw, ou encore chaque Touch, on envoi l'event à la scène courante via le dispatcher, qui l'envoi à son tour à tous les gameobject et leurs components.

Lors d'un changement de scène sur le dispatcher on appelle Load sur la nouvelle Scène et on recréer les objets si nécessaire.

Pour gérer les rotations d'écrans, j'ai décider d'utiliser des scènes différentes, car les transform des gameobject qu'elles contiennent ne sont pas forcément identique, et donc j'ai trouvé que c'était plus simple de changer de scène lors d'une rotation. Pour décider de qu'elle scène afficher, le dispatcher regarder le ratio width / height, et affiche soit la scène "normale", soit sa version "landscape" ( voir view.scene.landscape ).

Il est possible que certains de mes camarades aient utilisé la même approche Scène -> GameObjects -> Components après que je leur ai partagée l'idée.

Enfin j'aimerai préciser que pour ce projet utilisé GL10 et non GLES30. Je pense qu'utiliser GLES30 est possible cependant lorsque j'ai premièrement réaliser mon SpriteRenderer pour mes tests, je n'ai pas réussi à le faire via GLES30, mais avec l'expérience obtenu avec GL10 j'en serais peut être capable maintenant.
La différence principale entre les deux est que la matrice MVP est implicite avec GL10, ainsi plutot que de transmettre une matrice entre tous nos objets, on transmet directement l'instance de GL10. Alors qu'avec GLES30, on doit définir nous même cette matrice, et la transmettre entre tous les objets qui en ont besoin.
