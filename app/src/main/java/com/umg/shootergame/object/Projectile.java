package com.umg.shootergame.object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.umg.shootergame.GameLoop;
import com.umg.shootergame.R;

public class Projectile extends Circle{
    public static final double SPEED_PIXELS_PER_SECOND = 800.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
//    private final Player player;

    public Projectile(Context context, Player player) {
        super( context,
                ContextCompat.getColor(context, R.color.spell),
                player.getPositionX(),
                player.getPositionY(),
                15
        );
//        this.player = player;
        velocityX = player.getDirectionX()*MAX_SPEED;
        velocityY = player.getDirectionY()*MAX_SPEED;
    }

    @Override
    public void update() {
        positionX+=velocityX;
        positionY+=velocityY;
    }
}
