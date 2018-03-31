package ru.pussy_penetrator.randomcounter.firework;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

/**
 * Created by Sex_predator on 10.12.2016.
 */
class Fireworks {

    /**
     * Maximum number of rockets.
     */
    public int MaxRocketNumber          = 15;
    /**
     * Controls "energy" of firwork explosion. Default value 850.
     */
    public int MaxRocketExplosionEnergy = 1100;
    /**
     * Controls the density of the firework burst. Larger numbers give higher density.
     * Default value 90.
     */
    public int MaxRocketPatchNumber     = 90;
    /**
     * Controls the radius of the firework burst. Larger numbers give larger radius.
     * Default value 68.
     */
    public int MaxRocketPatchLength     = 200;

    /**
     * Controls gravity of the firework simulation.
     * Default value 400.
     */
    public int Gravity = 400;

    transient private Rocket rocket[];
    transient private boolean rocketsCreated = false;

    private int width;
    private int height;

    Fireworks(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void createRockets() {
        rocketsCreated = true;

        Rocket tempRocket[] = new Rocket[MaxRocketNumber];

        for (int i = 0; i < MaxRocketNumber; i++)
            tempRocket[i] = new Rocket(width, height, Gravity);

        rocket = tempRocket;
    }

    public synchronized void reshape(int width, int height) {
        this.width = width;
        this.height = height;

        rocketsCreated = false;
    }

    public void doDraw(Canvas canvas, Paint paint) {
        if (canvas == null)
            return;

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        canvas.drawColor(Color.BLACK);

        int i, e, p, l;
        long s;

        boolean sleep;

        if (!rocketsCreated) {
            createRockets();
        }

        if (rocketsCreated) {
            sleep = true;

            for (i = 0; i < MaxRocketNumber; i++)
                sleep = sleep && rocket[i].sleep;

            for (i = 0; i < MaxRocketNumber; ++i) {
                e = (int) (Math.random() * MaxRocketExplosionEnergy * 3 / 4) +
                    MaxRocketExplosionEnergy / 4 + 1;
                p = (int) (Math.random() * MaxRocketPatchNumber * 3 / 4) +
                    MaxRocketPatchNumber / 4 + 1;
                l = (int) (Math.random() * MaxRocketPatchLength * 3 / 4) +
                    MaxRocketPatchLength / 4 + 1;
                s = (long) (Math.random() * 10000);

                Rocket r = rocket[i];
                if (r.sleep && Math.random() * MaxRocketNumber * l < 2) {
                    r.init(e, p, l, s);
                    r.start();
                }

                if (rocketsCreated)
                    r.doDraw(canvas, paint);
            }
        }
    }
}
