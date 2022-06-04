package com.umg.shootergame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.umg.shootergame.object.Circle;
import com.umg.shootergame.object.Enemy;
import com.umg.shootergame.object.Player;
import com.umg.shootergame.object.Projectile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    //private final Enemy enemy;
    private GameLoop gameLoop;
//    private Context context;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Projectile> projectileList = new ArrayList<Projectile>();
    private int joystickPointerId = 0;
    private int projectilesAmount = 0;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

//        this.context = context;
        gameLoop = new GameLoop(this, surfaceHolder);

        joystick = new Joystick(350, 750,100, 60);
        player = new Player(getContext(), joystick,1500,800, 30);

        //enemy = new Enemy(getContext(), player, 200, 10, 20);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(joystick.getIsPressed()) {
                    projectilesAmount++;
                } else if(joystick.isPressed((double)event.getX(),(double)event.getY())){
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else {
                    projectilesAmount++;
                }
//                player.setPosition((double)event.getX(),(double)event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double)event.getX(),(double)event.getY());
                }
//                player.setPosition((double)event.getX(),(double)event.getY());
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawFPS(canvas);
//        drawUPS(canvas);
        player.draw(canvas);
        joystick.draw(canvas);
        for(Enemy enemy : enemyList) {
            enemy.draw(canvas);
        }
        for(Projectile projectile : projectileList) {
            projectile.draw(canvas);
        }
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.stats);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 200, paint);
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.stats);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 100, paint);
    }

    public void update(){
        player.update();
        joystick.update();

        if(Enemy.readyToSpawn()) {
            enemyList.add(new Enemy(getContext(), player));
        }

        while (projectilesAmount > 0) {
            projectileList.add(new Projectile(getContext(), player));
            projectilesAmount--;
        }
        for(Enemy enemy : enemyList) {
            enemy.update();
        }

        for(Projectile projectile : projectileList) {
            projectile.update();
        }

        //Enemy and bullets collision
        Iterator<Enemy> enemyIterator = enemyList.iterator();
        while (enemyIterator.hasNext()) {
            Circle enemy = enemyIterator.next();
            if(Circle.isColliding(enemy, player)) {
                enemyIterator.remove();
                player.setHealthPoints(player.getHealthPoints() - 1);
                continue;
            }

            Iterator<Projectile> projectileIterator = projectileList.iterator();
            while (projectileIterator.hasNext()) {
                Circle projectile = projectileIterator.next();
                if(Circle.isColliding(projectile,enemy)) {
                    projectileIterator.remove();
                    enemyIterator.remove();
                    break;
                }
            }
        }
    }
}
