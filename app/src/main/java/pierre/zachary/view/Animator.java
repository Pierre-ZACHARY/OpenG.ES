package pierre.zachary.view;

import java.util.ArrayList;
import java.util.List;

public class Animator extends MonoBehaviour{

    private List<TransformAnimation> animations = new ArrayList<>();

    public void addAnim(TransformAnimation animation){
        animations.add(animation);
    }

    @Override
    public void Update() {

        if(animations.size()>0){
            TransformAnimation current = animations.get(0);

            if(current != null){
                current.Moove();

                if(current.ended()){
                    animations.remove(0);
                }
            }
        }



    }
}
