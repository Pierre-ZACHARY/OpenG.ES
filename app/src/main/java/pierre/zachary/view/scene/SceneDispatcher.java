package pierre.zachary.view.scene;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class SceneDispatcher  implements GLSurfaceView.Renderer{

    private static SceneDispatcher instance;

    public static SceneDispatcher getInstance(){
        return  instance;
    }


    private Scene currentScene;
    private Scene level1Scene;
    private Scene level2Scene;
    private Scene menuScene;
    private EndScene endScene;
    private SceneName toBeLoaded;

    public SceneDispatcher(Context context){
        instance = this;
        menuScene = new MenuScene(context);
        level1Scene = new MainScene(context, 1);
        level2Scene = new MainScene(context, 2);
        endScene = new EndScene(context);

        setCurrentScene(SceneName.Level1);
    }

    private void setCurrentScene(SceneName name){
        switch (name){
            case Level1:
                endScene.lastLevel = name;
                currentScene = level1Scene;
                break;
            case Level2:
                endScene.lastLevel = name;
                currentScene = level2Scene;
                break;
            case End:
                currentScene = endScene;
                break;
            default:
                currentScene = menuScene;
        }
        currentScene.resetScene();
        currentScene.mainCamera.setMainCamera();
    }

    public void loadScene(SceneName name){
        toBeLoaded = name;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        level1Scene.onSurfaceCreated(gl10, eglConfig);
        level2Scene.onSurfaceCreated(gl10, eglConfig);
        menuScene.onSurfaceCreated(gl10, eglConfig);
    }

    private int width;
    private int height;
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;
        currentScene.onSurfaceChanged(gl10, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if(toBeLoaded != null){
            setCurrentScene(toBeLoaded);
            onSurfaceChanged(gl10, width, height);
            toBeLoaded = null;
        }
        currentScene.onDrawFrame(gl10);
    }

    public void onTouchEvent(MotionEvent e) {
        currentScene.onTouchEvent(e);
    }
}
