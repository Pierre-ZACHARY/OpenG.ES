package pierre.zachary.view.scene;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import org.w3c.dom.Text;

import java.util.function.Function;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.R;
import pierre.zachary.view.Camera;
import pierre.zachary.view.GameObject;
import pierre.zachary.view.component.renderer.SpriteRenderer;
import pierre.zachary.view.component.renderer.TextRenderer;
import pierre.zachary.view.component.renderer.TextSize;
import pierre.zachary.view.component.renderer.collider.SpriteCollider;
import pierre.zachary.view.component.renderer.collider.TextRendererBackgroundCollider;
import pierre.zachary.view.component.scripts.OnClickCallBackBehaviour;

public class EndScene extends Scene {

    public EndScene(Context context) {
        super(context);
    }

    public SceneName lastLevel = SceneName.Level1;

    @Override
    public void Start() {
        mainCamera = new Camera( 1, true);

        GameObject titre = new GameObject(this, "Game Over Text");
        titre.transform.positionY = .2f ;
        titre.addComponent(new TextRenderer("Perdu !", Color.valueOf(Color.rgb(189, 30, 72)), TextSize.Title, this.getFont(R.font.luckiestguy)));


        GameObject score = new GameObject(this, "Game Over Text");
        score.transform.positionY = .1f ;

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
        exit.transform.positionX = -0.3f ; // 0.5 car le transform est au centre du gameobject
        exit.transform.positionY = -0.15f ;
        exit.transform.scaleX = .2f ;
        exit.transform.scaleY = .2f ;
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
        tryagain.transform.positionY = -0.15f ;
        tryagain.transform.positionX = 0.15f ;
        tryagain.transform.scaleX = .6f ;
        tryagain.transform.scaleY = .6f ;
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
}
