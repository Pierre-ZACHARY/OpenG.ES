package pierre.zachary.view;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.R;

public class MainScene extends Scene {
    Square square1, square2;


    public MainScene(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void init(GL10 gl) {
        square1 = new Square();
        square2 = new Square();
    }

    public void load(GL10 gl) {
        image(gl, R.drawable.bluerectangle);
    }

    public void draw(GL10 gl) {
        square1.draw(gl, images.get(R.drawable.bluerectangle), 0, 0, width, height, 0);
    }
}