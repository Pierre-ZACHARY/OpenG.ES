package pierre.zachary.view.scene;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.view.scene.landscape.EndSceneLandscape;
import pierre.zachary.view.scene.landscape.MainSceneLandscape;
import pierre.zachary.view.scene.landscape.MenuSceneLandscape;


public class SceneDispatcher  implements GLSurfaceView.Renderer{

    private static SceneDispatcher instance;

    public static SceneDispatcher getInstance(){
        return  instance;
    }


    private SceneName currentScene;
    private Scene level1Scene;
    private Scene level1SceneLandscape;

    private Scene level2Scene;
    private Scene level2SceneLandscape;

    private Scene menuScene;
    private Scene menuSceneLanscape;

    private EndScene endScene;
    private EndSceneLandscape endSceneLandscape;

    private SceneName toBeLoaded;

    public SceneDispatcher(Context context){
        instance = this;
        menuScene = new MenuScene(context);
        menuSceneLanscape = new MenuSceneLandscape(context);
        level1Scene = new MainScene(context, 1);
        level1SceneLandscape = new MainSceneLandscape(context, 1);
        level2Scene = new MainScene(context, 2);
        level2SceneLandscape = new MainSceneLandscape(context, 2);
        endScene = new EndScene(context);
        endSceneLandscape = new EndSceneLandscape(context);

        setCurrentScene(SceneName.Menu);
    }

    private boolean landscapeMode(){
        return width>=height;
    }

    private Scene getScene(){
        switch (currentScene){
            case Level1:
                if(landscapeMode()) return level1SceneLandscape;
                else return level1Scene;
            case Level2:
                if(landscapeMode()) return level2SceneLandscape;
                else return level2Scene;
            case End:
                if(landscapeMode()) return endSceneLandscape;
                else return endScene;
            default:
                if(landscapeMode()) return menuSceneLanscape;
                else return menuScene;
        }
    }

    private void setCurrentScene(SceneName name){
        if(name == SceneName.Level1 || name == SceneName.Level2){
            endScene.lastLevel = name;
            endSceneLandscape.lastLevel = name;
        }
        currentScene = name;
        getScene().resetScene();
        getScene().mainCamera.setMainCamera();
    }

    public void loadScene(SceneName name){
        toBeLoaded = name;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        level1Scene.onSurfaceCreated(gl10, eglConfig);
        level1SceneLandscape.onSurfaceCreated(gl10, eglConfig);
        level2Scene.onSurfaceCreated(gl10, eglConfig);
        level2SceneLandscape.onSurfaceCreated(gl10, eglConfig);
        menuScene.onSurfaceCreated(gl10, eglConfig);
        menuSceneLanscape.onSurfaceCreated(gl10, eglConfig);
        endScene.onSurfaceCreated(gl10, eglConfig);
        endSceneLandscape.onSurfaceCreated(gl10, eglConfig);
    }

    private int width;
    private int height;
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;
        getScene().onSurfaceChanged(gl10, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if(toBeLoaded != null){
            setCurrentScene(toBeLoaded);
            onSurfaceChanged(gl10, width, height);
            toBeLoaded = null;
        }
        getScene().onDrawFrame(gl10);
    }

    public void onTouchEvent(MotionEvent e) {
        getScene().onTouchEvent(e);
    }
}
