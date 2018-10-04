/*******************************************************************************
 * Copyright (C) 2017  TeamDank
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package uib.teamdank.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import uib.teamdank.common.util.Animation;

/**
 * Represent an object in a game.
 */
public class GameObject {

	private boolean marked;
	private final Vector2 pos = new Vector2();
    private final Vector2 velocity = new Vector2();
	private Animation animation;
	private float scale;
	private float angle;
	private boolean flipHorizontally;
	private boolean flipVertically;

	/**
	 * Creates a GameObject that spans no area, in the origin (0, 0).
	 */
	public GameObject() {
		this(null);
		
	}

	/**
	 * Creates a GameObject in the origin, that spans the area of the given
	 * {@link TextureRegion}.
	 * 
	 * @param tRegion
	 */
	public GameObject(TextureRegion tRegion) {
		this(0,0, tRegion);
	}

	/**
	 * Creates a GameObject that spans no area, in the position 
	 * ({@link Vector2#x}, {@link Vector2#y}).
	 * 
	 * @param x
	 * @param y
	 */
	public GameObject(float x, float y) {
		this(x, y, null);
	}

	/**
	 * Creates a GameObject in the position ({@link Vector2#x}, {@link Vector2#y}),
	 * that spans the area of the given {@link TextureRegion}.
	 * 
	 * @param x
	 * @param y
	 * @param tRegion
	 */
	public GameObject(float x, float y, TextureRegion tRegion) {
	    this.pos.set(x, y);
	    this.setTexture(tRegion);
	    this.setScale(1);
	    this.setFlipHorizontally(false);
	    this.setFlipVertically(false);
	}

	/**
	 * Set the scaling of the item.
	 * @param s
	 */
	public void setScale(float s){
		this.scale = s;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return Whether the given coordinates are contained within this GameObject.
	 */
	public boolean contains(double x, double y) {
		return x >= pos.x && x <= (pos.x + getWidth()) && y >= pos.y && y <= (pos.y + getHeight());
	}
	
	public boolean contains(GameObject obj) {
		if (contains(obj.getX(), obj.getY())) {
			return true;
		}
		if (contains(obj.getX() + obj.getWidth(), obj.getY())) {
			return true;
		}
		if (contains(obj.getX(), obj.getY() + obj.getHeight())) {
			return true;
		}
		if (contains(obj.getX() + obj.getWidth(), obj.getY() + obj.getHeight())) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return The width of this GameObject.
	 */
	public int getWidth() {
		if (getTexture() == null) {
			return 0;
		}
		return (int) (getTexture().getRegionWidth()*scale);
	}

	/**
	 *
	 * @return The height of this GameObject.
	 */
	public int getHeight() {
		if (getTexture() == null) {
			return 0;
		}
		return (int) (getTexture().getRegionHeight()*scale);
	}

	/**
	 * Mark this object for removal.
	 */
	public void markForRemoval() {
		marked = true;
	}

	/**
	 *
	 * @return Whether or not this object is marked for removal.
	 */
	public boolean isMarkedForRemoval() {
		return marked;
	}

	public Animation getAnimation() {
		return animation;
	}
	
	/**
	 * 
	 * @return The current position of this GameObject.
	 */
	public Vector2 getPosition() {
		return pos;
	}

	/**
	 * @return The current velocity of this GameObject.
	 */
	public Vector2 getVelocity() {
		return velocity;
	}

	/**
	 * @return The rectangular area eventually containing a texture that is the
	 *         image representation of this GameObject.
	 */
	public TextureRegion getTexture() {
		return animation == null ? null : animation.getTexture();
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	/**
	 * Sets this game object's texture to be flipped along the vertical
	 * axis when rendered.
	 */
	public void setFlipHorizontally(boolean flipHorizontally) {
		this.flipHorizontally = flipHorizontally;
	}
	
	public boolean getFlipHorizontally() {
		return flipHorizontally;
	}
	
	/**
	 * Sets this game object's texture to be flipped along the horizontal
	 * axis when rendered.
	 */
	public void setFlipVertically(boolean flipVertically) {
		this.flipVertically = flipVertically;
	}
	
	public boolean getFlipVertically() {
		return flipVertically;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public float getAngle() {
		return angle;
	}
	
    /**
     * Sets the texture of this object
     * @param texture
     */
	public void setTexture(TextureRegion texture){
		if (texture == null) {
			this.animation = null;
		} else {
			this.animation = Animation.createSingleFramed(texture);
		}
    }
	
	/**
	 * Renders this game object. Renders the texture returned
	 * by {@link #getTexture()} by default.
	 */
	public void render(SpriteBatch batch, float delta) {
		renderTexture(batch, delta, getTexture(), 0, 0);
	}
	
	protected void renderTexture(SpriteBatch batch, float delta, TextureRegion texture, float xOffset, float yOffset) {
		renderTexture(batch, delta, texture, getWidth(), getHeight(), xOffset, yOffset);
	}
	
	protected void renderTexture(SpriteBatch batch, float delta, TextureRegion texture, float width, float height, float xOffset, float yOffset) {
		if (texture != null) {
			final Vector2 pos = getPosition();
			final float flipX = getFlipHorizontally() ? -1 : 1;
			final float flipY = getFlipVertically() ? -1 : 1;
			batch.draw(texture, pos.x + xOffset, pos.y + yOffset, width / 2, height / 2, width, height, flipX, flipY, getAngle());
		}
		
	}
	
	/**
	 * Updates this game object. Updates movement
	 * and animation by default.
	 */
	public void update(float delta) {
		updateMovement(delta);
		updateAnimation(delta);
	}
	
	protected void updateMovement(float delta) {
		if (isMovable()) {
			pos.x += (velocity.x * delta);
			pos.y += (velocity.y * delta);
		}
	}
	
	protected void updateAnimation(float delta) {
		if (getAnimation() != null) {
			getAnimation().update(delta);
		}
	}

	/**
	 * 
	 * @return Whether or not this GameObject can move.
	 */
	public boolean isMovable() {
		return true;
	}

	public float getScale() {
		return scale;
	}
	
	/**
	 * @return Whether or not this GameObject can share space with other
	 *         GameObject's.
	 */
	// TODO: Subclasses must remember to override
	public boolean isSolid() {
		return false;
	}
	
	public float getX(){
		return pos.x;
	}
	
	public float getY(){
		return pos.y;
	}
	
	public void setX(float x){
		pos.x = x;
	}
	public void setY(float y){
		pos.y = y;
	}

}
