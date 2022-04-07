package pierre.zachary.modele;


import java.util.Objects;

public class Pions {

    private final int type;

    public Pions(int type){
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pions pions = (Pions) o;
        return type == pions.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
