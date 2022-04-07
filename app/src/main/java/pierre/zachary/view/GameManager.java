package pierre.zachary.view;

import java.util.List;

import pierre.zachary.R;
import pierre.zachary.modele.Drawer;
import pierre.zachary.modele.Facade;
import pierre.zachary.modele.Grid;
import pierre.zachary.modele.Pions;
import pierre.zachary.modele.Score;

public class GameManager extends MonoBehaviour implements Score, Drawer {

    Facade jeu;
    Grid grid;

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
                caseGO.addComponent(new OnClickBehaviour(caseGO));
            }
        }
    }


    @Override
    public void drawGrid(Grid g) {

    }

    @Override
    public void drawNext(List<Pions> pionsList) {

    }

    @Override
    public void notifyScoreChanged(int addedScore) {

    }
}
