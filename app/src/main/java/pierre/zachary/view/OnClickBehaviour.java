package pierre.zachary.view;

public class OnClickBehaviour extends MonoBehaviour{
    public OnClickBehaviour(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void OnTouchDown(float x, float y) {
        super.OnTouchDown(x, y);
        System.out.println(gameObject.name + " touché");
    }
}
