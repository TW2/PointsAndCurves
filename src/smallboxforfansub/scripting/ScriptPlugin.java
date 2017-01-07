/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.scripting;


import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import smallboxforfansub.drawing.lib.BSpline;
import smallboxforfansub.drawing.lib.Bezier;
import smallboxforfansub.drawing.lib.ControlPoint;
import smallboxforfansub.drawing.lib.IShape;
import smallboxforfansub.drawing.lib.Layer;
import smallboxforfansub.drawing.lib.Line;
import smallboxforfansub.drawing.lib.Move;
import smallboxforfansub.drawing.lib.Point;
import smallboxforfansub.drawing.lib.ReStart;
import smallboxforfansub.drawing.lib.Sheet;

/**
 *
 * @author The Wingate 2940
 */
public class ScriptPlugin {
    
    private static String actualPath = null;
    private static List<Object> sobjectList = new ArrayList<>();
    JRubyScript ruby;
    JythonScript python;
    
    static String fxScripts = "E:\\Dev\\Projets\\Java\\AssFxMaker\\src\\assfxmaker\\docs\\";
    static String script = "E:\\Dev\\Projets\\Java\\AssFxMaker\\src\\assfxmaker\\docs\\sample.rb";
    static String docsPath = "E:\\Dev\\Projets\\Java\\AssFxMaker\\src\\assfxmaker\\docs\\";
    static int videoHeight = 720, videoWidth = 1280;
    static DefaultTableModel orgModel, resModel;
    
    Frame frame; //We have to display dialog on program (error message)
    
    public ScriptPlugin(Frame frame){
        this.frame = frame;
        ruby = new JRubyScript(frame);
        python = new JythonScript(frame);
    }
    
    public ScriptPlugin(Frame frame, String fxPath, String scriptPath, String docsPath){
        this.frame = frame;
        fxScripts = fxPath;
        script = scriptPath;
        ScriptPlugin.docsPath = docsPath;
        ruby = new JRubyScript(frame);
        python = new JythonScript(frame);
    }
    
    /** <p>Search for all script of this directory.<br />
     * Recherche tous les scripts du répertoire.</p> */
    public void searchForScript(String directory){
        sobjectList.clear(); //Security
        File dir = new File(directory);
        if(dir.exists()){
            for(File f : dir.listFiles()){
                if(f.getPath().endsWith(".rb")){
                    actualPath = f.getPath();
                    ruby.runRubyScript(actualPath);
                }
                if(f.getPath().endsWith(".py")){
                    actualPath = f.getPath();
                    python.runPythonScript(actualPath);
                }
                actualPath = "";
            }
        }
    }
    
    public void runScriptAndDo(Object o){
        if(o instanceof DrawingScript){
            DrawingScript scr = (DrawingScript)o;
            if(scr.getScriptPathname().endsWith(".rb")){
                ruby.runRubyScriptAndDo(scr.getScriptPathname(), scr.getFunction());
            }else if(scr.getScriptPathname().endsWith(".py")){
                python.runPythonScriptAndDo(scr.getScriptPathname(), scr.getFunction());
            }
        }
    }
    
    public String runFxCodeAndDo(String code, String function){
        String value;
        if(code.contains("):") | code.contains("# python")){
            value = python.runPythonCodeAndDo(code, function);
        }else{
            value = ruby.runRubyCodeAndDo(code, function);
        }
        return value;
    }
    
    public List<Object> getSObjectList(){
        return sobjectList;
    }
    
    public void clearSObjectList(){
        sobjectList.clear();
    }
    
    //==========================================================================
    //****************************************************************IO Methods
    //==========================================================================
    
    public void setPaths(String fxPath, String scriptPath, String docsPath){
        ScriptPlugin.fxScripts = fxPath;
        ScriptPlugin.script = scriptPath;
        ScriptPlugin.docsPath = docsPath;
    }
    
    public void setModelsForManagement(DefaultTableModel org, DefaultTableModel res){
        ScriptPlugin.orgModel = org;
        ScriptPlugin.resModel = res;
    }
    
    public void setVideoSize(int videoWidth, int videoHeight){
        ScriptPlugin.videoWidth = videoWidth;
        ScriptPlugin.videoHeight = videoHeight;
    }
    
    public void setFrameReference(Frame frame){
        this.frame = frame;
        ruby = new JRubyScript(frame);
        python = new JythonScript(frame);
    }
    
    //==========================================================================
    //****************************************************************** Methods
    //==========================================================================
    
    private static Color fromHTMLColor(String HTMLColor){
        // HTML -> RRGGBB
        if(HTMLColor.startsWith("#")){HTMLColor=HTMLColor.substring(1);}
        String red = HTMLColor.substring(0, 2);
        String green = HTMLColor.substring(2, 4);
        String blue = HTMLColor.substring(4);
        return new Color(
                Integer.parseInt(red, 16),
                Integer.parseInt(green, 16),
                Integer.parseInt(blue, 16));
    }
    
