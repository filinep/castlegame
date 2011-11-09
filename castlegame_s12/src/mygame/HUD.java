/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

/**
 *
 * @author filipe
 */
public class HUD extends Node {
    private BitmapText health;
    private BitmapText weapon;
    GameLogic game;
    
    public HUD(GameLogic gl) {
        game = gl;
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
    }
    
    public void update() {
        weapon.setText("Weapon: " + game.getPlayer().getWeapon().getName() +
                       " AMMO = " + game.getPlayer().getWeapon().getCurrentAmmo());
        health.setText("Health: " + game.getPlayer().getHealth());
    }
}
