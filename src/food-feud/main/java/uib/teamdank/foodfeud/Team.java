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
package uib.teamdank.foodfeud;

/**
 * Represents a team in the game.
 * <p>
 * Each team specifies the following string formats (with
 * the name of the character in the team as the only argument) to
 * locate the appropriate resources:
 * <ul>
 * <li>{@code "%s_body_1"} to {@code "%s_body_5"} (texture atlas regions for body expansion)</li>
 * <li>{@code "%s_body_dead"} (texture atlas region for dead body)</li>
 * <li>{@code "Images/%s_still_animation.json"} (animation file for feet standing still)</li>
 * <li>{@code "Images/%s_walking_animation.json"} (animation file for feet walking)</li>
 * <li>{@code "Images/%s_falling_animation.json"} (animation file for feet falling)</li>
 * </ul> 
 */
public enum Team {
	ALPHA("Rock'n'Rollers", "elvis"),
	BETA("Useless Celebrities", "kim"),
	CHARLIE("Depressed Magicians", "guy1"),
	DELTA("Annoying Teenagers", "guy2");
	
	private static final String[] BODY_EXPANSION_TEXTURES = {
		"%s_body_1", "%s_body_2", "%s_body_3", "%s_body_4", "%s_body_5",
	};
	private static final String BODY_DEAD = "%s_body_dead";
	private static final String STILL_ANIMATION = "Images/%s_still_animation.json";
	private static final String WALKING_ANIMATION = "Images/%s_walking_animation.json";
	private static final String FALLING_ANIMATION = "Images/%s_falling_animation.json";
	
	private final String name;
	private final String characterName;

	private Team(String name, String characterName) {
		this.name = name;
		this.characterName = characterName;
	}

	public String getBodyDead() {
		return String.format(BODY_DEAD, characterName);
	}
	
	public String getBodyExpansion(int index) {
		return String.format(BODY_EXPANSION_TEXTURES[index], characterName);
	}
	
	public int getBodyExpansionCount() {
		return BODY_EXPANSION_TEXTURES.length;
	}
	
	public String getFallingAnimation() {
		return String.format(FALLING_ANIMATION, characterName);
	}

	public String getName() {
		return name;
	}
	
	public String getStillAnimation() {
		return String.format(STILL_ANIMATION, characterName);
	}

	public String getWalkingAnimation() {
		return String.format(WALKING_ANIMATION, characterName);
	}
}
