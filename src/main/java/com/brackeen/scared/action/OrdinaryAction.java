package com.brackeen.scared.action;

/**
 * Created by pisatel on 21.07.16.
 */
public class OrdinaryAction implements Action {

    private int ticks;

    public void tick(){

    }

    public void unload(){

    }

    public boolean isFinished(){
        return ticks <= 0;
    }
}
