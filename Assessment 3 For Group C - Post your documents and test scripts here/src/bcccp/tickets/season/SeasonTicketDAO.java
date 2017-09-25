package bcccp.tickets.season;

import java.util.HashMap;
import java.util.Map;

import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.IUsageRecordFactory;

public class SeasonTicketDAO implements ISeasonTicketDAO {

	private Map<String, ISeasonTicket> currentTickets;
	private IUsageRecordFactory factory;

	
	
	public SeasonTicketDAO(IUsageRecordFactory factory) {
		this.factory = factory;
		currentTickets = new HashMap<>();		
	}
	
	
	
	@Override
	public void registerTicket(ISeasonTicket ticket) {
		if (!currentTickets.containsKey(ticket.getId())) {
			currentTickets.put(ticket.getId(),ticket);
		}
	}
	
	
	
	@Override
	public void deregisterTicket(ISeasonTicket ticket) {
		if (currentTickets.containsKey(ticket.getId())) {
			currentTickets.remove(ticket.getId());
		}
	}
		
		
	@Override
	public int getNumberOfTickets() {
		return currentTickets.size();
	}

	@Override
	public ISeasonTicket findTicketById(String barcode) {
		if (currentTickets.containsKey(barcode)) {
			return currentTickets.get(barcode);
		}
		return null;
	}



	@Override
	public void recordTicketEntry(String ticketId) {
		ISeasonTicket ticket = findTicketById(ticketId);
		if (ticket == null) throw new RuntimeException("recordTicketUsage : no such ticket: " + ticketId);
		
		long datetime = System.currentTimeMillis();
		IUsageRecord usage = factory.make(ticketId, datetime);
		ticket.recordUsage(usage);		
	}



	@Override
	public void recordTicketExit(String ticketId) {
		ISeasonTicket ticket = findTicketById(ticketId);
		if (ticket == null) throw new RuntimeException("finaliseTicketUsage : no such ticket: " + ticketId);

		long dateTime = System.currentTimeMillis();
		ticket.endUsage(dateTime);
		
		
		
	}
}


