package pierre.zachary.view.component.scripts;

import java.util.function.Function;

import pierre.zachary.view.GameObject;

public class OnClickCallBackBehaviour extends MonoBehaviour{
    Function<GameObject, String> callback;


    public OnClickCallBackBehaviour(Function<GameObject, String> function) {
        this.callback = function;
    }

    @Override
    public void OnTouchDown(float x, float y) {
        super.OnTouchDown(x, y);
        System.out.println(gameObject.name + " touché");
        callback.apply(this.gameObject);
    }
}

