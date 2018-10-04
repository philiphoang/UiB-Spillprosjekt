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

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * Represent an accumulated amount of points.
 */
public class Score implements Comparable<Score> {
	
	private long score;
	private String name;
	
	public Score(long score, String name){
		this.score = score;
		this.name = name;
	}
	
	public Score(long score){
		this(score, "Anonymous");
	}
	
	public Score(String name){
		this(0, name);
	}
	
	public Score(){
		this(0, "Anonymous");
	}
	
	/**
	 * 
	 * @param handle FileHandle for the file the scores are saved in
	 * @return The scores in an array, values will be in descending order.
	 */
	public static Score[] createFromJson(FileHandle handle) {
		Objects.requireNonNull(handle, "file handle cannot be null");
		return new Gson().fromJson(handle.reader(), Score[].class);
	}
	
	/**
	 * 
	 * @param handle FileHandle for the file we want to save the scores
	 * @param scores The scores to be saved, this array will be sorted in descending order,
	 * and we will only write the 10 highest values to the file.
	 */
	public static void writeToJson(FileHandle handle, Score[] scores) {
		Objects.requireNonNull(handle, "file handle cannot be null");
		Arrays.sort(scores, Collections.reverseOrder());
		
		Score[] highscore;
		if(scores.length > 10)
			highscore = Arrays.copyOfRange(scores, 0, 10);
		else
			highscore = scores;
		Gson gson = new GsonBuilder().create();
		handle.writeString(gson.toJson(highscore), false);
	}
	
	public void addToScore(long score) {
		if(score < 0)
			throw new IllegalArgumentException("Score can not be negative: " + score);
		this.score += score;
	}

	/**
	 *
	 * Compares the two scores.
	 */
	@Override
	public int compareTo(Score o) {
		return (int) Math.signum((score - o.getScore()));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Score other = (Score) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (score != other.score)
			return false;
		return true;
	}

	public long getScore(){
		return this.score;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (score ^ (score >>> 32));
		return result;
	}
	
	/**
	 * Sets the new score. The score cannot be less than zero.
	 */
	public void setScore(long score){
		if(score < 0)
			throw new IllegalArgumentException("Score can not be negative: " + score);
		this.score = score;
	}
	
	public void setName(String name){
		this.name = name;
	}
}
