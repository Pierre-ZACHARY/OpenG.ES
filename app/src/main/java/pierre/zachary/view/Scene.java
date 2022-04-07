package pierre.zachary.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.util.SparseIntArray;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

public abstract class Scene implements GLSurfaceView.Renderer
{
    public Context context;
    public Resources resources;
    public SparseIntArray images = new SparseIntArray ();
    public float width;
    public float height;

    public Scene (Context context)
    {
        this.context = context;
        this.resources = context.getResources ();
    }

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    @Override
    public void onDrawFrame (GL10 gl)
    {

        //GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        //Matrix.setIdentityM(mViewMatrix,0);
        //Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        //Matrix.setIdentityM(mModelMatrix,0);
        //Matrix.translateM(mModelMatrix, 0, mSquarePosition[0], mSquarePosition[1], 0);
        //Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mModelMatrix, 0);

        /* on appelle la méthode dessin du carré élémentaire */
        //mSquare.draw(scratch, mTextureDataHandle);


//      // clear Screen and Depth Buffer
        gl.glClear (GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
//      // Reset the Modelview Matrix
        gl.glLoadIdentity ();

        draw (gl);

    }


    @Override
    public void onSurfaceChanged (GL10 gl, int width, int height)
    {
        this.width = width;
        this.height = height;

        gl.glViewport (0, 0, width, height); // Reset The Current Viewport
        gl.glMatrixMode (GL10.GL_PROJECTION); // Select The Projection Matrix
        gl.glLoadIdentity (); // Reset The Projection Matrix

        gl.glOrthof (0, 10f*width, 0, 10f*(height/width)*height, -1f, 1f);
        //gl.glTranslatef (0f, -height/2, 0.0f); // move the camera !!


        gl.glMatrixMode (GL10.GL_MODELVIEW); // Select The Modelview Matrix
        gl.glLoadIdentity (); // Reset The Modelview Matrix

        load (gl);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config)
    {
        gl.glEnable(GL10.GL_TEXTURE_2D);            //Enable Texture Mapping ( NEW )
        gl.glShadeModel(GL10.GL_SMOOTH);            //Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    //Black Background
        gl.glClearDepthf(1.0f);                     //Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL);             //The Type Of Depth Testing To Do

        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_BLEND);


        //Really Nice Perspective Calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        init (gl);
    }


    public void init (GL10 gl)
    {

    }

    public void load (GL10 gl)
    {

    }

    public void draw (GL10 gl)
    {

    }

    private static int next (GL10 gl)
    {
        int[] temp = new int[1];
        gl.glGenTextures (1, temp, 0);
        return temp[0];
    }

    public int image (GL10 gl, int resource)
    {
        int id = next (gl);
        images.put (resource, id);

        gl.glBindTexture (GL10.GL_TEXTURE_2D, id);

        gl.glTexParameterf (GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf (GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        gl.glTexParameterf (GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf (GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        gl.glTexEnvf (GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

        BitmapFactory.Options options = new BitmapFactory.Options ();
        options.inScaled = false;

        InputStream input = resources.openRawResource (resource);
        Bitmap bitmap;
        try
        {
            bitmap = BitmapFactory.decodeStream (input, null, options);
        }
        finally
        {
            try
            {
                input.close ();
            }
            catch (IOException e)
            {
                // Ignore.
            }
        }

//       Matrix flip = new Matrix ();
//       flip.postScale (1f, -1f);
//       bitmap = Bitmap.createBitmap (bitmap, 0, 0, bitmap.getWidth (), bitmap.getHeight (), flip, true);

        GLUtils.texImage2D (GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        return id;
    }

}
