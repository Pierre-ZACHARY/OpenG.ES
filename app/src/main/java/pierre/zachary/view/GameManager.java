package pierre.zachary.view;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

import android.graphics.Color;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.R;
import pierre.zachary.modele.Drawer;
import pierre.zachary.modele.Facade;
import pierre.zachary.modele.Grid;
import pierre.zachary.modele.Pions;
import pierre.zachary.modele.Position;
import pierre.zachary.modele.Score;
import pierre.zachary.modele.exception.NoPossiblePath;
import pierre.zachary.modele.exception.OutofBounds;
import pierre.zachary.modele.exception.PionsNotInGrid;
import pierre.zachary.modele.exception.TargetNotEmpty;

public class GameManager extends MonoBehaviour implements Score, Drawer {

    Facade jeu;
    Grid grid;

    List<GameObject> gridGO = new ArrayList<>();
    GameObject case_selector;

    Pions selectedPion;

    TextRenderer scoreTextRenderer;

    int score = 0;

    int level;

    public GameManager(int level) {
        this.level = level;
    }

    public void Start(){
        jeu = new Facade(this, this);
        switch (level){
            default:
                grid = jeu.Level9x9();
                break;
            case 2:
                grid = jeu.Level7x7();
                break;
        }
        for(int i = 0; i<grid.getGridSize(); i++){
            for(int j=0; j< grid.getGridSize(); j++){
                float start = -(Camera.main.getSize()/2f);
                GameObject caseGO = new GameObject(this.gameObject.scene, "Case "+i+":"+j);
                gridGO.add(caseGO);
                caseGO.transform.positionX = start+i+0.5f; // 0.5 car le transform est au centre du gameobject
                caseGO.transform.positionY = start+j+0.5f;
                caseGO.addComponent(new SpriteRenderer(R.drawable.resource_case));
                caseGO.addComponent(new SpriteCollider());
                caseGO.addComponent(new OnClickCallBackBehaviour(new Function<GameObject, String>() {
                    @Override
                    public String apply(GameObject gameObject) {
                        if(case_selector != null && (case_selector.transform.positionX != gameObject.transform.positionX || case_selector.transform.positionY != gameObject.transform.positionY)){
                            case_selector.scene.remove(case_selector);
                            if(selectedPion!=null){
                                int index = gridGO.indexOf(gameObject);
                                int x = index / grid.getGridSize();
                                int y = index % grid.getGridSize();
                                try {
                                    jeu.moove(selectedPion, new Position(x, y));
                                    selectedPion = null;
                                } catch (TargetNotEmpty | NoPossiblePath | PionsNotInGrid targetNotEmpty) {
                                    targetNotEmpty.printStackTrace();
                                }
                            }
                        }
                        return null;
                    }
                }));

            }
        }
        GameObject titreNiveau = new GameObject(this.gameObject.scene, "Score");
        titreNiveau.transform.positionX = 0f;
        titreNiveau.transform.scaleX = 5;
        titreNiveau.transform.scaleY = 5;
        titreNiveau.transform.positionY = ((int) (grid.getGridSize()/2))+2;
        titreNiveau.transform.anchorPoint = TransformAnchorPoint.Center;
        TextRenderer niveauTexteRenderer = new TextRenderer("Niveau "+level, Color.valueOf(Color.WHITE), TextSize.Title);
        titreNiveau.addComponent(niveauTexteRenderer);

        GameObject affichageScore = new GameObject(this.gameObject.scene, "Score");
        affichageScore.transform.positionX = 0f;
        affichageScore.transform.scaleX = 5;
        affichageScore.transform.scaleY = 5;
        affichageScore.transform.positionY = ((int) (grid.getGridSize()/2))+1;
        affichageScore.transform.anchorPoint = TransformAnchorPoint.Center;
        scoreTextRenderer = new TextRenderer("SCORE : 0", Color.valueOf(Color.WHITE), TextSize.SubTitle);
        affichageScore.addComponent(scoreTextRenderer);

        for(int k = 0; k<3; k++){
            GameObject caseGO = new GameObject(this.gameObject.scene, "NextPionGO : "+k);
            caseGO.transform.positionX = -1+k; // 0.5 car le transform est au centre du gameobject
            caseGO.transform.positionY = ((int) -(grid.getGridSize()/2))-1.2f;
            caseGO.addComponent(new SpriteRenderer(R.drawable.resource_case));
        }

        grid.spawnNext();
    }

    @Override
    public void Draw(GL10 gl) {

        if(selectedPion != null && !grid.containsPions(selectedPion)){
            GameObject pionsGo = pionsGameObjectHashMap.get(selectedPion);
            pionsGo.scene.remove(pionsGo);
            selectedPion = null;
            case_selector.scene.remove(case_selector);
        }
        super.Draw(gl);
    }

