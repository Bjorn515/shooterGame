package com.umg.shootergame.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.umg.shootergame.R;

public class HealthBar {

    private final Player player;
    private int width, height, margin;
    private Paint borderPaint;
    private Paint healthPaint;

    public HealthBar(Context context, Player player) {
        this.player = player;
        this.width = 100;
        this.height = 20;
        this.margin = 2;

        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.healthBarBorder);
        borderPaint.setColor(borderColor);

        this.healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, R.color.healthBarHealth);
        healthPaint.setColor(healthColor);
    }

    public void draw(Canvas canvas) {
        float x = (float) player.getPositionX();
        float y = (float) player.getPositionY();
        float distanceToPlayer = 30;
        float healthPointsPercentage = (float) player. getHealthPoints()/player.MAX_HEALTH_POINTS;


        float borderLeft = x - width/2;
        float borderRight = x + width/2;
        float borderBottom = y - distanceToPlayer;
        float borderTop = borderBottom - height;
        canvas.drawRect(borderLeft, borderTop, borderRight, borderBottom, borderPaint);


        float healthWidth = width - 2*margin;
        float healthHeight = height - 2*margin;
        float healthLeft = borderLeft + margin;
        float healthRight = healthLeft + healthWidth * healthPointsPercentage;
        float healthBottom = borderBottom - margin;
        float healthTop = healthBottom - healthHeight;

        canvas.drawRect(healthLeft, healthTop, healthRight, healthBottom, healthPaint);
    }
}
