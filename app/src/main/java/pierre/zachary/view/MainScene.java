package pierre.zachary.view;

import android.content.Context;
import android.graphics.Color;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.MyGLSurfaceView;
import pierre.zachary.R;
import pierre.zachary.modele.Facade;

public class MainScene extends Scene {
    GameObject gameManager;

    public MainScene(Context context, int level) {
        super(context);
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