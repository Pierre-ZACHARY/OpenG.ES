package pierre.zachary.modele;

import pierre.zachary.modele.exception.NoPossiblePath;
import pierre.zachary.modele.exception.PionsNotInGrid;
import pierre.zachary.modele.exception.TargetNotEmpty;

public class Facade {

    private Grid g;
    private final Drawer drawer;
    private final Score score;

    public Facade(Score s, Drawer d){
        this.score = s;
        this.drawer = d;
    }

    public Grid Level9x9(){
        g = new Grid(0, score, drawer);
        return g;
    }

    public Grid Level7x7(){
        g = new Grid(1, score, drawer);
        return g;
    }

    public void moove(Position from, Position to) throws TargetNotEmpty, NoPossiblePath {
        g.moove(from.getX(), from.getY(), to.getX(), to.getY());
    }

    public void moove(Pions from, Position to) throws TargetNotEmpty, NoPossiblePath, PionsNotInGrid {
        Position p  = g.getPosition(from);
        if(p == null){
            throw new PionsNotInGrid();
        }
        moove(p, to);
    }

}
