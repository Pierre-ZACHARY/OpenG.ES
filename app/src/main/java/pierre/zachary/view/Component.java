package pierre.zachary.view;

import android.view.MotionEvent;

import javax.microedition.khronos.opengles.GL10;

public abstract class Component {

    GameObject gameObject;

    public Component(GameObject gameObject){
        this.gameObject = gameObject;
    }

    public void Update(GL10 gl) {

    }

    public  void Load(GL10 gl) {

    }

    public void Start() {

    }

    public void OnTouchEvent(MotionEvent e){

    }
}
