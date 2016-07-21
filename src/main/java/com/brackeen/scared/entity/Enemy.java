package com.brackeen.scared.entity;

import com.brackeen.app.App;
import com.brackeen.scared.Map;
import com.brackeen.scared.SoftTexture;
import com.brackeen.scared.Stats;
import com.brackeen.scared.movings.EnemyMovingController;

import java.awt.geom.Point2D;
import java.util.List;

public class Enemy extends Entity {

    public static final int NUM_IMAGES = 15;

    private static final int STATE_ASLEEP = 0;
    private static final int STATE_TERMINATE = 1;
    private static final int STATE_MOVE_LEFT = 2;
    private static final int STATE_MOVE_FAR_LEFT = 3;
    private static final int STATE_MOVE_RIGHT = 4;
    private static final int STATE_MOVE_FAR_RIGHT = 5;
    private static final int LAST_STATE_WITH_ANIM = STATE_MOVE_FAR_RIGHT;
    private static final int STATE_READY = 6;
    private static final int STATE_AIM = 7;
    private static final int STATE_FIRE = 8;
    private static final int STATE_HURT = 9;
    private static final int STATE_DYING = 10;
    private static final int STATE_DEAD = 11;

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
    private int health;
    private final double p; //probability of changing states
    private int ticksRemaining;
    private int ticks;
    private double aimAngle;

    private boolean enemyVisibilityNeedsCalculation;
    private boolean isEnemyVisible;
    private int kills = 0;

    private EnemyMovingController movingController = null;

    public Enemy(Map map, Stats stats, SoftTexture[] textures, float x, float y, int type) {
        super(0.25f, x, y);
        this.textures = textures;
        this.map = map;
        this.stats = stats;
        setTexture(textures[0]);


        //setTextureScale(getTextureScale() / 2); // For 128x128 textures
        setZ(-4f / DEFAULT_PIXELS_PER_TILE);
        setState(STATE_ASLEEP);

        switch (type) {
            case 1:
            default:
                health = 20;
                STATE_TICKS[STATE_READY] = 10;
                STATE_TICKS[STATE_AIM] = 24;
                p = .1;
                break;

            case 2:
                health = 30;
                STATE_TICKS[STATE_READY] = 12;
                STATE_TICKS[STATE_AIM] = 24;
                p = .05;
                break;

            case 3:
                health = 50;
                STATE_TICKS[STATE_READY] = 6;
                STATE_TICKS[STATE_AIM] = 18;
                p = .1;
                break;

            case 4:
                health = 80;
                STATE_TICKS[STATE_READY] = 0;
                STATE_TICKS[STATE_AIM] = 12;
                p = .03;
                break;
        }
    }

    public void initMoveController() {
        movingController = new EnemyMovingController(map.getTilesMatrix()); 
        movingController.setCurrentPosition(new Point2D.Double(getX(), getY()));
    }

    private void setState(int state) {
        if (this.state != state) {
            this.state = state;
            if (state == STATE_DEAD) {
                setRadius(0); // Prevent future collisions
            }
            ticksRemaining = STATE_TICKS[state];
        }
    }

    public boolean hurt(int points) {

        if (health <= 0) {
            return false;
        } else {
            health -= points;
            boolean gotoHurtState = false;

            if (health <= 0) {
                gotoHurtState = true;
            } else if (state == STATE_FIRE) {
                // 50% of interrupting firing
                if (Math.random() < .5) {
                    gotoHurtState = true;
                }
            } else if (state != STATE_HURT) {
                gotoHurtState = true;
            }

            if (gotoHurtState) {
                setState(STATE_HURT);
            }
            return true;
        }
    }

    @Override
    public boolean notifyCollision(Entity movingEntity) {
        return true;
    }

    private boolean isEnemyVisible(float angleToEnemy) {
        if (enemyVisibilityNeedsCalculation) {
            enemyVisibilityNeedsCalculation = false;
            isEnemyVisible = false;
            Point2D.Float point = map.getWallCollision(getX(), getY(), (float) Math.toDegrees(angleToEnemy));
            if (point != null) {
                List<Entity> playerHit = map.getCollisions(Enemy.class, getX(), getY(), point.x, point.y);
                if (playerHit.size() > 0) {
                    isEnemyVisible = true;
                }
            }
        }
        return isEnemyVisible;
    }

    private Enemy getNearestEnemy() {//TODO: make graph search
        List<Enemy> enemies = map.getEnemies();
        double max_distance = -10;
        Enemy nearest = null;
        double x = getX(), y = getY();
        for (Enemy enemy : enemies) {
            if (enemy == this)
                continue;
            double tmp_x = enemy.getX(), tmp_y = enemy.getY();
            double cur_distance = Point2D.distance(x, y, tmp_x, tmp_y);
            if (max_distance < cur_distance) {
                max_distance = cur_distance;
                nearest = enemy;
            }
        }
        return nearest;
    }

