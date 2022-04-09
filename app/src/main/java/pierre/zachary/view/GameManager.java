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

    public GameManager(GameObject gameObject) {
        super(gameObject);
        jeu = new Facade(this, this);
        grid = jeu.Level9x9();
        GameObject affichageScore = new GameObject(this.gameObject.scene, "Score");
        affichageScore.transform.positionX = 0f;
        affichageScore.transform.scaleX = 3;
        affichageScore.transform.scaleY = 3;
        affichageScore.transform.positionY = 5f;
        affichageScore.transform.anchorPoint = TransformAnchorPoint.Center;
        scoreTextRenderer = new TextRenderer(affichageScore,"SCORE : 0", Color.valueOf(Color.WHITE));
        affichageScore.addComponent(scoreTextRenderer);

        for(int i = 0; i<grid.getGridSize(); i++){
            for(int j=0; j< grid.getGridSize(); j++){
                float start = -(Camera.main.getSize()/2f);
                GameObject caseGO = new GameObject(this.gameObject.scene, "Case "+i+":"+j);
                gridGO.add(caseGO);
                caseGO.transform.positionX = start+i+0.5f; // 0.5 car le transform est au centre du gameobject
                caseGO.transform.positionY = start+j+0.5f;
                caseGO.addComponent(new SpriteRenderer(caseGO, R.drawable.resource_case));
                caseGO.addComponent(new SpriteCollider(caseGO));
                caseGO.addComponent(new OnClickCallBackBehaviour(caseGO, new Function<GameObject, String>() {
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
//            else{
//                // si pas dans la scene : le remettre ?
//                //entry.getValue().transform.positionX+=0.1f;
//            }
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
                    if(!pionsGameObjectHashMap.containsKey(p)){
                        System.out.println("Pions "+i+":"+j);
                        GameObject pionsGO = new GameObject(this.gameObject.scene, "Pions "+i+":"+j);
                        pionsGO.transform.positionX = start+i+0.5f; // 0.5 car le transform est au centre du gameobject
                        pionsGO.transform.positionY = start+j+0.5f;
                        pionsGO.addComponent(new SpriteRenderer(pionsGO, this.getImageRessource(p)));
                        pionsGO.addComponent(new SpriteCollider(pionsGO));
                        pionsGO.addComponent(new OnClickCallBackBehaviour(pionsGO, new Function<GameObject, String>() {
                            @Override
                            public String apply(GameObject gameObject) {
                                Pions p = getPions(gameObject);
                                if(p!=null){
                                    selectedPion = p;
                                    if(case_selector!=null){
                                        case_selector.scene.remove(case_selector);
                                    }
                                    case_selector = new GameObject(gameObject.scene, "Case selector");
                                    case_selector.addComponent(new SpriteRenderer(case_selector, R.drawable.case_select));
                                    case_selector.addComponent(new SpriteCollider(case_selector));
                                    case_selector.transform.positionX = gameObject.transform.positionX;
                                    case_selector.transform.positionY = gameObject.transform.positionY;
                                }

                                return null;
                            }
                        }));
                        pionsGameObjectHashMap.put(p, pionsGO);
                    }
                    else{
                        GameObject pionsGO = pionsGameObjectHashMap.get(p);
                        pionsGO.name = "Pions "+i+":"+j;
                        // TODO une animation de déplacement avec fonction de easing ?
                        pionsGO.transform.positionX = start+i+0.5f;
                        pionsGO.transform.positionY = start+j+0.5f;
                        pionsGO.scene.add(pionsGO);
                    }
                }
            }
        }
        if(selectedPion != null && !grid.containsPions(selectedPion)){
            GameObject pionsGo = pionsGameObjectHashMap.get(selectedPion);
            pionsGo.scene.remove(pionsGo);
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
        // TODO
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
