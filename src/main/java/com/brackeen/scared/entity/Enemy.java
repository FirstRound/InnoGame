package com.brackeen.scared.entity;

import com.brackeen.scared.Map;
import com.brackeen.scared.SoftTexture;
import com.brackeen.scared.Stats;
import com.brackeen.scared.controllers.DecisionController;
import com.brackeen.scared.genetic.Genome;

public class Enemy extends Entity {

    public static final int NUM_IMAGES = 15;

    public static final int STATE_ASLEEP = 0;
    public static final int STATE_TERMINATE = 1;
    public static final int STATE_MOVE_LEFT = 2;
    public static final int STATE_MOVE_FAR_LEFT = 3;
    public static final int STATE_MOVE_RIGHT = 4;
    public static final int STATE_MOVE_FAR_RIGHT = 5;
    public static final int LAST_STATE_WITH_ANIM = STATE_MOVE_FAR_RIGHT;
    public static final int STATE_READY = 6;
    public static final int STATE_AIM = 7;
    public static final int STATE_FIRE = 8;
    public static final int STATE_HURT = 9;
    public static final int STATE_DYING = 10;
    public static final int STATE_DEAD = 11;

    // @formatter:off
    //                                 state =   0   1   2   3   4   5   6   7   8   9  10  11
    private static final int[] STATE_TEXTURE = { 0,  0,  2,  4,  6,  8,  0, 10, 11, 12, 13, 14 };
    private static final int[] STATE_TICKS =  { 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12 };
    // @formatter:on

    private static final float STEP_SIZE = 0.05f;

    private final SoftTexture[] textures;
    private final Map map;
    private final Stats stats;
    private int state;
    private int health = 100;
    private int ticksRemaining = 0;
    private final int TICKS = 20;
    private double aimAngle;

    private final int damage = 10;

    private int points = 0;

    private boolean enemyVisibilityNeedsCalculation;
    private boolean isEnemyVisible;
    private int kills = 0;

    private DecisionController decisionController = null;
    private Genome genome = null;

    public boolean canMakeAction() {
        if (++ticksRemaining < TICKS) {
            return false;
        }
        else {
            ticksRemaining = 0;
            return true;
        }
    }

    public void flushTicks() {
        ticksRemaining = 0;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public int getDamage() {
        return damage;
    }

    public Enemy(Map map, Stats stats, SoftTexture[] textures, float x, float y) {
        super(0.25f, x, y);
        this.textures = textures;
        this.map = map;
        this.stats = stats;
        setTexture(textures[0]);


        //setTextureScale(getTextureScale() / 2); // For 128x128 textures
        setZ(-4f / DEFAULT_PIXELS_PER_TILE);
        setState(STATE_ASLEEP);

    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    public Genome getGenome() {
        return genome;
    }

    public void initDecisionController() {
        decisionController = new DecisionController(this, map.getTilesMatrix());

    }

    public DecisionController getDecisionController() {
        return decisionController;
    }

    public void setState(int state) {
        if (this.state != state) {
            ticksRemaining = 0;
            this.state = state;
            if (state == STATE_DEAD) {
                setRadius(0); // Prevent future collisions
            }
        }
    }

    public boolean hurt(int points) {
        health -= points;
        System.out.println("Health: " + health);
        if (health <= 0) {
            setState(STATE_DYING);
            delete(); // delete enemy from map
            return false;
        }
        return true;
    }

    public int getState() {
        return state;
    }

    @Override
    public boolean notifyCollision(Entity movingEntity) {
        return true;
    }

    //TODO: try to understand this
    private boolean isEnemyVisible(float angleToEnemy) {
        return true;
    }

    @Override
    public void tick() {
        int textureIndex = STATE_TEXTURE[state];
        if (state <= LAST_STATE_WITH_ANIM && ((ticksRemaining / 12) & 1) == 0) {
            textureIndex++;
        }
        setTexture(textures[textureIndex]);
    }



    private boolean isAlive() {
        return state != STATE_DEAD;
    }

    private void setKills(int i) {
        this.kills = i;
    }

    private int getKills() {
        return kills;
    }

    //TODO: try to understand
    private boolean isCollision(float x, float y) {
        int minTileX = (int) (x - getRadius());
        int maxTileX = (int) (x + getRadius());
        int minTileY = (int) (y - getRadius());
        int maxTileY = (int) (y + getRadius());

        for (int tileY = minTileY; tileY <= maxTileY; tileY++) {
            for (int tileX = minTileX; tileX <= maxTileX; tileX++) {
                if (map.isSolidAt(tileX, tileY)) {
                    return true;
                }
            }
        }

        return false;
    }

}
