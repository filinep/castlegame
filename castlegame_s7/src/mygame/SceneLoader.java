package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.math.Vector3f;
import com.jme3.light.DirectionalLight;

/** 
 * Loads the j3o file and digs into it to grab the meshes and empties therein. 
 * Note there is no reference to the Jme3 handed it, yet the rootNode, 
 * assetManager etc from Jme3 are used. 
 * @author ste3e 
 */
/*
public class SceneLoader {
    //It seems meshes must be Nodes, empties must be Spatials 

    private Node placeholder, scene, floor, box;
    private Spatial playerPos;
    private static BitmapText sysout;//dubug to player screen

    public void init(SimpleApplication a) {
        /*  
         * placeholder is attached to the rootNode supplied by the Jme3. It 
         * is accessed via Main.getJme() which gets the static class  
         * created in Main. getRootNode() is a method found in Application 
         * which Main accesses by extending SimpleAppliaction. The placeholder 
         * is translated down the y axis, and the following Nodes are attached to it. 
         */

        //placeholder = new Node("placeholder");
        //placeholder.setLocalTranslation(new Vector3f(0.0f, -2.5f, 0.0f));

        //r .attachChild(placeholder);


        /* 
         * the j3o file is loaded into the Node scene. We then dig into scene  
         * to grab its children, the empty playerPos-node, and the two 
         * meshes. Note the children have the same names as those found in 
         * the Scene Explorer 
         */
/*
        scene = (Node) Main.grab().getAssetManager().loadModel(
                "Scenes/aScene/basic.j3o");
        playerPos = scene.getChild("playerPos-node");
        floor = (Node) scene.getChild("floor-node");
        box = (Node) scene.getChild("box-node");
        //attach the two meshes to placeholder 
        placeholder.attachChild(floor);
        placeholder.attachChild(box);

        /* 
         * get the position of playerPos and print it's x value to the screen using the 
         * debug function defined in Main.  
         */
/*
        Vector3f player = playerPos.getLocalTranslation();
        String str = "playerEmpty x, y, z is ";
        str += Float.toString(player.x) + ", ";
        str += Float.toString(player.y) + ", ";
        str += Float.toString(player.z);
        Main.grab().debug(str);

        //a light 
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        Main.grab().getRootNode().addLight(sun);
    }

}
*/