    private int getImageRessource(Pions p){
        if(p!=null){
            switch(p.getType()){
                case 0: return R.drawable.bonbon_bleu;
                case 1: return R.drawable.bonbon_orange;
                case 2: return R.drawable.bonbon_jaune;
                case 3: return R.drawable.bonbon_rose;
                case 4: return R.drawable.bonbon_rouge;
                case 5: return R.drawable.bonbon_vert;
                case 6: return R.drawable.bonbon_violet;
                default:
                    System.out.println("Type>6");
                    return R.drawable.bonbon_bleu;
            }
        }
        System.out.println("pions null");
        return R.drawable.bonbon_bleu;
    }

    private HashMap<Pions, GameObject> pionsGameObjectHashMap = new HashMap<>();
    private HashMap<Pions, GameObject> nextPionsHashMap = new HashMap<>();

    private Pions getPions(GameObject pionsGO){
        for(Map.Entry<Pions, GameObject> entry : pionsGameObjectHashMap.entrySet()){
            if(entry.getValue() == pionsGO){
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void drawGrid(Grid g) {

        float start = -(Camera.main.getSize()/2f);

        for(Map.Entry<Pions, GameObject> entry : pionsGameObjectHashMap.entrySet()){
            if(!g.containsPions(entry.getKey())){
                entry.getValue().scene.remove(entry.getValue());
            }
        }

        for(int i = 0; i < grid.getGridSize(); i++){
            for(int j = 0; j < grid.getGridSize(); j++){
                Pions p = null;
                try {
                    p = grid.getPions(i,j);
                } catch (OutofBounds outofBounds) {
                    outofBounds.printStackTrace();
                }

                if(p!=null){
                    if(!pionsGameObjectHashMap.containsKey(p)){ // déplacement d'un nouveau pion
                        GameObject pionsGO = nextPionsHashMap.get(p);
                        if(pionsGO==null){
                            pionsGO = new GameObject(this.gameObject.scene);
                            pionsGO.name = "Pions "+i+":"+j;
                            System.out.println("Création : "+pionsGO);
                            pionsGO.transform.positionX = start+i+0.5f;
                            pionsGO.transform.positionY = start+j+0.5f;
                            pionsGO.addComponent(new SpriteRenderer(this.getImageRessource(p)));
                        }
                        pionsGO.scene.add(pionsGO);
                        pionsGO.addComponent(new SpriteCollider());
                        pionsGO.addComponent(new OnClickCallBackBehaviour(new Function<GameObject, String>() {
                            @Override
                            public String apply(GameObject gameObject) {
                                Pions p = getPions(gameObject);
                                if(p!=null){
                                    selectedPion = p;
                                    if(case_selector!=null){
                                        case_selector.scene.remove(case_selector);
                                    }
                                    case_selector = new GameObject(gameObject.scene, "Case selector");
                                    case_selector.addComponent(new SpriteRenderer(R.drawable.case_select));
                                    case_selector.addComponent(new SpriteCollider());
                                    case_selector.transform.positionX = gameObject.transform.positionX;
                                    case_selector.transform.positionY = gameObject.transform.positionY;
                                }

                                return null;
                            }}));
                        nextPionsHashMap.remove(p);
                        pionsGameObjectHashMap.put(p, pionsGO);
                    }
                    else{
                        GameObject pionsGO = pionsGameObjectHashMap.get(p);

                        assert pionsGO != null;

                        if(!pionsGO.name.equals("Pions " + i + ":" + j)){
                            // TODO une animation de déplacement avec fonction de easing ?
                            System.out.println("Moving : "+pionsGO+" to "+i+", "+j);
                            pionsGO.name = "Pions "+i+":"+j;
                            pionsGO.transform.positionX = start+i+0.5f;
                            pionsGO.transform.positionY = start+j+0.5f;
                        }
                        pionsGO.scene.add(pionsGO);

                    }
                }
            }
        }
        if(selectedPion != null && !grid.containsPions(selectedPion)){
            GameObject pionsGo = pionsGameObjectHashMap.get(selectedPion);
            pionsGo.scene.remove(pionsGo); // null ?
            selectedPion = null;
            case_selector.scene.remove(case_selector);
        }
    }


    /**
     * UNUSED
     * @param progress : progression de l'animation entre 0 et 1
     * @return double : valeur de la courbe ( 1 -> doit être à sa position finale / 0 -> doit être à sa position initial)
     */
    public double easeInSine(float progress){
        return 1 - cos((progress * PI) / 2);
    }


    @Override
    public void drawNext(List<Pions> pionsList) {
        if(grid!=null){
            for(int k = 0; k<pionsList.size(); k++){
                Pions p = pionsList.get(k);

                GameObject nextPionGO = new GameObject(this.gameObject.scene, "NextPionGO : "+k);
                nextPionGO.transform.positionX = -1+k; // 0.5 car le transform est au centre du gameobject
                nextPionGO.transform.positionY = ((int) -(grid.getGridSize()/2))-1.2f;
                nextPionGO.addComponent(new SpriteRenderer(this.getImageRessource(p)));
                nextPionsHashMap.put(p, nextPionGO);

            }
        }

    }

    @Override
    public void gameOver(Grid g) {
        System.out.println("GAME OVER");
        // TODO
    }

    @Override
    public void notifyScoreChanged(int addedScore) {
        score += addedScore;
        scoreTextRenderer.setText("SCORE : "+score);
    }
}
