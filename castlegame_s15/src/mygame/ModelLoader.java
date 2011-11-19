package mygame; 
 
import com.jme3.math.Vector3f; 
import com.jme3.scene.Geometry; 
import com.jme3.scene.shape.Box; 
import com.jme3.math.ColorRGBA; 
import com.jme3.material.Material; 
 
 
 
 
/** 
 * This class creates a colored box and positions it at the coordinates given 
 * by the empty playPos, and gets it to lookAt the cube exported in the scene file. 
 * @author ste3e 
 * 
 */ 
/*
public class ModelLoader{ 
 
    public void init(SceneLoader sceneLoader) { 
        //See HelloWorld tutorial for explanation 
        Box box=new Box(new Vector3f(0.0f,0.0f,0.0f), 0.5f,0.5f,0.5f); 
        Geometry aBox=new Geometry("aBox", box); 
        Material mat=new Material(Jimy.grab().getAssetManager(), 
"Common/MatDefs/Misc/SolidColor.j3md"); 
        mat.setColor("m_Color", ColorRGBA.randomColor()); 
        aBox.setMaterial(mat); 
 
         
        aBox.setLocalTranslation(sceneLoader.getPlayerPos()); 
        aBox.lookAt(sceneLoader.getBoxPos(), new Vector3f(0.0f,1.0f,0.0f)); 
         
        Jimy.grab().getRootNode().attachChild(aBox); 
    } 
} 

*/
