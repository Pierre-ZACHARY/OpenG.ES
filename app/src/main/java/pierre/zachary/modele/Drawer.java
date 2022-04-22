package pierre.zachary.modele;

import java.util.List;
import java.util.function.Function;

public interface Drawer {
    void drawGrid(Grid g);
    void drawNext(List<Pions> pionsList);
    void gameOver(Grid g);

    void addCallbackAction(Function<String, String> callback);
}
