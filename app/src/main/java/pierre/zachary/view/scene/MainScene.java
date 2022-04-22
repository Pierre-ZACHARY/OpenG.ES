package pierre.zachary.view.scene;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.view.Camera;
import pierre.zachary.view.component.scripts.GameManager;
import pierre.zachary.view.GameObject;

public class MainScene extends Scene {
    private final int level;
    GameObject gameManager;

    public MainScene(Context context, int level) {
        super(context);
        this.level = level;
    }

    @Override
    public void Start() {
        switch (level){
            default:
                mainCamera = new Camera( 9, true);
                break;
            case 2:
                mainCamera = new Camera( 7, true);
                break;
        }
        gameManager = new GameObject(this, "Game Manager");
        gameManager.addComponent(new GameManager(level));
    }
}