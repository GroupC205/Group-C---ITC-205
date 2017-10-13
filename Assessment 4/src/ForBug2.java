import static org.junit.Assert.*;

import org.junit.Test;

public class ForBug2 {
    
    @Test
    public void testAmountAt0() {
    	Player player = new Player("john", 100);
    	player.setLimit(0);
    	int turn = 0;
    	int bet = 5;
    	while (player.balanceExceedsLimitBy(bet))
    	{
    		player.takeBet(bet);
    		turn++;
    	}
    	System.out.println("player left balance is " + player.getBalance());
    	System.out.println("turn is " + turn);
    	assertEquals(0, player.getBalance());
    }

}
