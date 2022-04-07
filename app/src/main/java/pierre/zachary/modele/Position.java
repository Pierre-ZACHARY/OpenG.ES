package pierre.zachary.modele;

import java.util.Comparator;
import java.util.Objects;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

class PositionComparator implements Comparator<Position>{

    @Override
    public int compare(Position position, Position t1) {
        return Integer.compare(position.getX()+position.getY(), t1.getY()+t1.getX());
    }
}