package Cheese;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.controllers.*;

public class CheeseMenu extends BaseScreen {

    public CheeseMenu(Game g) {
        super(g);
    }

    public void create() {

        BaseActor background = new BaseActor();
        background.setTexture(new Texture("tiles-menu.jpg"));

        BaseActor titleText = new BaseActor();
        titleText.setTexture(new Texture("cheese-please.png"));

        titleText.setPosition(20,100);
        uiStage.addActor(titleText);

        BitmapFont bitmapFont = new BitmapFont();
        String text = "Press S to start, M for main menu ";
        LabelStyle labelStyle = new LabelStyle(bitmapFont, Color.YELLOW);
        Label instructions = new Label(text, labelStyle);
        instructions.setFontScale(2);
        instructions.setPosition(100, 50);
        //repeating color pulse effect
        instructions.addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.color(new Color(1,1,0,1), 0.5f),
                                Actions.delay(0.5f),
                                Actions.color(new Color(0.5f, 0.5f, 0, 1), 0.5f)
                        )
                )
        );
        uiStage.addActor(instructions);
    }

    @Override
    public void update(float dt) {

    }

    //InputProcessor methods for handling discrete input
    public boolean keyDown(int keycode){
        if (keycode == Keys.S)
            game.setScreen(new CheeseLevel(game));
        return false;
    }

}
