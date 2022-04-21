package pierre.zachary.view;

import android.content.Context;
import android.graphics.Color;

import java.util.function.Function;

import pierre.zachary.MyGLSurfaceView;
import pierre.zachary.R;

public class MenuScene extends Scene{

    public MenuScene(Context context) {
        super(context);
        mainCamera = new Camera( 1, true);

        GameObject affichageScore = new GameObject(this, "WelcomeText");
        affichageScore.transform.positionY = .1f ;
        affichageScore.addComponent(new TextRenderer("Cinq ou plus", Color.valueOf(Color.WHITE)));

        // TODO pas top d'utiliser deux gameobject différent, l'affichage risque de change selon les dimension de l'écran, le mieux serait de mettre tout le texte dans le même, ça nécessite de pas mal modifier le TextRenderer
        GameObject auteur = new GameObject(this, "Auteur");
        auteur.addComponent(new TextRenderer("Pierre ZACHARY", Color.valueOf(Color.GRAY), TextSize.Hint));

        GameObject niveau1 = new GameObject(this, "niveau1");
        niveau1.transform.positionY = -0.2f ;
        niveau1.transform.scaleX = .7f ;
        niveau1.transform.scaleY = .7f ;
        TextRenderer niveau1TR = new TextRenderer("Niveau 1", Color.valueOf(Color.WHITE), TextSize.SubTitle);
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
        TextRenderer niveau2TR = new TextRenderer("Niveau 2", Color.valueOf(Color.WHITE), TextSize.SubTitle);
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
