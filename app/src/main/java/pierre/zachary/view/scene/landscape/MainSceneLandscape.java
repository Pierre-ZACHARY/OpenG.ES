package pierre.zachary.view.scene.landscape;

import android.content.Context;

import pierre.zachary.view.Camera;
import pierre.zachary.view.GameObject;
import pierre.zachary.view.component.scripts.GameManager;
import pierre.zachary.view.scene.Scene;
import pierre.zachary.view.scene.landscape.landscapescripts.GameManagerLandscape;

public class MainSceneLandscape extends Scene {
    private final int level;
    GameObject gameManager;

    public MainSceneLandscape(Context context, int level) {
        super(context);
        this.level = level;
        Start();
        gameManager.Start();
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
        gameManager.addComponent(new GameManagerLandscape(level));
    }
}