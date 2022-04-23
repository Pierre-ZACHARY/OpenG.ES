package pierre.zachary.view.component.scripts;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.Preference;

import java.util.ArrayList;
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
import pierre.zachary.view.Camera;
import pierre.zachary.view.GameObject;
import pierre.zachary.view.component.LandscapeTransform;
import pierre.zachary.view.component.Transform;
import pierre.zachary.view.component.renderer.collider.SpriteCollider;
import pierre.zachary.view.component.TransformAnchorPoint;
import pierre.zachary.view.component.renderer.SpriteRenderer;
import pierre.zachary.view.component.renderer.TextRenderer;
import pierre.zachary.view.component.renderer.TextSize;
import pierre.zachary.view.scene.SceneDispatcher;
import pierre.zachary.view.scene.SceneName;

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
                defaultLandscapeTransform(caseGO.transform);
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
        TextRenderer niveauTexteRenderer = new TextRenderer("Niveau "+level, Color.valueOf(Color.WHITE), TextSize.Title, this.gameObject.scene.getFont(R.font.luckiestguy));
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

        if(actionsList.size()>0){
            if(animEnded()){
                Function<String, String> action = actionsList.get(0);
                actionsList.remove(0);
                action.apply("");
            }
        }

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

    private List<Animator> animators = new ArrayList<>();

    public Boolean animEnded(){
        for(Animator a : animators){
            if(!a.Ended()){
                return false;
            }
        }
        return true;
    }


    public void defaultLandscapeTransform(Transform t){
        t.landscapeTransform.mulPosX *= Transform.landScapeModeScreenRatio()*.93f; // petite marge de 7% car j'ai une bordure sur mon émulateur
        t.landscapeTransform.mulPosY *= Transform.landScapeModeScreenRatio()*.93f;
        t.landscapeTransform.addPosX += (Transform.landScapeModeScreenRatio()*.93f)/1.5f;
        t.landscapeTransform.addPosY += (Transform.landScapeModeScreenRatio()*.93f)/1.5f;
        t.landscapeTransform.mulScaleX *= Transform.landScapeModeScreenRatio()*.93f;
        t.landscapeTransform.mulScaleY *=  Transform.landScapeModeScreenRatio()*.93f;
    }

    public void selectPionsGo(GameObject pionsGOToSelect){
        Pions p = getPions(pionsGOToSelect);
        if(p!=null){
            selectedPion = p;
            if(case_selector!=null){
                case_selector.scene.remove(case_selector);
            }
            case_selector = new GameObject(pionsGOToSelect.scene, "Case selector");
            case_selector.addComponent(new SpriteRenderer(R.drawable.case_select));
            case_selector.addComponent(new SpriteCollider());
            defaultLandscapeTransform(case_selector.transform);
            case_selector.transform.positionX = pionsGOToSelect.transform.positionX;
            case_selector.transform.positionY = pionsGOToSelect.transform.positionY;
        }
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
                    if(nextPionsHashMap.containsKey(p)){
                        GameObject pionsGO = nextPionsHashMap.get(p);
                        pionsGO.name = "Pions "+i+":"+j;
                        defaultLandscapeTransform(pionsGO.transform);
                        Animator anim = new Animator();
                        animators.add(anim);
                        pionsGO.addComponent(anim);
                        anim.addAnim(new TransformAnimation(pionsGO, start+i+0.5f, start+j+0.5f));

                        pionsGO.addComponent(new SpriteCollider());
                        pionsGO.addComponent(new OnClickCallBackBehaviour(new Function<GameObject, String>() {
                            @Override
                            public String apply(GameObject gameObject) {
                                selectPionsGo(gameObject);
                                return null;
                            }}));
                        pionsGameObjectHashMap.put(p, pionsGO);
                        System.out.println(pionsGO.scene);
                        nextPionsHashMap.remove(p);
                    }
                    else if(!pionsGameObjectHashMap.containsKey(p)){ // déplacement d'un nouveau pion
                        GameObject pionsGO = new GameObject(this.gameObject.scene);
                        pionsGO.name = "Pions "+i+":"+j;
                        pionsGO.transform.positionX = start+i+0.5f;
                        pionsGO.transform.positionY = start+j+0.5f;
                        defaultLandscapeTransform(pionsGO.transform);
                        pionsGO.addComponent(new SpriteRenderer(this.getImageRessource(p)));
                        pionsGO.scene.add(pionsGO);
                        pionsGO.addComponent(new SpriteCollider());
                        pionsGO.addComponent(new OnClickCallBackBehaviour(new Function<GameObject, String>() {
                            @Override
                            public String apply(GameObject gameObject) {
                                selectPionsGo(gameObject);
                                return null;
                            }}));
                        pionsGameObjectHashMap.put(p, pionsGO);
                    }
                    else{
                        GameObject pionsGO = pionsGameObjectHashMap.get(p);

                        assert pionsGO != null;

                        if(!pionsGO.name.equals("Pions " + i + ":" + j)){
                            System.out.println("Moving : "+pionsGO+" to "+i+", "+j);
                            pionsGO.name = "Pions "+i+":"+j;

                            Animator anim =(Animator) pionsGO.getComponent(Animator.class);
                            if(anim == null){
                                anim = new Animator();
                                animators.add(anim);
                                pionsGO.addComponent(anim);
                            }
                            anim.addAnim(new TransformAnimation(pionsGO, 100, start+i+0.5f, start+j+0.5f));
                            // TODO : attendre que ça ai fini de bouger avant de : regarder si ils sont alignés / spawn les next
                        }
                        pionsGO.scene.add(pionsGO);

                    }
                }
            }
        }
        if(selectedPion != null && !grid.containsPions(selectedPion)){
            GameObject pionsGo = pionsGameObjectHashMap.get(selectedPion);
            pionsGo.scene.remove(pionsGo); // TODO null ?
            selectedPion = null;
            case_selector.scene.remove(case_selector);
        }
    }


    @Override
    public void drawNext(List<Pions> pionsList) {

        if(grid!=null){
            for(int k = 0; k<pionsList.size(); k++){
                Pions p = pionsList.get(k);

                if(!nextPionsHashMap.containsKey(p)){
                    GameObject nextPionGO = new GameObject(this.gameObject.scene, "NextPionGO : "+k);
                    nextPionGO.transform.positionX = -1+k; // 0.5 car le transform est au centre du gameobject
                    nextPionGO.transform.positionY = ((int) -(grid.getGridSize()/2))-1.2f;
                    nextPionGO.addComponent(new SpriteRenderer(this.getImageRessource(p)));
                    nextPionsHashMap.put(p, nextPionGO);
                }


            }
        }

    }

    @Override
    public void gameOver(Grid g) {
        System.out.println("GAME OVER");
        SharedPreferences pref = this.gameObject.scene.getPrefs();
        int best = pref.getInt("BestScore", 0);

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("Score", score);
        if(score>best){
            editor.putInt("BestScore", score);
        }
        editor.apply();
        SceneDispatcher.getInstance().loadScene(SceneName.End);
    }

    private List<Function<String, String>> actionsList = new ArrayList<>();

    @Override
    public void addCallbackAction(Function<String, String> callback) {
        actionsList.add(callback);
    }

    @Override
    public void notifyScoreChanged(int addedScore) {
        score += addedScore;
        scoreTextRenderer.setText("SCORE : "+score);
    }
}
