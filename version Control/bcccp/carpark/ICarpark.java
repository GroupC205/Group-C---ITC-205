package bcccp.carpark;

import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.season.ISeasonTicket;

public interface ICarpark {
	
	public void register(ICarparkObserver observer);
	public void deregister(ICarparkObserver observer);
	public String getName();
	public boolean isFull();
	
	public IAdhocTicket issueAdhocTicket();
	public void recordAdhocTicketEntry();
	public IAdhocTicket getAdhocTicket(String barcode);
	public float calculateAddHocTicketCharge(long entryDateTime);
	public void recordAdhocTicketExit();
	
	public void registerSeasonTicket(ISeasonTicket seasonTicket);
	public void deregisterSeasonTicket(ISeasonTicket seasonTicket);

	public boolean isSeasonTicketValid(String ticketId);
	public boolean isSeasonTicketInUse(String ticketId);
	public void recordSeasonTicketEntry(String ticketId);
	public void recordSeasonTicketExit(String ticketId);


}
