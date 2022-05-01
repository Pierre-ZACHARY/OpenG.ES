package pierre.zachary.modele;


import java.util.Objects;

public class Pions {

    private static int global_id = 0;

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    private int id;


    private final int type;

    public Pions(int type){
        this.id = global_id;
        global_id += 1;
        this.type = type;
    }

    public boolean sameType(Pions p){
        return p!=null && type == p.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pions pions = (Pions) o;
        return id == pions.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
