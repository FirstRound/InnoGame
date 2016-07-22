package com.brackeen.scared.action;

/**
 * Created by pisatel on 21.07.16.
 * For special skills actions (health up to 10sec, give 10+ to ammo, etc.)
 * Usual enemy action - go to position
 */
public class SpecialAction implements Action{

    private int ticks;

    public void tick(){

    }

    public void unload(){

    }

    public boolean isFinished(){
        return ticks <= 0;
    }

}
