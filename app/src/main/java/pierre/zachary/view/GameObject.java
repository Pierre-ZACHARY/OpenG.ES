package pierre.zachary.view;

import android.view.MotionEvent;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class GameObject {

    Scene scene;
    String name;
    Transform transform;
    ArrayList<Component> componentList;

    private static int counter = 0;

    public GameObject(Scene scene){
        this.scene = scene;
        componentList = new ArrayList<>();
        transform = new Transform(this);
        name = "GameObject : "+counter;
        counter+=1;
    }

    public GameObject(Scene scene, String name){
        this.scene = scene;
        componentList = new ArrayList<>();
        transform = new Transform(this);
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "GameObject{" +
                "name='" + name + '\'' +
                '}';
    }

    public void addComponent(Component e){
        componentList.add(e);
    }

    public void Start(){
        for(Component e : componentList){
            e.Start();
        }
    }

    public void Load(GL10 gl){
        for(Component e : componentList){
            e.Load(gl);
        }
    }

    public void Update(GL10 gl){ // TODO rename en draw
        gl.glPushMatrix();

        transform.Update(gl);

        for(Component e : componentList){
            e.Update(gl);
        }

        gl.glPopMatrix();
    }

    public void OnTouchEvent(MotionEvent e){
        for(Component c : componentList){
            c.OnTouchEvent(e);
        }
    }
}
