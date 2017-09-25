package bcccp.tickets.adhoc;

public interface IAdhocTicket {

	public int getTicketNo();
	public String getBarcode();
	public String getCarparkId();

	public void enter(long dateTime);
	public long getEntryDateTime();
	public boolean isCurrent();
	
	public void pay(long dateTime, float charge);
	public long getPaidDateTime();
	public boolean isPaid();
	public float getCharge();
	
	public void exit(long dateTime);
	public long getExitDateTime();
	public boolean hasExited();
	
	
	
}
