package pierre.zachary.view.scene;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import java.util.function.Function;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.R;
import pierre.zachary.view.Camera;
import pierre.zachary.view.GameObject;
import pierre.zachary.view.component.scripts.OnClickCallBackBehaviour;
import pierre.zachary.view.component.renderer.TextRenderer;
import pierre.zachary.view.component.renderer.collider.TextRendererBackgroundCollider;
import pierre.zachary.view.component.renderer.TextSize;

public class MenuScene extends Scene {

    public MenuScene(Context context) {
        super(context);
    }

    @Override
    public void Start() {
        mainCamera = new Camera( 1, true);

        GameObject titre = new GameObject(this, "WelcomeText");
        titre.transform.positionY = .2f ;
        titre.addComponent(new TextRenderer("Cinq ou +", Color.valueOf(Color.WHITE), TextSize.Title, this.getFont(R.font.luckiestguy)));

        // TODO pas top d'utiliser deux gameobject différent, l'affichage risque de change selon les dimension de l'écran, le mieux serait de mettre tout le texte dans le même, ça nécessite de pas mal modifier le TextRenderer
        GameObject auteur = new GameObject(this, "Auteur");
        auteur.transform.positionY = .1f ;
        auteur.addComponent(new TextRenderer("Pierre ZACHARY", Color.valueOf(Color.GRAY), TextSize.Hint));


        GameObject meilleurscore = new GameObject(this, "Meilleur score");
        SharedPreferences pref = this.getPrefs();
        TextRenderer meilleurscoretr = new TextRenderer(
                "Meilleur Score : "+pref.getInt("BestScore", 0),
                Color.valueOf(Color.WHITE),
                TextSize.Hint,
                this.getFont(R.font.luckiestguy));
        pref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if(s.equals("BestScore")){
                    int score = sharedPreferences.getInt(s, 0);
                    meilleurscoretr.setText("Meilleur Score : "+score);
                }
            }
        });
        meilleurscore.addComponent(meilleurscoretr);


        GameObject niveau1 = new GameObject(this, "niveau1");
        niveau1.transform.positionY = -0.2f ;
        niveau1.transform.scaleX = .7f ;
        niveau1.transform.scaleY = .7f ;
        TextRenderer niveau1TR = new TextRenderer("Niveau 1", Color.valueOf(Color.WHITE), TextSize.SubTitle, this.getFont(R.font.luckiestguy));
        niveau1TR.addBackground(getDrawable(R.drawable.orange_button));
        niveau1.addComponent(niveau1TR);
        niveau1.addComponent(new TextRendererBackgroundCollider());
        niveau1.addComponent(new OnClickCallBackBehaviour(new Function<GameObject, String>() {
            @Override
            public String apply(GameObject gameObject) {
                SceneDispatcher.getInstance().loadScene(SceneName.Level1);
                return null;
            }
        }));

        GameObject niveau2 = new GameObject(this, "niveau2");
        niveau2.transform.positionY = -0.4f ;
        niveau2.transform.scaleX = .7f ;
        niveau2.transform.scaleY = .7f ;
        TextRenderer niveau2TR = new TextRenderer("Niveau 2", Color.valueOf(Color.WHITE), TextSize.SubTitle, this.getFont(R.font.luckiestguy));
        niveau2TR.addBackground(getDrawable(R.drawable.magenta_button));
        niveau2.addComponent(niveau2TR);
        niveau2.addComponent(new TextRendererBackgroundCollider());
        niveau2.addComponent(new OnClickCallBackBehaviour(new Function<GameObject, String>() {
            @Override
            public String apply(GameObject gameObject) {
                SceneDispatcher.getInstance().loadScene(SceneName.Level2);
                return null;
            }
        }));
    }
}
