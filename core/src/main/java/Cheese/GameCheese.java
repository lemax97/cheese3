package Cheese;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameCheese extends Game {
    @Override
    public void create() {
        CheeseMenu cheeseMenu = new CheeseMenu(this);
        setScreen(cheeseMenu);
    }
}