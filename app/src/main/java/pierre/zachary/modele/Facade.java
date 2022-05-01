package pierre.zachary.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import pierre.zachary.modele.exception.NoPossiblePath;
import pierre.zachary.modele.exception.PionsNotInGrid;
import pierre.zachary.modele.exception.TargetNotEmpty;

public class Facade implements Drawer, Score{

    private static Facade instance;
    public static Facade getInstance(){
        if(instance==null) instance=new Facade();
        return instance;
    }


    private Grid level1;
    private Grid level2;
    private HashMap<Grid, HashSet<Drawer>> drawerPerGrid;
    private HashMap<Grid, HashSet<Score>> scorePerGrid;
    private Grid currentGrid;

    private Facade(){
        reset();
    }
    
    public void reset(){
        drawerPerGrid = new HashMap<>();
        scorePerGrid = new HashMap<>();

        level1 = new Grid(0, this, this);
        drawerPerGrid.put(level1, new HashSet<>());
        scorePerGrid.put(level1, new HashSet<>());
        level1.spawnNext();


        level2 = new Grid(1, this, this);
        drawerPerGrid.put(level2, new HashSet<>());
        scorePerGrid.put(level2, new HashSet<>());
        level2.spawnNext();
    }

    public Grid Level9x9(Score s, Drawer d){
        currentGrid = level1;
        drawerPerGrid.get(level1).add(d);
        scorePerGrid.get(level1).add(s);
        return currentGrid;
    }

    public Grid Level7x7(Score s, Drawer d){
        currentGrid = level2;
        drawerPerGrid.get(level2).add(d);
        scorePerGrid.get(level2).add(s);
        return currentGrid;
    }

    public void moove(Position from, Position to) throws TargetNotEmpty, NoPossiblePath {
        currentGrid.moove(from.getX(), from.getY(), to.getX(), to.getY());
    }

    public void moove(Pions from, Position to) throws TargetNotEmpty, NoPossiblePath, PionsNotInGrid {
        Position p  = currentGrid.getPosition(from);
        if(p == null){
            throw new PionsNotInGrid();
        }
        moove(p, to);
    }

    @Override
    public void drawGrid(Grid g) {
        if(drawerPerGrid.containsKey(currentGrid)){
            for(Drawer d : drawerPerGrid.get(currentGrid)){
                d.drawGrid(g);
            }
        }
    }

    public void askRedraw(Drawer d){
        d.drawGrid(currentGrid);
        d.drawNext(currentGrid.next);
    }

    @Override
    public void drawNext(List<Pions> pionsList) {
        if(drawerPerGrid.containsKey(currentGrid)){
            for(Drawer d : drawerPerGrid.get(currentGrid)){
                d.drawNext(pionsList);
            }
        }
    }

    @Override
    public void gameOver(Grid g) {
        if(drawerPerGrid.containsKey(currentGrid)){
            for(Drawer d : drawerPerGrid.get(currentGrid)){
                d.gameOver(g);
            }
        }
    }

    @Override
    public void addCallbackAction(Function<String, String> callback) {
        if(drawerPerGrid.containsKey(currentGrid)){
            for(Drawer d : drawerPerGrid.get(currentGrid)){
                d.addCallbackAction(callback);
            }
        }

    }

    @Override
    public void notifyScoreChanged(int addedScore) {
        if(scorePerGrid.containsKey(currentGrid)){
            for(Score s : scorePerGrid.get(currentGrid)){
                s.notifyScoreChanged(addedScore);
            }
        }

    }
}
