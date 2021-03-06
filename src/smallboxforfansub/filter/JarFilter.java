/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.filter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author The Wingate 2940
 */
public class JarFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        //Show folders in FileChooser
        if (f.isDirectory()) {
            return true;
        }

        //Show image files in FileChooser
        if(f.getName().endsWith(".jar")){
            return true;
        }

        //It's enough
        return false;
    }

    @Override
    public String getDescription() {
        //Show *** in selector
        return "Jar files (*.jar)";
    }
    
}
