package mygame.enemy;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import mygame.GameEntity;
import mygame.GameLogic;

/**
 *
 * @author scott
 */
public class EnemySpawner extends GameEntity {
    private static float INTERVAL = 2f;
    
    private int maxEnemiesAtOnce;
    private float checkInterval;
    private EnemyType enemyType;
    
    public EnemySpawner(GameLogic game, int maxAtOnce, EnemyType enemyType) {
        super(game);
        
        this.type = GameEntity.TYPE.Other;
        this.maxEnemiesAtOnce = maxAtOnce;
        this.checkInterval = INTERVAL;
        this.enemyType = enemyType;
        
        birth();
    }
    
    /*public void init() {
        // check for enemies:
        String typeName = enemyType.name().toLowerCase();
        
        for (int ix = 0; ix < 100; ix++) {
            String search = typeName + ix;
            Spatial item = game.getGameLevel().getChild(search);
            
            if (item != null) {
                Enemy e = new Enemy(game, enemyType, item);
            }
        }
    }*/
    
    @Override
    public void update(float tpf) {
        checkInterval -= tpf;
        
        if (checkInterval <= 0f) {
            int count = 0;
            for (Enemy e : game.getEnemies()) {
                if (e.getEnemyType() == enemyType) {
                    count++;
                }
            }
            float yloc = 5f;
            if (enemyType == EnemyType.IMP)
            {
                yloc = 10f;
            }
            
            if(count < maxEnemiesAtOnce) {
                //find random position in game level
                Vector3f min = null;
                min = ((BoundingBox) game.getGameLevel().getWorldBound()).getMin(min);
                Vector3f max = null;
                max = ((BoundingBox) game.getGameLevel().getWorldBound()).getMax(max);
                Vector3f loc = new Vector3f(FastMath.rand.nextFloat() * (max.x - min.x) + min.x,
                                            yloc,
                                            FastMath.rand.nextFloat() * (max.z - min.z) + min.z);

                //create new enemy and place at random position
                Enemy e = Enemy.createEnemy(game, loc, enemyType);
            }
            
            checkInterval = INTERVAL;
        }
    }
}
