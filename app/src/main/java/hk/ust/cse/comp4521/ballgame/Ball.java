package hk.ust.cse.comp4521.ballgame;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class Ball {
    float radius = 10; // Ball's radius
    float x = radius + 20; // Ball's center (x,y)
    float y = radius + 40;
    float speedX = 10; // Ball's speed (x,y)
    float speedY = 6;
    int score = 0;
    private RectF bounds; // Needed for Canvas.drawOval
    private Paint paint; // The paint style, color used for drawing

    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundPoolMap;
    private AudioManager mAudioManager;
    private Context mContext;
    int streamVolume;

    // Constructor
    public Ball(int color, Context c) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        mContext = c;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        mSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager) mContext
                .getSystemService(Context.AUDIO_SERVICE);

        mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.ping_pong, 1));

        streamVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public void moveBall(Box box, Paddle paddle) {
        // Get new (x,y) position
        x += speedX;
        y += speedY;
        // Detect collision and react
        if (x + radius > box.xMax) {
            speedX = -speedX;
            x = box.xMax - radius;
            mSoundPool.play(mSoundPoolMap.get(1), streamVolume, streamVolume,
                    100, 0, 1f);
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin + radius;
            mSoundPool.play(mSoundPoolMap.get(1), streamVolume, streamVolume,
                    100, 0, 1f);
        }
        if ((y + radius > paddle.yMin) && (x - radius > paddle.xMin)
                && (x + radius < paddle.xMax)) {
            speedY = -speedY;
            y = paddle.yMin - radius;
            score++;
            mSoundPool.play(mSoundPoolMap.get(1), streamVolume, streamVolume,
                    100, 0, 1f);
        } else if (y + radius > box.yMax) {
            speedY = -speedY;
            y = box.yMin + radius;
            score--;
        } else if (y - radius < box.yMin) {
            speedY = -speedY;
            y = box.yMin + radius;
            mSoundPool.play(mSoundPoolMap.get(1), streamVolume, streamVolume,
                    100, 0, 1f);
        }
    }

    public void draw(Canvas canvas) {
        bounds.set(x - radius, y - radius, x + radius, y + radius);
        canvas.drawOval(bounds, paint);
    }
}
