package hk.ust.cse.comp4521.ballgame;

import java.util.Formatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

public class StatusMessage {
    // Status message to show the Ball's (x,y) position and speed.
    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter = new Formatter(statusMsg);
    private Paint paint;

    // Constructor
    public StatusMessage(int color) {
        paint = new Paint();
        // Set the font face and size of drawing text
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(16);
        paint.setColor(color);
    }

    public void update(Ball ball) {
        // Build status message
        statusMsg.delete(0, statusMsg.length()); // Empty buffer
        formatter.format("Score = %d", ball.score);
    }

    public void draw(Canvas canvas) {
        canvas.drawText(statusMsg.toString(), 10, 30, paint);
    }
}
