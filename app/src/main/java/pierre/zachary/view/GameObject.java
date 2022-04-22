package pierre.zachary.view;

import android.view.MotionEvent;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

import javax.microedition.khronos.opengles.GL10;

public class GameObject {

    private static int global_id = 0;
    private int id;
    Scene scene;
    String name;
    Transform transform;
    ArrayList<Component> componentList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private static int counter = 0;

    public GameObject(Scene scene){
        id = global_id;
        global_id++;
        this.scene = scene;
        componentList = new ArrayList<>();
        transform = new Transform();
        name = "GameObject : "+counter;
        counter+=1;
        scene.add(this);
    }

    public GameObject(Scene scene, String name){
        this(scene);
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
        e.gameObject = this;
        componentList.add(e);
        e.Start();
    }

    public void removeComponent(Component e){
        componentList.remove(e);
    }

    public <T extends Component> Component getComponent(Class<T> whatclass){
        for(Component c : componentList){
            if(whatclass.isInstance(c)){
                return c;
            }
        }
        return null;
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

    public void Draw(GL10 gl){
        Update();

        gl.glPushMatrix();

        transform.Draw(gl);

        for(Component e : componentList){
            e.Draw(gl);
        }

        gl.glPopMatrix();
    }

    public void Update(){
        // call avant un draw

        for(Component e : componentList){
            if(e instanceof MonoBehaviour){
                ((MonoBehaviour) e).Update();
            }
        }
    }

    public void OnTouchEvent(MotionEvent e){
        for(Component c : componentList){
            c.OnTouchEvent(e);
        }
    }
}
