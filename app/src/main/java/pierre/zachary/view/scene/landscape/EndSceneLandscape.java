package pierre.zachary.view.scene.landscape;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import java.util.function.Function;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.R;
import pierre.zachary.modele.Facade;
import pierre.zachary.view.Camera;
import pierre.zachary.view.GameObject;
import pierre.zachary.view.component.Transform;
import pierre.zachary.view.component.renderer.SpriteRenderer;
import pierre.zachary.view.component.renderer.TextRenderer;
import pierre.zachary.view.component.renderer.TextSize;
import pierre.zachary.view.component.renderer.collider.SpriteCollider;
import pierre.zachary.view.component.renderer.collider.TextRendererBackgroundCollider;
import pierre.zachary.view.component.scripts.OnClickCallBackBehaviour;
import pierre.zachary.view.scene.Scene;
import pierre.zachary.view.scene.SceneDispatcher;
import pierre.zachary.view.scene.SceneName;

public class EndSceneLandscape extends Scene {

    public EndSceneLandscape(Context context) {
        super(context);
    }

    public SceneName lastLevel = SceneName.Level1;

    @Override
    public void load(GL10 gl) {
        super.load(gl);
        this.clearGameObjects();

        GameObject titre = new GameObject(this, "Game Over Text");
        titre.transform.positionY = .1f ;
        titre.transform.scaleX = Transform.screenRatio ;
        titre.transform.scaleY = Transform.screenRatio ;
        titre.addComponent(new TextRenderer("Perdu !", Color.valueOf(Color.rgb(189, 30, 72)), TextSize.Title, this.getFont(R.font.luckiestguy)));


        GameObject score = new GameObject(this, "Game Over Text");
        score.transform.positionY = .05f ;
        score.transform.scaleX = Transform.screenRatio ;
        score.transform.scaleY = Transform.screenRatio ;

        SharedPreferences pref = this.getPrefs();
        TextRenderer scoretr = new TextRenderer(
                "Score : "+pref.getInt("Score", 0),
                Color.valueOf(Color.WHITE),
                TextSize.SubTitle,
                this.getFont(R.font.luckiestguy));
        pref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if(s.equals("Score")){
                    int score = sharedPreferences.getInt(s, 0);
                    scoretr.setText("Score : "+score);
                }
            }
        });

        score.addComponent(scoretr);

        GameObject exit = new GameObject(this, "exit");
        exit.transform.positionX = -0.3f* Transform.screenRatio ; // 0.5 car le transform est au centre du gameobject
        exit.transform.positionY = -0.15f* Transform.screenRatio ;
        exit.transform.scaleX = .2f * Transform.screenRatio;
        exit.transform.scaleY = .2f * Transform.screenRatio ;
        exit.addComponent(new SpriteRenderer(R.drawable.exit_button));
        exit.addComponent(new SpriteCollider());
        exit.addComponent(new OnClickCallBackBehaviour(new Function<GameObject, String>() {
            @Override
            public String apply(GameObject gameObject) {
                SceneDispatcher.getInstance().loadScene(SceneName.Menu);
                return null;
            }
        }));

        GameObject tryagain = new GameObject(this, "tryagain");
        tryagain.transform.positionY = -0.15f* Transform.screenRatio ;
        tryagain.transform.positionX = 0.15f* Transform.screenRatio ;
        tryagain.transform.scaleX = .6f* Transform.screenRatio ;
        tryagain.transform.scaleY = .6f* Transform.screenRatio ;
        tryagain.addComponent(new TextRenderer(
                "Rejouer",
                Color.valueOf(Color.WHITE),
                TextSize.SubTitle,
                this.getFont(R.font.luckiestguy))
                .addBackground(getDrawable(R.drawable.green_button)));
        tryagain.addComponent(new TextRendererBackgroundCollider());
        tryagain.addComponent(new OnClickCallBackBehaviour(new Function<GameObject, String>() {
            @Override
            public String apply(GameObject gameObject) {
                SceneDispatcher.getInstance().loadScene(lastLevel);
                return null;
            }
        }));
    }

    @Override
    public void Start() {
        mainCamera = new Camera( 1, true);
    }
}
