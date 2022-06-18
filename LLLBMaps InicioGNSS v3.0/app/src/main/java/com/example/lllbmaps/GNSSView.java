package com.example.lllbmaps;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.GnssStatus;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GNSSView extends View {
    private GnssStatus newStatus;
    private int r;
    private int height,width;


    public GNSSView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void onSatelliteStatusChanged(GnssStatus status) {
        newStatus=status;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // coletando informações do tamanho tela de desenho
        width=getMeasuredWidth();
        height=getMeasuredHeight();

        // definindo o raio da esfera celeste
        if (width<height)
            r=(int)(width/2*0.9);
        else
            r=(int)(height/2*0.9);

        // configurando o pincel para desenhar a projeção da esfera celeste
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.WHITE);

        // Desenha a projeção da esfera celeste
        // desenhando círculos concêntricos
        int radius=r;
        canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);
        radius=(int)(radius*Math.cos(Math.toRadians(45)));
        canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);
        radius=(int)(radius*Math.cos(Math.toRadians(60)));
        canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);

        //desenhando os eixos
        canvas.drawLine(computeXc(0),computeYc(-r),computeXc(0),computeYc(r),paint);
        canvas.drawLine(computeXc(-r),computeYc(0),computeXc(r),computeYc(0),paint);

        // configura o pincel para desenhar os satélites
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);

        // desenhando os satélites
        if (newStatus!=null) {
            for(int i=0;i<newStatus.getSatelliteCount();i++) {
                float az=newStatus.getAzimuthDegrees(i);
                float el=newStatus.getElevationDegrees(i);
                float x=(float)(r*Math.cos(Math.toRadians(el))*Math.sin(Math.toRadians(az)));
                float y=(float)(r*Math.cos(Math.toRadians(el))*Math.cos(Math.toRadians(az)));
                canvas.drawCircle(computeXc(x), computeYc(y), 10, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(30);
                String satID=newStatus.getSvid(i)+"";
                canvas.drawText(satID, computeXc(x)+10, computeYc(y)+10, paint);
            }
        }
    }
    private int computeXc(double x) {
        return (int)(x+width/2);
    }
    private int computeYc(double y) {
        return (int)(-y+height/2);
    }

}