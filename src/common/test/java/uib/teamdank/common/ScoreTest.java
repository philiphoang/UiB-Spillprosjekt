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
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.util.Random;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.badlogic.gdx.files.FileHandle;

public class ScoreTest {
	@Test
	public void scoreZeroAtStartTest(){
		Score testScore = new Score();
		long score = testScore.getScore();
		long zero = 0;
		assertEquals(score,zero);
	}
	@Test
	public void addToScoreTest(){
		Score testScore = new Score();
		testScore.addToScore(3);
		long score = testScore.getScore();
		long three = 3;
		assertEquals(score,three);
	}
	@Test
	public void setScoreTest(){
		Score testScore = new Score();
		testScore.setScore(5);
		long score = testScore.getScore();
		long three = 5;
		assertEquals(score,three);
	}
	@Test
	public void compareToTest(){
		Score score1 = new Score();
		Score score2 = new Score();
		long stor = 10;
		long liten = 1;
		score1.setScore(stor);
		score2.setScore(liten);
		int compare1 = score1.compareTo(score2);
		assertEquals(compare1, 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void subtractScoreTest(){
		Score score = new Score();
		score.addToScore(-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setNegativeScoreTest(){
		Score score = new Score();
		score.setScore(-1);
	}
	
	@Test
	public void setAndGetNameTest(){
		Score s1 = new Score();
		Score s2 = new Score("test");
		assertEquals("Anonymous", s1.getName());
		assertEquals("test", s2.getName());
		
		s1.setName("test2");
		assertEquals("test2", s1.getName());
	}
	
	@Test
	public void testWritesExpectedJsonToFile() {
		FileHandle mockhandle = mock(FileHandle.class);
		String expectedJson = "[{\"score\":0,\"name\":\"Anonymous\"}]";
		
		ArgumentCaptor<String> jsonCapture = ArgumentCaptor.forClass(String.class);
		
		Score.writeToJson(mockhandle, new Score[]{new Score()});
		
		verify(mockhandle).writeString(jsonCapture.capture(), anyBoolean());
		
		assertEquals(expectedJson, jsonCapture.getValue());
	}
	
	@Test
	public void testCreatesCorrectHighScoresFromFile(){
		FileHandle mockhandle = mock(FileHandle.class);
		String json = "[{\"score\":2,\"name\":\"Anonymous\"},{\"score\":1,\"name\":\"test\"}]";
		
		when(mockhandle.reader()).thenReturn(new StringReader(json));
		
		Score[] scores =  Score.createFromJson(mockhandle);
		
		assertEquals(2, scores.length);
		assertEquals("Anonymous", scores[0].getName());
		assertEquals("test", scores[1].getName());
		assertEquals(2, scores[0].getScore());
		assertEquals(1, scores[1].getScore());
	}
	
	@Test
	public void testWriteOnlyThe10HighestScoresToFile(){
		FileHandle mockhandle = mock(FileHandle.class);
		Score[] scores = new Score[20];
		
		Random r = new Random();
		for(int i = 0; i < scores.length-1; i++){
			scores[i] = new Score(r.nextInt(100));
		}
		scores[scores.length-1] = new Score(1000, "mrks");
		
		ArgumentCaptor<String> jsonCapture = ArgumentCaptor.forClass(String.class);
		Score.writeToJson(mockhandle, scores);
		
		verify(mockhandle).writeString(jsonCapture.capture(), anyBoolean());
		when(mockhandle.reader()).thenReturn(new StringReader(jsonCapture.getValue()));
		
		Score[] scoresInFile = Score.createFromJson(mockhandle); 
		
		System.out.println(scoresInFile[0].getScore());
		
		assertEquals(10, scoresInFile.length);
		assertEquals("mrks", scoresInFile[0].getName());
	}
	
}
