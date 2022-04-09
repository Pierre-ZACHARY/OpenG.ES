package pierre.zachary.view;

import android.content.Context;
import android.graphics.Color;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.MyGLSurfaceView;
import pierre.zachary.R;
import pierre.zachary.modele.Facade;

public class MainScene extends Scene {
    //Square square1, square2;
    Camera mainCam;
    GameObject gameManager, imageGO;

    Facade gameFacade;

    public MainScene(Context context) {
        super(context);
        mainCam = new Camera( 9, true);


        gameManager = new GameObject(this, "Game Manager");
        gameManager.addComponent(new GameManager(gameManager));

    }
}