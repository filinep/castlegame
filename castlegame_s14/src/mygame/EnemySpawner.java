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
public class EnemySpawner {
    GameLogic game;
    
    EnemySpawner() {}
    
    void init(GameLogic gl, Scene gameLevel)
    {
        game = gl;
        // check for skeletons:
        String skeleton = "skeleton";
        for (int ix = 0; ix < 100; ix++)
        {
            String search = skeleton + ix;
            Spatial item = gameLevel.getChild(search);
            if (item != null)
            {
                Enemy e = new Enemy(game, Enemy.ENEMYTYPE.SKELETON, item);
            }
        }
        
    }
}
