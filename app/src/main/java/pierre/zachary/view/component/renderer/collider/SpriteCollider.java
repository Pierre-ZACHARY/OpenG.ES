package pierre.zachary.view.component.renderer.collider;

import android.view.MotionEvent;

import pierre.zachary.view.component.Component;
import pierre.zachary.view.component.renderer.SpriteRenderer;
import pierre.zachary.view.component.scripts.MonoBehaviour;

public class SpriteCollider extends Component {

    SpriteRenderer renderer;

    @Override
    public void Start(){
        for(Component c : this.gameObject.componentList){
            if(c instanceof SpriteRenderer){
                renderer =(SpriteRenderer) c;
                break;
            }
        }

        if(renderer == null){
            System.err.println("Gameobject with Sprite Collider must have a Sprite Renderer component ("+this.gameObject.name+")");
        }
    }

    @Override
    public void OnTouchEvent(MotionEvent e){

        float x = e.getX();
        float y = e.getY();

        if(renderer.isInside(x, y)){
            for(Component c : this.gameObject.componentList){
                if(c instanceof MonoBehaviour){
                    MonoBehaviour script = (MonoBehaviour) c;
                    if(e.getAction() == MotionEvent.ACTION_DOWN)
                        script.OnTouchDown(x, y);
                }
            }
        }
    }

}
