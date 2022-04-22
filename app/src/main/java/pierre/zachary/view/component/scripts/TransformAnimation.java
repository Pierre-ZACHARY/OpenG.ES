package pierre.zachary.view.component.scripts;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.min;

import java.time.Clock;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import pierre.zachary.view.GameObject;

public class TransformAnimation{

    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private long animStart;
    private long animEnd;

    private GameObject go;

    public TransformAnimation(GameObject go, long millis, float Xtarget, float Ytarget){
        animEnd = millis; // TODO la même chose mais calculer les millis selon la distance à parcourir
        endX = Xtarget;
        endY = Ytarget;
        this.go = go;
    }

    public TransformAnimation(GameObject go, float Xtarget, float Ytarget){
        animEnd = 0;
        endX = Xtarget;
        endY = Ytarget;
        this.go = go;
    }

    private Boolean init = false;

    public void Init() {
        animStart = Calendar.getInstance().getTimeInMillis();
        startX = go.transform.positionX;
        startY = go.transform.positionY;
        if(animEnd == 0){
            float diffX = endX - startX;
            float diffY = endY - startY;
            animEnd = (long) (50 * ( abs(diffX) + abs(diffY))); // 100 ms par unité de distance
        }
        init = true;
    }

    /**
     * UNUSED
     * @param progress : progression de l'animation entre 0 et 1
     * @return double : valeur de la courbe ( 1 -> doit être à sa position finale / 0 -> doit être à sa position initial)
     */
    public double easeInSine(float progress){
        return 1 - cos((progress * PI) / 2);
    }

    public Boolean ended(){
        float progress =(float) min(1, (Calendar.getInstance().getTimeInMillis() - animStart)/(animEnd*1f));
        return progress == 1f;
    }


    public void Moove() {
        if(!init){
            Init();
        }
        float progress =(float) min(1, (Calendar.getInstance().getTimeInMillis() - animStart)/(animEnd*1f));
        float diffX = endX - startX;
        float diffY = endY - startY;
        go.transform.positionX = (float) (startX + ((float) easeInSine(progress))*diffX);
        go.transform.positionY = (float) (startY + ((float) easeInSine(progress))*diffY);
    }
}
