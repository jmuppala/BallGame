package hk.ust.cse.comp4521.ballgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Box {
    int xMin, xMax, yMin, yMax;
    private Paint paint; // paint style and color
    private Rect bounds;

    public Box(int color) {
        paint = new Paint();
        paint.setColor(color);
        bounds = new Rect();
    }

    public void set(int x, int y, int width, int height) {
        xMin = x + 1;
        xMax = x + width - 1;
        yMin = y + 1;
        yMax = y + height - 1;
        // The box's bounds do not change unless the view's size changes
        bounds.set(xMin, yMin, xMax, yMax);
    }

    // Draw the box onto the canvas
    public void draw(Canvas canvas) {
        canvas.drawRect(bounds, paint);
    }
}
