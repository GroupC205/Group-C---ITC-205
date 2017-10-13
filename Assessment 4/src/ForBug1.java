import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ForBug1 {
	@Test
	public void test() throws IOException {
		Game game = null;
		Field[] attrs = Dice.class.getDeclaredFields();
		for (Field attr: attrs) {
			if (attr.getName().equals("value")) {
				attr.setAccessible(true);
				Dice d1 = new Dice();
				try {
					attr.set(d1, DiceValue.CROWN);
				} catch (IllegalArgumentException | IllegalAccessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Dice d2 = new Dice();
				try {
					attr.set(d2, DiceValue.ANCHOR);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Dice d3 = new Dice();
				try {
					attr.set(d3, DiceValue.HEART);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				game = new Game(d1, d2, d3);
				break;
			}
		}

		DiceValue pick = DiceValue.HEART;
		Player player = new Player("john", 100);
		int bet = 5;

		int winnings = game.playRound(player, pick, bet);
		System.out.println("after match 1, player have " + player.getBalance());
		assertEquals(105, player.getBalance());


	}
}