    @Override
    public void tick() {
        enemyVisibilityNeedsCalculation = true;
        //Player player = map.getPlayer();
        Enemy nearest_enemy = getNearestEnemy();


        float stepx = 0;
        float stepy = 0;
        float dx = nearest_enemy.getX() - getX();
        float dy = nearest_enemy.getY() - getY();
        float angleToEnemy = (float) Math.atan2(dy, dx);

        if ((ticksRemaining <= 0 || state == STATE_TERMINATE) && Math.abs(dx) < 2f && Math.abs(dy) < 2f && state < STATE_READY) {
            // Player is very close - move immediately or fire
            double pq = Math.random();

            if (pq < 0.1f) {
                setState(STATE_MOVE_FAR_LEFT);
            } else if (pq < 0.2f) {
                setState(STATE_MOVE_FAR_RIGHT);
            } else {
                setState(STATE_FIRE);
            }
        } else if (state > STATE_ASLEEP && state < STATE_READY && Math.random() < p) {
            // When moving, randomly change to another move state
            int s = (int) Math.round(Math.random() * 6);
            switch (s) {
                case 0:
                default:
                    setState(STATE_TERMINATE);
                    break;
                case 1:
                    setState(STATE_MOVE_LEFT);
                    break;
                case 2:
                    setState(STATE_MOVE_RIGHT);
                    break;
                case 3:
                    setState(STATE_MOVE_FAR_LEFT);
                    break;
                case 4:
                    setState(STATE_MOVE_FAR_RIGHT);
                    break;
                case 5:
                    if (isEnemyVisible(angleToEnemy)) {
                        setState(STATE_READY);
                    } else {
                        setState(STATE_TERMINATE);
                    }
                    break;
            }
        }

        switch (state) {
            case STATE_ASLEEP:
                if (isEnemyVisible(angleToEnemy)) {
                    setState(STATE_TERMINATE);
                }
                break;

            case STATE_TERMINATE:
                stepx = (float) Math.cos(angleToEnemy) * STEP_SIZE;
                stepy = (float) Math.sin(angleToEnemy) * STEP_SIZE;
                break;

            case STATE_MOVE_LEFT:
                stepx = (float) Math.cos(angleToEnemy + Math.PI / 4) * STEP_SIZE;
                stepy = (float) Math.sin(angleToEnemy + Math.PI / 4) * STEP_SIZE;
                break;

            case STATE_MOVE_RIGHT:
                stepx = (float) Math.cos(angleToEnemy - Math.PI / 4) * STEP_SIZE;
                stepy = (float) Math.sin(angleToEnemy - Math.PI / 4) * STEP_SIZE;
                break;

            case STATE_MOVE_FAR_LEFT:
                stepx = (float) Math.cos(angleToEnemy + Math.PI / 2) * STEP_SIZE;
                stepy = (float) Math.sin(angleToEnemy + Math.PI / 2) * STEP_SIZE;
                break;

            case STATE_MOVE_FAR_RIGHT:
                stepx = (float) Math.cos(angleToEnemy - Math.PI / 2) * STEP_SIZE;
                stepy = (float) Math.sin(angleToEnemy - Math.PI / 2) * STEP_SIZE;
                break;

            case STATE_READY:
                if (ticksRemaining <= 0) {
                    setState(STATE_AIM);
                }
                break;

            case STATE_AIM:
                if (ticksRemaining <= 0) {
                    if (nearest_enemy.isAlive() && isEnemyVisible(angleToEnemy)) {
                        aimAngle = angleToEnemy;
                        setState(STATE_FIRE);
                    } else {
                        setState(STATE_TERMINATE);
                    }
                }
                break;

            case STATE_FIRE:
                if (ticksRemaining <= 0) {
                    App.getApp().getAudio("/sound/laser0.wav").play();
                    stats.numEnemyShotsFired++;

                    boolean actuallyHurt = nearest_enemy.hurt(10);
                                
                    


                    setState(STATE_TERMINATE);
                }

                break;

            case STATE_HURT:
                if (ticksRemaining <= 0 || health <= 0) {

                    if (health <= 0) {
                        App.getApp().getAudio("/sound/enemy_dead.wav").play();
                        setState(STATE_DYING);
                    } else if (Math.random() < .666) {
                        setState(STATE_TERMINATE);
                    } else {
                        setState(STATE_ASLEEP);
                        // immediate fire
                        aimAngle = angleToEnemy;
                        setState(STATE_FIRE);
                    }

                }
                break;

            case STATE_DYING:
                if (ticksRemaining <= 0) {
                    setState(STATE_DEAD);
                    nearest_enemy.setKills(nearest_enemy.getKills() + 1);
                }
                break;
        }

        if (true) { //TODO: change this!
            float newX = getX() + stepx;
            float newY = getY() + stepy;

            if (!isCollision(newX, newY)) {
                setLocation(newX, newY);
            } else if (!isCollision(newX, getY())) {
                setX(newX);
            } else if (!isCollision(getX(), newY)) {
                setY(newY);
            }
        }

        ticksRemaining--;
        ticks++;
        int textureIndex = STATE_TEXTURE[state];
        if (state <= LAST_STATE_WITH_ANIM && ((ticks / 12) & 1) == 0) {
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