    //==========================================================================
    //**************************************************************** Functions
    //****************************************************** From KaraModeFunsub
    //==========================================================================
    
    /** <p>Set the path of actual ruby script.<br />
     * Définit le chemin du script ruby actuel.</p> */
    public static void setScript(Object opath){
        script = opath.toString();
    }
    
    /** <p>Get the path of the last ruby script.<br />
     * Obtient le chemin du dernier script ruby.</p> */
    public static String getScript(){
        return script;
    }

    /** <p>Get the absolute path of all scripts.<br />
     * Obtient le chemin de l'endroit où sont sauvegardés tous les scripts.</p> */
    public static String getScriptsPath(){
        return fxScripts;
    }
    
    /** <p>Get the absolute path of all documents.<br />
     * Obtient le chemin de l'endroit où sont sauvegardés tous les documents.</p> */
    public static String getDocsPath(){
        return docsPath;
    }
    
    /** <p>Get the width of the video (defined in the options).<br />
     * Obtient la largeur de la vidéo (défini dans les options).</p> */
    public static int getVideoWidth(){
        return videoWidth;
    }

    /** <p>Get the height of the video (defined in the options).<br />
     * Obtient la hauteur de la vidéo (défini dans les options).</p> */
    public static int getVideoHeight(){
        return videoHeight;
    }
    
    /** <p>Launch the selected link and open a browser.<br />
     * Lance le lien sélectionné et ou un navigateur.</p> */
    public static void openLink(String link){
        boolean hasResult = true;
        if(java.awt.Desktop.isDesktopSupported()){
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if(desktop.isSupported(java.awt.Desktop.Action.BROWSE)){
                try {
                    try {
                        desktop.browse(new java.net.URI(link));
                    } catch (java.io.IOException ex) {hasResult = false;}
                } catch (java.net.URISyntaxException ex) {hasResult = false;}
            }else{
                hasResult = false;
            }
        }else{
            hasResult = false;
        }
        if(hasResult==false){
            java.util.Properties sys = System.getProperties();
            String os = sys.getProperty("os.name").toLowerCase();
            try {
                if(os.contains("windows")==true){
                    Process proc = Runtime.getRuntime().exec("cmd /c start "+link);
                }else{
                    Process proc = Runtime.getRuntime().exec("start "+link);
                }
            } catch (java.io.IOException e) {
                // unsupported
            }
        }        
    }
    
    /** <p>Launch the selected link and open a browser.<br />
     * Lance le lien sélectionné et ou un navigateur.</p> */
    public static void openFile(String doc){
        boolean hasResult = true;
        if(java.awt.Desktop.isDesktopSupported()){
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if(desktop.isSupported(java.awt.Desktop.Action.OPEN)){
                try {
                    desktop.open(new File(doc));
                } catch (Exception ex) {hasResult = false;}                
            }else{
                hasResult = false;
            }
        }else{
            hasResult = false;
        }
        if(hasResult==false){
            java.util.Properties sys = System.getProperties();
            String os = sys.getProperty("os.name").toLowerCase();
            try {
                if(os.contains("windows")==true){
                    Process proc = Runtime.getRuntime().exec("cmd /c start "+doc);
                }else{
                    Process proc = Runtime.getRuntime().exec("start "+doc);
                }
            } catch (java.io.IOException e) {
                // unsupported
            }
        }        
    }
    
    //============================= DRAWING ====================================
    
    /** <p>Register a function of a script. (JRuby-Jython)<br />
     * Enregistre une fonction d'un script. (JRuby-Jython)</p>
     * @param oname The display name for your effect. (required)
     * @param ofunction The def function's name to use. (required)
     * @param oversion Your revision.
     * @param odescription A small description to explain your function.
     * @param oauthor Your name or team name.
     */
    public static void aspRegister(Object oname, Object ofunction,
            Object oversion, Object odescription, Object oauthor){
        if(actualPath!=null && actualPath.equals("")==false){
            DrawingScript scr = new DrawingScript(
                    oname.toString(),
                    odescription.toString(),
                    oversion.toString(),
                    oauthor.toString(),
                    actualPath, 
                    ofunction.toString());
            sobjectList.add(scr);
        }
    }
    
