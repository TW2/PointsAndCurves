/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smallboxforfansub.drawing.lib;

import java.awt.Point;

/**
 *
 * @author The Wingate 2940
 */
public abstract class AShape implements IShape {
    
    //Les points de coordonnées 'origine' et 'dernier'.
    protected java.awt.Point origin, last;
    //marked sert à signalé un changement de position de façon globale.
    protected boolean marked = false;
    
    protected boolean inSelection = false, firstInSelection = false;
    
    //Sélection des points pour ajouter ou enlever des points
    protected boolean addlist = false, removelist = false;
    
    @Override
    public void setOriginPoint(int x, int y) {
        origin = new java.awt.Point(x,y);
    }

    @Override
    public Point getOriginPoint() {
        return origin;
    }

    @Override
    public void setLastPoint(int x, int y) {
        last = new java.awt.Point(x, y);
    }

    @Override
    public Point getLastPoint() {
        return last;
    }

    @Override
    public void setMarked(boolean b) {
        marked = b;
    }

    @Override
    public boolean getMarked() {
        return marked;
    }

    @Override
    public void setInSelection(boolean b){
        inSelection = b;
    }
    
    @Override
    public boolean isInSelection(){
        return inSelection;
    }

    @Override
    public void setFirstInSelection(boolean b){
        firstInSelection = b;
    }
    
    @Override
    public boolean isFirstInSelection(){
        return firstInSelection;
    }

    @Override
    public void setInAddList(boolean b){
        addlist = b;
    }    
    
    @Override
    public boolean isInAddList(){
        return addlist;
    }
    
    @Override
    public void setInRemoveList(boolean b){
        removelist = b;
    }    
    
    @Override
    public boolean isInRemoveList(){
        return removelist;
    }
    
}
