package pierre.zachary.view.component.scripts;

public class OnClickBehaviour extends MonoBehaviour{

    @Override
    public void OnTouchDown(float x, float y) {
        super.OnTouchDown(x, y);
        System.out.println(gameObject.name + " touché");
    }
}
