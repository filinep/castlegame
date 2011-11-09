package mygame;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.ui.Picture;

/**
 *
 * @author filipe
 */
public class HUD extends GameEntity {
    private static float DAMAGED_TIME = 3f;
    private BitmapText health;
    private BitmapText weapon;
    private Picture damageIndicator;
    private float playerDamaged = 0f;
    
    public HUD(GameLogic gl) {
        super(gl);
        BitmapFont guiFont = Main.get().getGuiFont();
        
        health = new BitmapText(guiFont, false);          
        health.setSize(guiFont.getCharSet().getRenderedSize() * 1.5f);
        health.setColor(ColorRGBA.White);
        health.setText("Health: " + game.getPlayer().getHealth());
        health.setLocalTranslation(10, Main.get().getSettings().getHeight(), 0);
        this.attachChild(health);
        
        weapon = new BitmapText(guiFont, false);          
        weapon.setSize(guiFont.getCharSet().getRenderedSize() * 1.5f);
        weapon.setColor(ColorRGBA.White);
        weapon.setText("Weapon: " + game.getPlayer().getWeapon().getName() +
                       " AMMO = " + game.getPlayer().getWeapon().getCurrentAmmo());
        weapon.setLocalTranslation(10, Main.get().getSettings().getHeight() - health.getHeight(), 0);
        this.attachChild(weapon);
        
        damageIndicator = new Picture("DamageIndicator");
        damageIndicator.setImage(Main.get().getAssetManager(), "Textures/damageIndicator.png", true);
        damageIndicator.setWidth(Main.get().getSettings().getWidth());
        damageIndicator.setHeight(Main.get().getSettings().getHeight());
        damageIndicator.setPosition(0, 0);
        this.attachChild(damageIndicator);
    }
    
    public void setPlayerDamaged() {
        playerDamaged = DAMAGED_TIME;
    }
    
    @Override
    public void update(float tpf) {
        weapon.setText("Weapon: " + game.getPlayer().getWeapon().getName().toLowerCase() +
                       "\nAMMO = " + game.getPlayer().getWeapon().getCurrentAmmo());
        health.setText("Health: " + game.getPlayer().getHealth());
        
        damageIndicator.getMaterial().setColor("Color", 
                                new ColorRGBA(1f, 0f, 0f, .5f - (DAMAGED_TIME - playerDamaged) / (2*DAMAGED_TIME)));
        
        if (playerDamaged > 0)
            playerDamaged -= tpf;
    }
}
