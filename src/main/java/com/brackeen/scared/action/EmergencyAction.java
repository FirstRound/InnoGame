package com.brackeen.scared.action;

/**
 * Created by pisatel on 21.07.16.
 * Can be added to list when some strange action be occur (enemy was damaged)
 */
public class EmergencyAction implements Action {

    private int ticks = 1;

    public void tick(){

    }

    public void unload(){

    }

    public boolean isFinished(){
        return ticks <= 0;
    }
}
