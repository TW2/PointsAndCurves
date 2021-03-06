/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smallboxforfansub.drawing.lib;

import java.awt.Rectangle;

/**
 * Cette classe "point" sert à relier les lignes de type "ligne" ou "bézier".
 * Le "point" sera dessiné par un carré sur le dessin.
 * @author The Wingate 2940
 */
public class Point extends AShape implements Cloneable {

    //Un "point" est défini par un rectangle sur le dessin et virtuellement;
    //ce rectangle sert de conteneur de coordonnées.
    //Les coordonnées du centre du rectangle étant les coordonnées réelles.
    private Rectangle rect = new Rectangle();
    private Thickness thickness = Thickness.Big;
    private boolean selected = false;

    /** Création d'un "point". */
    public Point(){
        // nothing
    }

    /** Création d'un "point" avec coordonnées. */
    public Point(int x, int y){
        rect.setRect(
                x-thickness.getThickness()/2,
                y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        origin = new java.awt.Point(x, y);
        last = new java.awt.Point(x, y);
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
        rect.setRect(
                origin.x-thickness.getThickness()/2,
                origin.y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
    }

    /** Donne la commande ASS spécifique à la "ligne". */
    @Override
    public String getCommand() {
        return "";
    }

    /** Renvoie <b>true</b> si la coordonnée est concernée par un changement. */
    public boolean isPointisinRectangle(java.awt.Point p){
        return rect.contains(p);
    }

    /** Met à jour la position du point concerné. */
    public void updatePointPosition(java.awt.Point p){
        int x = (int)p.getX();
        int y = (int)p.getY();
        rect.setRect(
                x-thickness.getThickness()/2,
                y-thickness.getThickness()/2,
                thickness.getThickness(),
                thickness.getThickness());
        origin = p;
        last = p;
    }
    
    /** Marque l'élément "point" comme 'sélectionné'. */
    public void setSelected(boolean b){
        selected = b;
    }

    /** Obtient le status de la sélection. */
    public boolean isSelected() {
        return selected;
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
    
    public boolean hasCoordinates(int x, int y){
        if(x == last.x && y == last.y){
            return true;
        }
        return false;
    }
    
    public boolean hasCoordinates(java.awt.Point p){
        if(p.x == last.x && p.y == last.y){
            return true;
        }
        return false;
    }
}
