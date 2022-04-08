package pierre.zachary.modele;

import java.util.List;

public interface Drawer {
    void drawGrid(Grid g);
    void drawNext(List<Pions> pionsList);
    void gameOver(Grid g);
}
