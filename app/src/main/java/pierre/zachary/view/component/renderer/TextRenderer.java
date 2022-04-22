package pierre.zachary.view.component.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import javax.microedition.khronos.opengles.GL10;

import pierre.zachary.view.Camera;
import pierre.zachary.view.component.Component;
import pierre.zachary.view.component.Transform;

public class TextRenderer  extends Component {

    private final Canvas canvas;
    private final Bitmap bitmap;
    private final Paint textPaint;



    public void setText(String text) {
        hasBeenLoaded = false;
        this.text = text;
    }

    private String text;
    Vertex shape = new Vertex (new float[]{
            1f,1f,0f,
            0f,1f,0f,
            1f,0f,0f,
            0f,0f,0f,
    });
    Vertex texture = new Vertex (new float[]{
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
    });
    private boolean hasBeenLoaded;
    private int textureId;
    private Drawable background;

    public Integer getSize(TextSize textSize){
        switch (textSize){
            default:
                return 64;
            case SubTitle:
                return 48;
            case Hint:
                return 32;
        }
    }

    public TextRenderer(String text, Color textColor, TextSize textSize, Typeface font) {
        this.text = text;
        // Create an empty, mutable bitmap
        bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_4444);
        // get a canvas to paint over the bitmap
        canvas = new Canvas(bitmap);
        bitmap.eraseColor(0);

        // Draw the text
        textPaint = new Paint();
        System.out.println(textPaint.measureText(text)); // TODO faire dépendre le setTextSize du résultat de measureText ( ou bien le faire défiler ? )
        textPaint.setTextSize(getSize(textSize));
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor.toArgb());
        if(font!=null){
            textPaint.setTypeface(font);
        }
        //textPaint.setARGB(0xff, 0x00, 0x00, 0x00);
        // draw the text centered
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public TextRenderer(String text, Color textColor, TextSize textSize ) {
        this(text, textColor, textSize, null);
    }

    public TextRenderer(String text, Color textColor){
        this(text, textColor, TextSize.Title);
    }

    public TextRenderer addBackground(Drawable drawable){
        background = drawable;
        float imageRatio = background.getIntrinsicHeight() / (background.getIntrinsicWidth()*1f);
        if(imageRatio<1f){
            // height<width
            background.setBounds(0, (int) (256-0.5f*(512*imageRatio)), 512, (int) (256+0.5f*(512*imageRatio)));
        }
        else{
            background.setBounds((int) (256-0.5f*background.getIntrinsicWidth()), 0 , (int) (256+0.5f*background.getIntrinsicWidth()), 512);
        }
        //background.setBounds(0, 100, 512, 300);
        hasBeenLoaded = false;
        return this;
    }

    public void DrawTextOnCanvas(){
        bitmap.eraseColor(0);
        if(background != null){
            background.draw(canvas);
        }
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText(text, xPos,yPos, textPaint);
    }

    @Override
    public void Load(GL10 gl){
        if(gameObject.scene != null){
            DrawTextOnCanvas();
            textureId = this.gameObject.scene.loadBitmap(gl, bitmap);
            hasBeenLoaded = true;
        }
    }

    @Override
    public void Draw(GL10 gl){
        if(!hasBeenLoaded){
            Load(gl);
        }
        // bind the previously generated texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        // Point to our buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // set the colour for the square
        gl.glColor4f (1.0f, 1.0f, 1.0f, 1.0f);

        // Set the face rotation
        gl.glFrontFace(GL10.GL_CW);

        // Point to our vertex buffer
        gl.glVertexPointer (3, GL10.GL_FLOAT, 0, shape.buffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.buffer);

        // Draw the vertices as triangle strip
        gl.glDrawArrays (GL10.GL_TRIANGLE_STRIP, 0, shape.vertex.length / 3);

        // Disable the client state before leaving
        gl.glDisableClientState (GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    public boolean isInside(float x, float y) {
        Transform t = this.gameObject.transform;
        float centerX = t.ScreenPositionX(); // position au centre de l'objet en X
        float centerY = t.ScreenPositionY();

        // on fait tout ça pour trouver la position du clic sur le sprite
        float rendererScreenWidth = (t.scaleX/ Camera.main.getSize())*Transform.gameUnitX();
        float startScreenX = centerX - rendererScreenWidth/2f;
        float endScreenX = centerX + rendererScreenWidth/2f;
        float startScreenY = centerY - rendererScreenWidth/2f;
        float endScreenY = centerY + rendererScreenWidth/2f;
        if(x >=startScreenX && x < endScreenX && y >= startScreenY && y<endScreenY){
            int pixelX = (int) (((x-startScreenX)/(rendererScreenWidth))*512);
            int pixelY = (int) (((y-startScreenY)/(rendererScreenWidth))*512);
            if(pixelX >=0 && pixelX<512 && pixelY>=0 && pixelY<512){
                return bitmap.getPixel(pixelX, pixelY) != 0; // si le pixel n'est pas transparent : on a touché le background
            }
        }
        return false;
    }
}
