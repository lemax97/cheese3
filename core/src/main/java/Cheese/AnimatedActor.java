package Cheese;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class AnimatedActor extends BaseActor {
    public float elapsedTime;
    public Animation animationActor;

    public AnimatedActor(){
        super();
        elapsedTime = 0;
    }

    public void setAnimation(Animation animation){
        Texture texture = ((TextureRegion)animation.getKeyFrame(0)).getTexture();
        setTexture(texture);
        animationActor = animation;
    }

    public void act(float dt){
        super.act(dt);
        elapsedTime += dt;
        if (velocityX != 0 || velocityY !=0) {
            setRotation(MathUtils.atan2(velocityY,velocityX) * MathUtils.radiansToDegrees);
        }
    }

    public void draw(Batch batch, float parentAlpha){
        region.setRegion((TextureRegion) animationActor.getKeyFrame(elapsedTime));
        super.draw(batch, parentAlpha);
    }
}
