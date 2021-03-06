package com.chromabits.ugaacm.WarpDrive.control;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.chromabits.ugaacm.WarpDrive.render.Gl2SurfaceView;
import com.chromabits.ugaacm.WarpDrive.render.GlRenderer;
import com.chromabits.ugaacm.WarpDrive.render.UnsupportedHardwareException;

/**
 * Prepares an Android Activity for rendering and creates the engine thread
 * Created by delta6 on 10/30/13.
 */
public class Bootstrapper {

    Activity context;
    Gl2SurfaceView glView;
    GlRenderer glRenderer;

    public Bootstrapper(Activity act){
        context = act;
    }

    /**
     * Create a new game engine thread and prepare the activity with a GlSurfaceView
     * @param game Game to use on the engine
     * @return Engine thread
     */
    public Engine init(Game game) throws UnsupportedHardwareException{
        // Prepare activity
        glRenderer = new GlRenderer();
        glView = new Gl2SurfaceView(context);
        glView.setRenderer(glRenderer);
        glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        // Request to remove window title
        context.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Go fullscreen
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Keep screen on
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Add GL view
        context.setContentView(glView);

        // Prepare engine
        Engine engine = new Engine(game,glRenderer,glView);

        performOnBackgroundThread(engine);

        return engine;
    }

    private static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }
}
