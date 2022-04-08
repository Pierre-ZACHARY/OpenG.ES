package pierre.zachary.view;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
import pierre.zachary.modele.Score;

public class GameManager extends MonoBehaviour implements Score, Drawer {

    Facade jeu;
    Grid grid;

    GameObject case_selector;

    public GameManager(GameObject gameObject) {
        super(gameObject);
        jeu = new Facade(this, this);
        grid = jeu.Level9x9();



        for(int i = 0; i<grid.getGridSize(); i++){
            for(int j=0; j< grid.getGridSize(); j++){
                float start = -(Camera.main.getSize()/2f);
                GameObject caseGO = new GameObject(this.gameObject.scene, "Case "+i+":"+j);
                caseGO.transform.positionX = start+i+0.5f; // 0.5 car le transform est au centre du gameobject
                caseGO.transform.positionY = start+j+0.5f;
                caseGO.addComponent(new SpriteRenderer(caseGO, R.drawable.resource_case));
                caseGO.addComponent(new SpriteCollider(caseGO));
                caseGO.addComponent(new OnClickCallBackBehaviour(caseGO, new Function<GameObject, String>() {
                    @Override
                    public String apply(GameObject gameObject) {
                        case_selector.transform.positionX = gameObject.transform.positionX;
                        case_selector.transform.positionY = gameObject.transform.positionY;
                        return null;
                    }
                }));
            }
        }
        case_selector = new GameObject(this.gameObject.scene, "Case selector");
        case_selector.addComponent(new SpriteRenderer(case_selector, R.drawable.case_select));
        case_selector.addComponent(new SpriteCollider(case_selector));
        grid.spawnNext();
    }

    private int getImageRessource(Pions p){
        if(p!=null){
            System.out.println("ici");
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

    @Override
    public void drawGrid(Grid g) {
        float start = -(Camera.main.getSize()/2f);

        for(Map.Entry<Pions, GameObject> entry : pionsGameObjectHashMap.entrySet()){
            if(!g.containsPions(entry.getKey())){
                entry.getValue().scene.remove(entry.getValue());
            }
            else{
                // si pas dans la scene : le remettre
            }
        }

        for(int i = 0; i < grid.getGridSize(); i++){
            for(int j = 0; j < grid.getGridSize(); j++){
                Pions p = grid.getPions(i,j);

                if(p!=null){
                    if(!pionsGameObjectHashMap.containsKey(p)){
                        System.out.println("Pions "+i+":"+j);
                        GameObject pionsGO = new GameObject(this.gameObject.scene, "Pions "+i+":"+j);
                        pionsGO.transform.positionX = start+i+0.5f; // 0.5 car le transform est au centre du gameobject
                        pionsGO.transform.positionY = start+j+0.5f;
                        pionsGO.addComponent(new SpriteRenderer(pionsGO, this.getImageRessource(p)));
                        pionsGO.addComponent(new SpriteCollider(pionsGO));
                        pionsGO.addComponent(new OnClickBehaviour(pionsGO));
                        pionsGameObjectHashMap.put(p, pionsGO);
                    }
                    else{
                        GameObject pionsGO = pionsGameObjectHashMap.get(p);
                        //this.MoovePosition(pionsGO, start+i+0.5f, start+j+0.5f, 2000);
                    }

                }
            }
        }

    }


    /**
     * @param progress : progression de l'animation entre 0 et 1
     * @return double : valeur de la courbe ( 1 -> doit être à sa position finale / 0 -> doit être à sa position initial)
     */
    public double easeInSine(float progress){
        return 1 - cos((progress * PI) / 2);
    }

    public void MoovePosition(GameObject pionsGO, float posX, float posY, long endMilli){
        long start = new Date().getTime();
        float positionXInitiale = pionsGO.transform.positionX;
        float positionYInitiale = pionsGO.transform.positionY;
        while(new Date().getTime() < start+endMilli){
            System.out.println("ici");
            float progress = (new Date().getTime())/((start+endMilli)*1f) ;
            pionsGO.transform.positionX = (float) (positionXInitiale+easeInSine(progress)*(posX-positionXInitiale));
            pionsGO.transform.positionY = (float) (positionYInitiale+easeInSine(progress)*(posY-positionYInitiale));
        }
    }

    @Override
    public void drawNext(List<Pions> pionsList) {

    }

    @Override
    public void gameOver(Grid g) {
        System.out.println("GAME OVER");
        // TODO
    }

    @Override
    public void notifyScoreChanged(int addedScore) {

    }
}
