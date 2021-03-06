/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.drawing.lib;

import java.awt.Rectangle;

/**
 * Cette classe définit un élément de type "ligne" comme étant un dérivé de
 * "forme" en implementant les méthodes demandées par "forme". Une "ligne" se
 * compose de la coordonnée 'origine' donnant la point de début et de la
 * coordonnée 'dernier' donnant le bout de la "ligne".
 * @author The Wingate 2940
 */
public class Line extends AShape implements Cloneable {

    //Les variables suivantes avertissent d'un changement de position en cours
    //sur le point 'origine' (ochange) ou le point dernier (lchange).
    boolean oChange = false; boolean lChange = false;
    private Thickness thickness = Thickness.Big;

    /** Création d'une "ligne". */
    public Line(){
        //nothing
    }

    /** Création d'une "ligne" avec coordonnées. */
    public Line(int originX1, int originY1, int lastX2, int lastY2){
        origin = new java.awt.Point(originX1,originY1);
        last = new java.awt.Point(lastX2, lastY2);
    }

    public enum Thickness{
        Big(10),Large(8),Medium(6),Small(4);

        private int thick;

        Thickness(int thick){
            this.thick = thick;
        }

        public int getThickness(){
            return thick;
        }
    }

    public void updateThickness(Thickness thickness){
        this.thickness = thickness;
    }

    /** Donne la commande ASS spécifique à la "ligne". */
    @Override
    public String getCommand() {
        return "l "+(int)last.getX()+" "+(int)last.getY();
    }

    /** Renvoie <b>true</b> si une extrémité de la "ligne" est concernée par
     * un changement et définit lequel dans l'élément "ligne". */
    public boolean isPointisNear(java.awt.Point p){
        int xp = (int)p.getX(); int yp = (int)p.getY();
        Rectangle rect = new Rectangle(
                xp-thickness.getThickness()/2,
                yp-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        if(rect.contains(origin)){oChange=true; return true;}
        if(rect.contains(last)){lChange=true; return true;}
        return false;
    }

    /** Met à jour la position du point concerné. */
    public void updatePointPosition(java.awt.Point p){
        if(oChange==true){origin = p;}
        if(lChange==true){last = p;}
    }

    /** Retourne à la configuration par défaut. */
    public void updatehasEnded(){
        oChange=false;
        lChange=false;
    }
    
    @Override
    public Object clone() {
        Object o = null;
    	try {
      		// On récupère l'instance à renvoyer par l'appel de la 
      		// méthode super.clone()
      		o = super.clone();
    	} catch(CloneNotSupportedException cnse) {
      		// Ne devrait jamais arriver car nous implémentons 
      		// l'interface Cloneable
      		cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;  	
    }
}
