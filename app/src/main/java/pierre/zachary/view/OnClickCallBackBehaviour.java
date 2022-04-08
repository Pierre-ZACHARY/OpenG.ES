package pierre.zachary.view;

import java.util.function.Function;

import pierre.zachary.R;

public class OnClickCallBackBehaviour extends MonoBehaviour{
    Function<GameObject, String> callback;


    public OnClickCallBackBehaviour(GameObject gameObject, Function<GameObject, String> function) {
        super(gameObject);
        this.callback = function;
    }

    @Override
    public void OnTouchDown(float x, float y) {
        super.OnTouchDown(x, y);
        System.out.println(gameObject.name + " touché");
        callback.apply(this.gameObject);
    }
}

