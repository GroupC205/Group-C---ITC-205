import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class ForBug3 {

	
	@Test
	public void test_playRound_Odds() {
		Random random = new Random(999);
		Player player = new Player("Pete", 100000000);
		Game game = new Game(new Dice(), new Dice(), new Dice());
		int wins = 0;
		int rounds = 5000000;
		
		for(int i = 0; i < rounds; i++)
		{
			DiceValue pick = DiceValue.values()[random.nextInt(6)];
			int winnings = game.playRound(player, pick, 1);
			if(winnings > 0)
				wins++;
		}
		assertEquals(0.42d, (double)wins/(double)rounds, 0.01d);
	}

	@Test
	public void test_rollDice_OddsOfAnchor() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		Dice dice = new Dice();
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = dice.roll() == DiceValue.ANCHOR;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_rollDice_OddsOfClub() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		Dice dice = new Dice();
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = dice.roll() == DiceValue.CLUB;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_rollDice_OddsOfCrown() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		Dice dice = new Dice();
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = dice.roll() == DiceValue.CROWN;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_rollDice_OddsOfDiamond() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		Dice dice = new Dice();
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = dice.roll() == DiceValue.DIAMOND;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_rollDice_OddsOfHeart() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		Dice dice = new Dice();
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = dice.roll() == DiceValue.HEART;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_rollDice_OddsOfSpade() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		Dice dice = new Dice();
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = dice.roll() == DiceValue.SPADE;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}

	@Test
	public void test_getRandomDiceValue_OddsOfAnchor() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = DiceValue.getRandom() == DiceValue.ANCHOR;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_getRandomDiceValue_OddsOfClub() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = DiceValue.getRandom() == DiceValue.CLUB;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_getRandomDiceValue_OddsOfCrown() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = DiceValue.getRandom() == DiceValue.CROWN;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_getRandomDiceValue_OddsOfDiamond() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = DiceValue.getRandom() == DiceValue.DIAMOND;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_getRandomDiceValue_OddsOfHeart() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = DiceValue.getRandom() == DiceValue.HEART;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_getRandomDiceValue_OddsOfSpade() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		
		for(int i = 0; i < testRolls; i++)
		{
			boolean match = DiceValue.getRandom() == DiceValue.SPADE;
			if(match)
				matchs++;
		}
		assertEquals(1d/6d, (double)matchs/(double)testRolls, 0.01d);
	}
	
	@Test
	public void test_getValueReturnsRolledValue() 
	{
		int matchs = 0;
		int testRolls = 5000000;
		Dice dice = new Dice();
		
		for(int i = 0; i < testRolls; i++)
		{
			DiceValue rolledValue = dice.roll(); 
			boolean match = dice.getValue() == rolledValue;
			if(match)
				matchs++;
		}
		assertEquals(1, (double)matchs/(double)testRolls, 0);
	}
}