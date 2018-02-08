package hk.ust.cse.comp4521.ballgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Paddle {
    int xMin, xMax, yMin, yMax;
    private Paint paint; // paint style and color
    private Rect bounds;
    private int w;
    private int movesize;

    public Paddle(int color) {
        paint = new Paint();
        paint.setColor(color);
        bounds = new Rect();
        movesize = 20;
    }

    public void set(int x, int y, int width, int height) {
        // paddle size is set to one fifth of the screen width and initially
        // centered // on the screen
        xMin = x + 2 * width / 5;
        xMax = x + 3 * width / 5;
        yMin = y + height - 10;
        yMax = y + height - 5;
        w = width;
        // The box's bounds do not change unless the view's size changes
        bounds.set(xMin, yMin, xMax, yMax);
    }

    public void moveright() {
        // move the paddle to the right
        if (xMax < w) {
            xMin += movesize;
            xMax += movesize;
        }

        bounds.set(xMin, yMin, xMax, yMax);
    }

    public void moveleft() {
        // move the paddle to the left
        if (xMin > 0) {
            xMin -= movesize;
            xMax -= movesize;
        }

        bounds.set(xMin, yMin, xMax, yMax);
    }

    // draw the paddle onto the canvas
    public void draw(Canvas canvas) {
        canvas.drawRect(bounds, paint);
    }
}
