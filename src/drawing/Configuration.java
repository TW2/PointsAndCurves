/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author The Wingate 2940
 */
public class Configuration {
    
    private String path = null;
    private JSONObject json = new JSONObject();
    
    public Configuration(){
        
    }
    
    public Configuration(String path){
        this.path = path;
    }
    
    public enum Key{
        Background_Image("bg_img"),         //Image de fond
        Theme("theme"),                     //Theme du L&F
        Force_ISO("force_iso");             //Langage        
        
        String key;
        
        Key(String key){
            this.key = key;
        }
        
        @Override
        public String toString(){
            return key;
        }        
    }
    
    public void setPath(String path){
        this.path = path;
    }
    
    public void save() throws IOException{
        FileWriter file = new FileWriter(path);
        file.write(json.toJSONString());
        file.flush();
        file.close();
    }
    
    public void load() throws IOException, ParseException{
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(path));
        json = (JSONObject)obj;
    }
    
    public void addElement(Key k, String value){
        if(value!=null){
            json.put(k.toString(), value);
        }        
    }
    
    public String getElement(Key k){
        return (String)json.get(k.toString());
    }
}
