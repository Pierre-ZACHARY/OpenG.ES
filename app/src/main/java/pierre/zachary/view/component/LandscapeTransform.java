package pierre.zachary.view.component;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.view.component.scripts.MonoBehaviour;

public class LandscapeTransform {

    public float mulPosX, mulPosY, mulPosZ;
    public float mulRotationX, mulRotationY, mulRotationZ;
    public float mulScaleX, mulScaleY, mulScaleZ ;

    public float addPosX, addPosY, addPosZ;
    public float addRotationX, addRotationY, addRotationZ;
    public float addScaleX, addScaleY, addScaleZ;

    public LandscapeTransform(){
        mulPosX = 1f;
        mulPosY = 1f;
        mulPosZ = 1f;
        mulRotationX = 1f;
        mulRotationY = 1f;
        mulRotationZ = 1f;
        mulScaleX = 1f;
        mulScaleY = 1f;
        mulScaleZ = 1f;

        addPosX = 0f;
        addPosY = 0f;
        addPosZ = 0f;
        addRotationX = 0f;
        addRotationY = 0f;
        addRotationZ = 0f;
        addScaleX = 0f;
        addScaleY = 0f;
        addScaleZ = 0f;
    }

}
