package Cheese;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.controllers.*;

public class CheeseLevel extends BaseScreen {

    private AnimatedActor mousey;
    private BaseActor cheese;
    private BaseActor floor;
    private BaseActor winText;
    private boolean win;
    private float timeElapsed;
    private Label timeLabel;

    //game world dimensions
    final int mapWidth = 800;
    final int mapHeight = 800;

    public CheeseLevel(Game game) {
        super(game);
    }

    public void create(){
        timeElapsed = 0;

        floor = new BaseActor();
        floor.setTexture(new Texture("tiles-800-800.jpg"));
        floor.setPosition(0,0);
        mainStage.addActor(floor);

        cheese = new BaseActor();
        cheese.setTexture(new Texture("cheese.png"));
        cheese.setPosition(400, 300);
        cheese.setOrigin(cheese.getWidth()/2, cheese.getHeight()/2);
        mainStage.addActor(cheese);

        mousey = new AnimatedActor();
        
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            String fileName = "mouse" + i + ".png";
            Texture texture = new Texture(fileName);
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            frames[i] = new TextureRegion(texture);
        }
        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        Animation animation = new Animation(0.1f, framesArray, PlayMode.LOOP_PINGPONG);


        mousey.setAnimation(animation);
        mousey.setOrigin(mousey.getWidth()/2, mousey.getHeight()/2);
        mousey.setPosition(20,30);
        mainStage.addActor(mousey);

        winText = new BaseActor();
        winText.setTexture(new Texture("you-win.png"));
        winText.setPosition(170, 60);
        winText.setVisible(false);
        uiStage.addActor(winText);         

        BitmapFont font = new BitmapFont();
        String text = "Time: 0";
        LabelStyle style = new LabelStyle(font, Color.NAVY);
        timeLabel = new Label(text, style);
        timeLabel.setFontScale(2);
        timeLabel.setPosition(500, 440);//sets bottom left (baseline) corner?
        uiStage.addActor(timeLabel);

        win = false;
    }

    @Override
    public void update(float delta) {
        //process input
        mousey.velocityX = 0;
        mousey.velocityY = 0;

        if (Gdx.input.isKeyPressed(Keys.LEFT))
            mousey.velocityX -= 100;
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            mousey.velocityX += 100;
        if (Gdx.input.isKeyPressed(Keys.UP))
            mousey.velocityY += 100;
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            mousey.velocityY -= 100;

        //bound mousey to rectangle defined by mapWidth, mapHeight
        mousey.setX(MathUtils.clamp(mousey.getX(), 0, mapWidth - mousey.getWidth()));
        mousey.setY(MathUtils.clamp(mousey.getY(), 0, mapHeight - mousey.getHeight()));

        //check win condition: mousey must be overlapping cheese
        Rectangle cheeseRectangle = cheese.getBoundingRectangle();
        Rectangle mouseyRectangle = mousey.getBoundingRectangle();
        if (!win && cheeseRectangle.contains(mouseyRectangle)) {
            win = true;

            cheese.addAction(Actions.parallel(
                    Actions.alpha(1), //set transparency value
                    Actions.rotateBy(360, 1), //rotation amount, duration
                    Actions.scaleTo(0,0,1), //x amount, y amount, duration
                    Actions.fadeOut(1) // duration of fade out
                    )
            );

            winText.addAction(Actions.sequence(
                    Actions.alpha(0), //set transparency value
                    Actions.show(), //set visible to true
                    Actions.fadeIn(2), //duration of fade in
                    Actions.forever(Actions.sequence(
                            //color shade to aproach, duration
                            Actions.color(new Color(1,0,0,1),1),
                            Actions.color(new Color(0,0,1,1),1)
                            )
                    )
                    )
            );

        }

        //Label of elapsed time
        if (!win){
            timeElapsed +=delta;
            timeLabel.setText("Time: " + (int)timeElapsed);
        }

        //camera adjustment
        Camera camera = mainStage.getCamera();

        //center camera on player
        camera.position.set(mousey.getX() + mousey.getOriginX(), mousey.getY() + mousey.getOriginY(), 0);

        //bound camera to layout
        camera.position.x = MathUtils.clamp(camera.position.x, viewWidth/2, mapWidth - viewWidth/2);
        camera.position.y = MathUtils.clamp(camera.position.y, viewHeight/2, mapHeight - viewHeight/2);
        camera.update();

    }

    // Input processor methods for handling discrete input

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.M)
            game.setScreen(new CheeseMenu(game));
        if (keycode==Keys.P)
            togglePaused();
        return false;
    }
}