    public static void add_M_To(int x, int y){
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        ReStart m = new ReStart(x+1000, y+1000, x+1000, y+1000);
        lay.getShapesList().addShape(m); lay.addRemember(m);
        Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
        smallboxforfansub.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_N_To(int x, int y){
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();        
        Move m = new Move(x+1000, y+1000, x+1000, y+1000);
        lay.getShapesList().addShape(m); lay.addRemember(m);
        Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
        smallboxforfansub.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_L_To(int x, int y){        
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        Line l = new Line(point.x, point.y, x+1000, y+1000);
        lay.getShapesList().addShape(l); lay.addRemember(l);
        Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
        smallboxforfansub.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_B_To(int x, int y){        
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        Bezier b = new Bezier(point.x, point.y, x+1000, y+1000);
        lay.getShapesList().addShape(b); lay.addRemember(b);
        ControlPoint cp1 = b.getControl1();
        lay.getShapesList().addShape(cp1); lay.addRemember(cp1);
        ControlPoint cp2 = b.getControl2();
        lay.getShapesList().addShape(cp2); lay.addRemember(cp2);
        Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
        smallboxforfansub.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_B_To(int x, int y, int cpx1, int cpy1, int cpx2, int cpy2){        
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        Bezier b = new Bezier(point.x, point.y, cpx1+1000, cpy1+1000, cpx2+1000, cpy2+1000, x+1000, y+1000);
        lay.getShapesList().addShape(b); lay.addRemember(b);
        ControlPoint cp1 = b.getControl1();
        lay.getShapesList().addShape(cp1); lay.addRemember(cp1);
        ControlPoint cp2 = b.getControl2();
        lay.getShapesList().addShape(cp2); lay.addRemember(cp2);
        Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
        smallboxforfansub.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_S_To(int x, int y){
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        BSpline bs = new BSpline(point.x, point.y);
        lay.getShapesList().addShape(bs); lay.addRemember(bs);
        Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
        smallboxforfansub.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_S_To(int x, int y, int[] cpx, int[] cpy){
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        java.awt.Point point;
        try{
            point = lay.getShapesList().getLastPoint().getLastPoint();
        }catch(Exception e){
            point = lay.getShapesList().getLastShape().getLastPoint();
        }
        Point p = new Point(x+1000, y+1000);
        lay.getShapesList().addShape(p); lay.addRemember(p);
        BSpline bs = new BSpline(point.x, point.y);
        lay.getShapesList().addShape(bs); lay.addRemember(bs);
        for(int i=0; i<cpx.length; i++){
            try{
                bs.addPoint(cpx[i], cpy[i]);
            }catch(Exception e){}
        }
        Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
        smallboxforfansub.drawing.DrawingPanel.setAssCommands();
    }
    
    public static void add_P_To(int x, int y){
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        IShape s = lay.getShapesList().getLastShape();
        if(s instanceof BSpline){
            BSpline bs = (BSpline)s;
            bs.setNextPoint(x+1000, y+1000);
            Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
            sh.updateGeneralPath(lay.getGeneralPath());
            sh.updateShapesList(lay.getShapesList());
            lay.setFirstPoint(lay.getLastPoint());
            smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
            smallboxforfansub.drawing.DrawingPanel.setAssCommands();
        }        
    }
    
    public static void add_C(){
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        IShape s = lay.getShapesList().getLastShape();
        if(s instanceof BSpline){
            BSpline bs = (BSpline)s;
            if(bs.isNextExist()==false){
                bs.setClosed(true);
            }
            Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
            sh.updateGeneralPath(lay.getGeneralPath());
            sh.updateShapesList(lay.getShapesList());
            lay.setFirstPoint(lay.getLastPoint());
            smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
            smallboxforfansub.drawing.DrawingPanel.setAssCommands();
        }
    }
    
    public static int createLayer(String name, int r, int g, int b){
        return smallboxforfansub.drawing.DrawingPanel.createLayer(name, r, g, b);
    }
    
    public static boolean changeLayer(int id){
        return smallboxforfansub.drawing.DrawingPanel.changeLayer(id);
    }
    
    public static void setLayerName(String name){
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        lay.setName(name);
        smallboxforfansub.drawing.DrawingPanel.updateLayerList();
    }
    
    public static void setLayerColor(int r, int g, int b){
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        try{
            lay.setColor(new Color(r,g,b));
        }catch(Exception e){
            lay.setColor(Color.green);
        }
        smallboxforfansub.drawing.DrawingPanel.updateLayerList();
    }
    
    public static void addASSCommands(String asscommands){
        smallboxforfansub.drawing.DrawingPanel.shapesFromCommands(asscommands, null, 0, 0, null, 0);
        Layer lay = smallboxforfansub.drawing.DrawingPanel.getCurrentLayer();
        Sheet sh = smallboxforfansub.drawing.DrawingPanel.getSheet();
        sh.updateGeneralPath(lay.getGeneralPath());
        sh.updateShapesList(lay.getShapesList());
        lay.setFirstPoint(lay.getLastPoint());
        smallboxforfansub.drawing.DrawingPanel.updateRemember(lay);
        smallboxforfansub.drawing.DrawingPanel.setAssCommands();
    }
    
    public List<Object> getObjectsList(){
        return sobjectList;
    }
    
}
