/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Spatial;

/**
 *
 * @author scott
 */
public class EnemySpawner extends GameEntity {
    private Enemy.ENEMYTYPE enemyType;
    
    public EnemySpawner(GameLogic game, Enemy.ENEMYTYPE type) {
        super(game);
    }
    
    public void init(Scene gameLevel) {
        // check for enemies:
        String name = enemyType.name().toLowerCase();
        
        for (int ix = 0; ix < 100; ix++) {
            String search = name + ix;
            Spatial item = game.getGameLevel().getChild(search);
            
            if (item != null) {
                Enemy e = new Enemy(game, enemyType, item);
            }
        }
        
    }
}
