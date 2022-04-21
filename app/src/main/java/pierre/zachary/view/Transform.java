package pierre.zachary.view;

import javax.microedition.khronos.opengles.GL10;


enum TransformAnchorPoint{
    Center, BottomLeft, BottomRight, TopLeft, TopRight
}

public class Transform extends Component{

    public static float sceneWidth;
    public static float sceneHeight;
    public static float screenRatio;
    public static int screenWidth;
    public static int screenHeight;


    public Transform() {
        this.positionX = 0f;
        this.positionY = 0f;
        this.positionZ = 0f;
        this.rotationX = 0f;
        this.rotationY = 0f;
        this.rotationZ = 0f;
        this.scaleX = 1f;
        this.scaleY = 1f;
        this.scaleZ = 1f;

        this.anchorPoint = TransformAnchorPoint.Center;
    }

    public float positionX, positionY, positionZ;
    public float rotationX, rotationY, rotationZ;
    public float scaleX, scaleY, scaleZ;

    private float getGameUnitSize(){
        if(Camera.main != null){
            return Camera.main.getSize();
        }
        else{
            return 1f;
        }
    }

    public static float gameUnitX(){
        return sceneWidth /Camera.main.getSize();
    }

    public static float gameUnitY(){
        return (sceneHeight /Camera.main.getSize())/screenRatio;
    }

    public TransformAnchorPoint anchorPoint;
    public float getAnchorPointX(){
        float gameUnit = gameUnitX();
        switch(anchorPoint){
            case Center:
                return (-gameUnit*scaleX)/2f;
            case BottomLeft:
            case TopLeft:
                return 0f;
            case BottomRight:
            case TopRight:
                return (-gameUnit*scaleX);
        }
        return 0f;
    }

    public float getAnchorPointY(){
        float gameUnit = gameUnitY();
        switch(anchorPoint){
            case Center:
                return (-gameUnit*scaleY)/2f;
            case BottomLeft:
            case BottomRight:
                return 0f;
            case TopLeft:
            case TopRight:
                return (-gameUnit*scaleY);
        }
        return 0f;
    }

    public float ScreenPositionX(){
        return (sceneWidth /2f)/Camera.main.getSize() // position Source ( milieu de l'écran )
                + (positionX*Transform.gameUnitX())/Camera.main.getSize()  // position de l'objet
                + (getAnchorPointX())/Camera.main.getSize() // point d'ancrage
                + ((scaleX*Transform.gameUnitX())/Camera.main.getSize())/2f; // scale (on prend le milieu de l'objet)
    }

    public float ScreenPositionY(){
        return ((sceneHeight /2f)/Camera.main.getSize())/screenRatio
                - ((positionY*Transform.gameUnitY())/Camera.main.getSize())/Transform.screenRatio
                - ((getAnchorPointY())/Camera.main.getSize())/Transform.screenRatio
                - (((scaleY*Transform.gameUnitY())/Camera.main.getSize())/Transform.screenRatio)/2f;
    }

    @Override
    public void Draw(GL10 gl){

        //positionX += 0.001f;

        gl.glRotatef (rotationZ, 0f, 0f, 1f); // ROTATION Z
        gl.glRotatef (rotationY, 0f, 1f, 0f); // ROTATION Y
        gl.glRotatef (rotationX, 1f, 0f, 0f); // ROTATION X


        float xStartingPoint = sceneWidth /2f; // le milieu de l'écran
        float yStartingPoint = sceneHeight /2f; // le milieu de l'écran

        gl.glTranslatef (xStartingPoint+getAnchorPointX()+positionX*gameUnitX(), yStartingPoint+getAnchorPointY()+positionY*gameUnitY(), 0f); // TRANSLATION

        gl.glScalef (gameUnitX()*scaleX, gameUnitY()*scaleY, 0f); // SCALE
    }

}
