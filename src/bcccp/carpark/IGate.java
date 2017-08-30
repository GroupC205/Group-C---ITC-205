package bcccp.carpark;

public interface IGate {
	public void raise();
	public void lower();
	
	public boolean isRaised();
	
}
