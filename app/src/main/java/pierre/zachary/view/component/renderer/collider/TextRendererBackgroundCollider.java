package pierre.zachary.view.component.renderer.collider;

import android.view.MotionEvent;

import pierre.zachary.view.component.Component;
import pierre.zachary.view.component.renderer.TextRenderer;
import pierre.zachary.view.component.scripts.MonoBehaviour;

public class TextRendererBackgroundCollider extends Component {

    TextRenderer renderer;

    @Override
    public void Start() {

        for (Component c : this.gameObject.componentList) {
            if (c instanceof TextRenderer) {
                renderer = (TextRenderer) c;
                break;
            }
        }

        if (renderer == null) {
            System.err.println("Gameobject with Text Renderer Collider must have a Text Renderer component (" + this.gameObject.name + ")");
        }
    }

    @Override
    public void OnTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        if (renderer.isInside(x, y)) {
            for (Component c : this.gameObject.componentList) {
                if (c instanceof MonoBehaviour) {
                    MonoBehaviour script = (MonoBehaviour) c;
                    if (e.getAction() == MotionEvent.ACTION_DOWN)
                        script.OnTouchDown(x, y);
                }
            }
        }
    }
}
