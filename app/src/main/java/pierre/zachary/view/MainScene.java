package pierre.zachary.view;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.MyGLSurfaceView;
import pierre.zachary.R;
import pierre.zachary.modele.Facade;

public class MainScene extends Scene {
    //Square square1, square2;
    Camera mainCam;
    GameObject gameManager, imageGO;

    Facade gameFacade;

    public MainScene(Context context, MyGLSurfaceView myGLSurfaceView) {
        super(context, myGLSurfaceView);
        mainCam = new Camera( 9, true);
        imageGO = new GameObject(this, "Image Bleu");
        imageGO.transform.positionX = 0;
        imageGO.transform.scaleX = 1;
        imageGO.transform.positionY = 0;
        imageGO.addComponent(new SpriteRenderer(imageGO, R.drawable.blue_rectangle));
        imageGO.addComponent(new SpriteCollider(imageGO));
        imageGO.addComponent(new OnClickBehaviour(imageGO));

        gameManager = new GameObject(this, "Game Manager");
        gameManager.addComponent(new GameManager(gameManager));

    }
}