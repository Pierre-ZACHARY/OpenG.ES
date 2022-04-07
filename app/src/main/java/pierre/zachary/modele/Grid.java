package pierre.zachary.modele;

import androidx.recyclerview.widget.SortedList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import pierre.zachary.modele.exception.NoPossiblePath;
import pierre.zachary.modele.exception.TargetNotEmpty;

public class Grid {

    private final int type;
    private final Score score;
    private final Drawer drawer;

    public int getGridSize() {
        return gridSize;
    }

    private int gridSize = 9;
    private int pionsAligne = 5;
    private ArrayList<Pions> grid;
    private ArrayList<Pions> next;

    public Grid(int type, Score s, Drawer d){
        this.drawer = d;
        this.score = s;
        this.type = type;
        switch(type){
            case 0:
                this.gridSize = 9;
                this.pionsAligne = 5;
                break;
            case 1:
                this.gridSize = 7;
                this.pionsAligne = 4;
        }
        this.grid = new ArrayList<Pions>(this.gridSize*this.gridSize);
        for(int i = 0; i<getGridSize()*getGridSize();i++){
            this.grid.add(null);
        }
        this.next = new ArrayList<>(3);
        for(int i = 0; i<3;i++){
            this.next.add(null);
        }
        this.populateNext();
    }

    private Pions getPions(int x, int y){
        return this.grid.get(x*this.gridSize+y);
    }

    private void setPions(Pions p, int x, int y){
        this.grid.set(x*this.gridSize+y, p);
    }

    private Pions randomPions(){
        int rand = new Random().nextInt(8);
        return new Pions(rand);
    }

    private void populateNext(){
        for(int i = 0; i<3; i++){
            this.next.set(i, this.randomPions());
        }
        this.drawer.drawNext(this.next);
    }

    private List<Position> getVide(){
        List<Position> res = new ArrayList<>();
        int x = 0;
        int y = 0;
        for(Pions p : this.grid){
            if(p == null){
                res.add(new Position(x, y));
            }
            x++;
            if(x>=this.gridSize){
                x=0;
                y++;
            }
        }
        return res;
    }

    public void spawnNext(){
        // on fait spawn les 3 pions à 3 endroits vide de la grille puis on re populate
        List<Position> vide = this.getVide();
        for(int i = 0; i<3; i++){
            int index = new Random().nextInt(vide.size());
            this.setPions(this.next.get(i), vide.get(index));
            this.checkAlignement(vide.get(index));
        }
        this.drawer.drawGrid(this);
        this.populateNext();
        this.drawer.drawNext(this.next);
    }

    public void removeAllPositions(List<Position> listPos){
        listPos.sort(new PositionComparator());
        for(Position p : listPos){
            this.setPions(null, p);
            this.drawer.drawGrid(this);
        }
    }

    public void checkAlignement(Position p){
        Pions base = this.getPions(p.getX(), p.getY());
        List<Position> alignedX = new ArrayList<>();
        List<Position> alignedY = new ArrayList<>();
        int[][] directionsX = {{-1,0}, {1,0}};
        int[][] directionsY = {{0,-1}, {0,1}};

        for(int[] dir : directionsX){
            for(int i = 0; i<this.gridSize; i++){
                Position pion_pos = new Position(p.getX()+dir[0]*i, p.getY());
                Pions pions = this.getPions(pion_pos);
                if(pions != base){
                    break;
                }
                alignedX.add(pion_pos);
            }
        }
        for(int[] dir : directionsY){
            for(int i = 0; i<this.gridSize; i++){
                Position pion_pos = new Position(p.getX(), p.getY()+dir[1]*i);
                Pions pions = this.getPions(pion_pos);
                if(pions != base){
                    break;
                }
                alignedY.add(pion_pos);
            }
        }
        if(alignedX.size()>=this.pionsAligne){
            this.score.notifyScoreChanged(10+alignedX.size()-this.pionsAligne);
            this.removeAllPositions(alignedX);
        }
        if(alignedY.size()>=this.pionsAligne){
            this.score.notifyScoreChanged(10+alignedY.size()-this.pionsAligne);
            this.removeAllPositions(alignedY);
        }
    }

    private Pions getPions(Position pion_pos) {
        return this.getPions(pion_pos.getX(), pion_pos.getY());
    }

    private void setPions(Pions pions, Position position) {
        this.setPions(pions, position.getX(), position.getY());
    }

    public void moove(int departX, int departY, int targetX, int targetY) throws TargetNotEmpty, NoPossiblePath {
        Pions depart = this.getPions(departX, departY);
        Position lastPos = new Position(departX, departY);
        if(this.getPions(targetX, targetY) != null){
            throw new TargetNotEmpty();
        }

        List<Position> path = this.aStar(new Position(departX, departY), new Position(targetX, targetY));
        for(Position p : path){
            this.setPions(depart, p);
            this.setPions(null, lastPos);
            lastPos = p;
            this.drawer.drawGrid(this);
        }
        this.checkAlignement(path.get(path.size()-1));
    }

    public static class AstarNode{

        private final List<AstarNode> parent;
        public final Position etat;
        private final Position target;
        private Grid grid;
        private int cout;

        public AstarNode(Grid g, Position etat, List<AstarNode> parent, Position target){
            this.etat = etat;
            this.parent = parent;
            this.grid = g;
            this.target = target;
            this.cout = this.getCout();
        }

        public List<Position> getPositions(){
            List<Position> res = new ArrayList<>();
            for(AstarNode p : this.parent){
                res.add(p.etat);
            }
            res.add(this.etat);
            return res;
        }

        private int getCout() {
            int distMan = 0;
            distMan += Math.abs(etat.getX() - target.getX());
            distMan += Math.abs(etat.getY() - target.getY());
            return this.parent.size()+distMan;
        }

        public List<AstarNode> getChilds(){
            ArrayList<AstarNode> res = new ArrayList<>();
            ArrayList<AstarNode> newParents = new ArrayList<>(parent);
            newParents.add(this);

            if(grid.getPions(this.etat.getX()+1, this.etat.getY()) != null){ res.add(new AstarNode(this.grid, new Position(this.etat.getX() + 1, this.etat.getY()), newParents, this.target)); }
            if(grid.getPions(this.etat.getX()-1, this.etat.getY()) != null){ res.add(new AstarNode(this.grid, new Position(this.etat.getX() - 1, this.etat.getY()), newParents, this.target)); }
            if(grid.getPions(this.etat.getX(), this.etat.getY()+1) != null){ res.add(new AstarNode(this.grid, new Position(this.etat.getX(), this.etat.getY() + 1), newParents, this.target)); }
            if(grid.getPions(this.etat.getX(), this.etat.getY()-1) != null){ res.add(new AstarNode(this.grid, new Position(this.etat.getX(), this.etat.getY() - 1), newParents, this.target)); }
            return res;
        }
    }
    public static class AstarNodeComparator implements Comparator<AstarNode>{
        @Override
        public int compare(AstarNode first, AstarNode second) {
            return Integer.compare(first.cout, second.cout);
        }
    }

    private List<Position> aStar(Position from, Position to) throws NoPossiblePath {
        AstarNode node = new AstarNode(this, from, new ArrayList<>(), to);
        ArrayList<AstarNode> file = new ArrayList<>();

        while(node.etat != to){
            file.addAll(node.getChilds());
            if(file.size() == 0){
                throw new NoPossiblePath();
            }
            file.sort(new AstarNodeComparator());
            node = file.remove(0);
        }

        return node.getPositions();
    }


}
