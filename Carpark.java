package bcccp.carpark;

import java.util.ArrayList;
import java.util.List;

import bcccp.tickets.adhoc.IAdhocTicket;
import bcccp.tickets.adhoc.IAdhocTicketDAO;
import bcccp.tickets.season.ISeasonTicket;
import bcccp.tickets.season.ISeasonTicketDAO;

public class Carpark implements ICarpark {

	private List<ICarparkObserver> observers;
	private String carparkId;
	private int capacity;
	private int numberOfCarsParked;
	private IAdhocTicketDAO adhocTicketDAO;
	private ISeasonTicketDAO seasonTicketDAO;
	final long FIFTEEN_MINUTES = 900000;
	final float FIFTEEN_MINUTE_PRICE = 4;

	public Carpark(String name, int capacity, 
			IAdhocTicketDAO adhocTicketDAO, 
			ISeasonTicketDAO seasonTicketDAO) {
		this.carparkId = name;
		this.capacity = capacity;
		this.numberOfCarsParked = 0;
		this.seasonTicketDAO = seasonTicketDAO;
		this.adhocTicketDAO = adhocTicketDAO;
		this.observers = new ArrayList<>();
	}


	@Override
	public void register(ICarparkObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}

	}


	@Override
	public void deregister(ICarparkObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}

	}


	@Override
	public String getName() {
		return this.carparkId;
	}


	@Override
	public boolean isFull() {
		return (numberOfCarsParked >= capacity);
	}



	@Override
	public IAdhocTicket issueAdhocTicket() {

		return adhocTicketDAO.createTicket(carparkId);
	}


	@Override
	public void recordAdhocTicketEntry(IAdhocTicket ticket) {

		adhocTicketDAO.addToCurrentList(ticket);
		numberOfCarsParked++;
		if (this.isFull()){ //If the carpark is full, notify all observers. Entry pillars will then display carpark full.

			for (int i = 0; i < observers.size(); i++){
				observers.get(i).notifyCarparkEvent();
			}

		}

	}

	@Override
	public IAdhocTicket getAdhocTicket(String barcode) { 
		//return adhocTicket object, or null if not found
		return adhocTicketDAO.findTicketByBarcode(barcode);

	}

	@Override
	public float calculateAdHocTicketCharge(long entryDateTime) {
		long stayTime = System.currentTimeMillis() - entryDateTime;

		float fifteenMinuteLotsStayed = (stayTime / FIFTEEN_MINUTES) + 1;

		return fifteenMinuteLotsStayed * FIFTEEN_MINUTE_PRICE;
	}

	@Override
	public void recordAdhocTicketExit(IAdhocTicket ticket) {
		numberOfCarsParked--;

		adhocTicketDAO.removeFromCurrentList(ticket);

		for (int i = 0; i < observers.size(); i++){
			observers.get(i).notifyCarparkEvent();
		}

	}

	@Override
	public void registerSeasonTicket(ISeasonTicket seasonTicket) {
		seasonTicketDAO.registerTicket(seasonTicket);

	}


	@Override
	public void deregisterSeasonTicket(ISeasonTicket seasonTicket) {
		seasonTicketDAO.deregisterTicket(seasonTicket);

	}


	@Override
	public boolean isSeasonTicketValid(String ticketId) {
		ISeasonTicket seasonTicket = seasonTicketDAO.findTicketById(ticketId);
		return (seasonTicket != null) && (System.currentTimeMillis() >= seasonTicket.getEndValidPeriod());
	}


	@Override
	public boolean isSeasonTicketInUse(String ticketId) {
		ISeasonTicket seasonTicket = seasonTicketDAO.findTicketById(ticketId);
		return seasonTicket.getCurrentUsageRecord() != null;
	}


	@Override
	public void recordSeasonTicketEntry(String ticketId) {
		seasonTicketDAO.recordTicketEntry(ticketId);

	}


	@Override
	public void recordSeasonTicketExit(String ticketId) {
		seasonTicketDAO.recordTicketExit(ticketId);

	}

}