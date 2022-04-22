package pierre.zachary.view.component;

import android.view.MotionEvent;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.view.GameObject;

public abstract class Component {

    public GameObject gameObject = null;


    public void Draw(GL10 gl) {

    }

    public  void Load(GL10 gl) {

    }

    public void Start() {

    }

    public void OnTouchEvent(MotionEvent e){

    }
}
