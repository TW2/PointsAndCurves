/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.svg;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.AttributesImpl;
import smallboxforfansub.drawing.adf.LayerContent;

/**
 *
 * @author The Wingate 2940
 */
public class XmlVectorWriter {
    
    // Storage of FxObjects
    VectorObject vo = new VectorObject();

    /** <p>Create a new XmlPresetWriter.<br />
     * Crée un nouveau XmlPresetWriter.</p> */
    public XmlVectorWriter(){

    }
    
    /** <p>Container of FxObjects.<br />Conteneur de FxObject.</p> */
    public class VectorObjectSource extends org.xml.sax.InputSource{
        
        // Storage of FxObjects
        VectorObject vo = new VectorObject();

        /** <p>Create a new XmlPresetSource.<br />
         * Crée un nouveau XmlPresetSource.</p> */
        public VectorObjectSource(VectorObject vo){
            super();
            this.vo = vo;

        }
        
        /** <p>Get a list of FxObject.<br />
         * Obtient une liste de FxObject.</p> */
        public VectorObject getVectorObject(){
            return vo;
        }
    }
    
    /** <p>The way to read an XML of XmlPresets (XFX).<br />
     * Comment lire un XML de XmlPresets (XFX).</p> */
    public class VectorObjectReader implements org.xml.sax.XMLReader{

        private ContentHandler chandler;
        private AttributesImpl attributes = new AttributesImpl();
        private Map<String,Boolean> features = new HashMap<>();
        private Map<String,Object> properties = new HashMap<>();
        private EntityResolver resolver;
        private DTDHandler dhandler;
        private ErrorHandler ehandler;
        
        @Override
        public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return features.get(name).booleanValue();
        }

        @Override
        public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
            try{
                features.put(name, value);
            }catch(Exception ex){
            }            
        }

        @Override
        public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return properties.get(name);
        }

        @Override
        public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
            try{
                properties.put(name, value);
            }catch(Exception ex){
            }  
        }

        @Override
        public void setEntityResolver(EntityResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public EntityResolver getEntityResolver() {
            return resolver;
        }

        @Override
        public void setDTDHandler(DTDHandler handler) {
            this.dhandler = handler;
        }

        @Override
        public DTDHandler getDTDHandler() {
            return dhandler;
        }

        @Override
        public void setContentHandler(ContentHandler handler) {
            this.chandler = handler;
        }

        @Override
        public ContentHandler getContentHandler() {
            return chandler;
        }

        @Override
        public void setErrorHandler(ErrorHandler handler) {
            this.ehandler = handler;
        }

        @Override
        public ErrorHandler getErrorHandler() {
            return ehandler;
        }

        @Override
        public void parse(InputSource input) throws IOException, SAXException {

            if(!(input instanceof VectorObjectSource)){
                throw new SAXException("The object isn't a VectorObjectSource");
            }
            if(chandler == null){
                throw new SAXException("ContentHandler not defined");
            }

            VectorObjectSource source = (VectorObjectSource)input;
            VectorObject vo = source.getVectorObject();

            // Main element - beginning
            chandler.startDocument();
            attributes.addAttribute("", "", "width", "width", "1000");
            attributes.addAttribute("", "", "height", "height", "1000");
            attributes.addAttribute("", "", "viewbox", "viewbox", "0 0 1000 1000");
            attributes.addAttribute("", "", "xmlns", "xmlns", "http://www.w3.org/2000/svg");
            attributes.addAttribute("", "", "version", "version", "1.2");
            attributes.addAttribute("", "", "baseProfile", "baseProfile", "tiny");
            chandler.startElement("", "svg", "svg", attributes);
            attributes.clear();

            // ParticleOjects element
            for(LayerContent content : vo.getLayers()){
                
                attributes.addAttribute("", "", "d", "d", AssToSvg(content.getAssCommands()));
                attributes.addAttribute("", "", "fill", "fill", "red");
                attributes.addAttribute("", "", "stroke", "stroke", "blue");
                attributes.addAttribute("", "", "stroke-width", "stroke-width", "3");
                chandler.startElement("", "path", "path", attributes);
                char[] path = "".toCharArray();
                chandler.characters(path,0,path.length);
                chandler.endElement("", "path", "path");
                attributes.clear();
            }

            // Main element - end
            chandler.endElement("", "svg", "svg");
            chandler.endDocument();

        }

        @Override
        public void parse(String systemId) throws IOException, SAXException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    /** <p>Create a new XML file.<br />Crée un nouveau fichier XML.</p> */
    public boolean createVectorObject(String path){
        org.xml.sax.XMLReader pread = new VectorObjectReader();
        InputSource psource = new VectorObjectSource(vo);
        Source source = new SAXSource(pread, psource);

        File file = new File(path);
        Result resultat = new StreamResult(file);
        
        try {
            TransformerFactory fabrique = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = fabrique.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, resultat);
        } catch (TransformerConfigurationException ex) {
            return false;
        } catch (TransformerException ex) {
            return false;
        }
        return true;
    }
    
    /** <p>Set a new list of FxObject.<br />
     * Définit une nouvelle liste FxObject.</p> */
    public void setVectorObject(VectorObject vo){
        this.vo = vo;
    }
    
    private String AssToSvg(String asscommands){
        String svgcommands = "";
        Pattern pat = Pattern.compile("([a-z]*)\\s*(-*\\d*)\\s*(-*\\d*)\\s*(-*\\d*)\\s*(-*\\d*)\\s*(-*\\d*)\\s*(-*\\d*)\\s*");
        Matcher mat = pat.matcher(asscommands);
        while(mat.find()){
            String param = mat.group(1);
            String xa = mat.group(2);
            String ya = mat.group(3);
            String xb = mat.group(4);
            String yb = mat.group(5);
            String xc = mat.group(6);
            String yc = mat.group(7);
            
            if(param.equalsIgnoreCase("m")){
                svgcommands += "M "+xa+","+ya+" ";
            }else if(param.equalsIgnoreCase("n")){
                svgcommands += "M "+xa+","+ya+" ";
            }else if(param.equalsIgnoreCase("l")){
                svgcommands += "L "+xa+","+ya+" ";
            }else if(param.equalsIgnoreCase("b")){
                svgcommands += "C "+xa+","+ya+" "+xb+","+yb+" "+xc+","+yc+" ";
            }
        }
        return svgcommands;
    }
    
}
