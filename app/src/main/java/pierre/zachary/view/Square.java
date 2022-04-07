package pierre.zachary.view;

import javax.microedition.khronos.opengles.GL10;

public class Square
{
    Vertex shape,texture;
    int corner=0;
    float x=0;

    public Square()
    {
        shape = new Vertex (new float[]
                {
                        1f,1f,0f,
                        0f,1f,0f,
                        1f,0f,0f,
                        0f,0f,0f,
                });

        texture = new Vertex (new float[]
                {
                        1.0f, 0.0f,
                        0.0f, 0.0f,
                        1.0f, 1.0f,
                        0.0f, 1.0f,
                });
    }

    /** The draw method for the square with the GL context */
    public void draw (GL10 gl, int image, float x, float y, float width, float height, float corner)
    {

        gl.glPushMatrix();

        //x += 10f;
        if (x>8000)
        {
            x = 0;
        }

        //position (gl, (10f*width)/2+x, (10f*(height/width)*height)/2+y, 1f*width, 1f*height, corner);

        gl.glTranslatef (((10f*width)/2+x)-width/2, ((10f*(height/width)*height)/2+y)-height/2, 0f); //MOVE !!! 1f is size of figure if called after scaling, 1f is pixel if called before scaling
        gl.glScalef (width, height, 0f); // ADJUST SIZE !!!


        // bind the previously generated texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, image);

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

        gl.glPopMatrix();
    }

    public void position (GL10 gl, float x, float y, float width, float height, float corner)
    {
        gl.glTranslatef (x-width/2, y-height/2, 0f); //MOVE !!! 1f is size of figure if called after scaling, 1f is pixel if called before scaling

        /*if (corner>0)
        {
            gl.glTranslatef (width/2, height/2, 0f);
            gl.glRotatef (corner, 0f, 0f, 1f); // ROTATE !!!
            gl.glTranslatef (-width/2, -height/2, 0f);

        }*/

        gl.glScalef (width, height, 0f); // ADJUST SIZE !!!

    }
}