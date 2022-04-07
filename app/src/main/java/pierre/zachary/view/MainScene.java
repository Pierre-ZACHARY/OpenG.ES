package pierre.zachary.view;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.R;

public class MainScene extends Scene {
    //Square square1, square2;
    Camera mainCam;
    GameObject imageGO2, imageGO;

    public MainScene(Context context) {
        super(context);
        mainCam = new Camera( 10, true);
        imageGO = new GameObject(this);
        imageGO.transform.positionX = 0; // 0.5 car le transform est au centre du gameobject
        imageGO.transform.scaleX = 1;
        imageGO.transform.positionY = 0;
        imageGO.addComponent(new SpriteRenderer(imageGO, R.drawable.blue_rectangle));
        imageGO.addComponent(new SpriteCollider(imageGO));


        imageGO2 = new GameObject(this);
        imageGO2.transform.positionX = 1; // 0.5 car le transform est au centre du gameobject
        imageGO2.transform.scaleX = 1;
        imageGO2.transform.positionY = 0;
        imageGO2.addComponent(new SpriteRenderer(imageGO2, R.mipmap.red_icon));
        imageGO2.addComponent(new SpriteCollider(imageGO2));

    }
}