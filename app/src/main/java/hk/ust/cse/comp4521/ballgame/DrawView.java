package hk.ust.cse.comp4521.ballgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawViewThread drawviewthread;
    private Ball ball;
    private Box box;
    private Paddle paddle;
    private StatusMessage statusMsg;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this);

        setFocusable(true);
        this.requestFocus();

        // declare the bounding box, paddle, ball and status message
        box = new Box(0xff006699);  // ARGB
        paddle = new Paddle(Color.RED);
        ball = new Ball(Color.GREEN, context);
        statusMsg = new StatusMessage(Color.CYAN);

    }

    public void drawGameBoard(Canvas canvas) {
        canvas.drawColor(Color.WHITE);     //if you want another background color

        // Draw the components
        box.draw(canvas);
        paddle.draw(canvas);
        ball.draw(canvas);
        statusMsg.draw(canvas);

        // Update the position of the ball, including collision detection and reaction.
        ball.moveBall(box, paddle);
        statusMsg.update(ball);

    }

    public void stopGame(){
        if (drawviewthread != null){
            drawviewthread.setRunning(false);
        }
    }

    public void releaseResources(){

    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
        paddle.set(0, 0, w, h);
    }

    public void movePaddleRight(int movesize){
        paddle.moveright(movesize);
    }


    public void movePaddleLeft(int movesize){
        paddle.moveleft(movesize);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        drawviewthread = new DrawViewThread(holder);
        drawviewthread.setRunning(true);
        drawviewthread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        boolean retry = true;
        drawviewthread.setRunning(false);

        while (retry){
            try {
                drawviewthread.join();
                retry = false;
            }
            catch (InterruptedException e){

            }
        }

    }

    public class DrawViewThread extends Thread{
        private SurfaceHolder surfaceHolder;
        private boolean threadIsRunning = true;

        public DrawViewThread(SurfaceHolder holder){
            surfaceHolder = holder;
            setName("DrawViewThread");
        }

        public void setRunning (boolean running){
            threadIsRunning = running;
        }

        public void run() {
            Canvas canvas = null;

            while (threadIsRunning) {

                try {
                    canvas = surfaceHolder.lockCanvas(null);

                    synchronized(surfaceHolder){
                        drawGameBoard(canvas);
                    }
                    sleep(30);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
