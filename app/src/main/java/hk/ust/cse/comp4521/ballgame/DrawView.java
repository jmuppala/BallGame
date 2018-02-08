package hk.ust.cse.comp4521.ballgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
    private Ball ball;
    private Box box;
    private Paddle paddle;
    private StatusMessage statusMsg;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFocusable(true);
        this.requestFocus();

        // declare the bounding box, paddle, ball and status message
        box = new Box(0xff006699); // ARGB
        paddle = new Paddle(Color.RED);
        ball = new Ball(Color.GREEN, context);
        statusMsg = new StatusMessage(Color.CYAN);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE); // if you want another background color

        // Draw the components
        box.draw(canvas);
        paddle.draw(canvas);
        ball.draw(canvas);
        statusMsg.draw(canvas);

        // Update the position of the ball, including collision detection and
        // reaction.
        ball.moveBall(box, paddle);
        statusMsg.update(ball);

        // Delay for a short while before forcing another redraw of the screen
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
        }

        // A call to invalidate causes the Android framework to call the onDraw
        // method of the DrawView
        // Thus every time the screen is refreshed, the framework is again
        // forced to call the onDraw
        // method. This creates the animation on the screen to simulate the game
        // playing
        invalidate();
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
        paddle.set(0, 0, w, h);
    }

    public void right() {
        paddle.moveright();
    }

    public void left() {
        paddle.moveleft();
    }

}
