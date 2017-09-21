package bcccp.tickets.adhoc;

import java.util.Date;

public class AdhocTicket implements IAdhocTicket {
	
	private String carparkId_;
	private int ticketNo_;
	private long entryDateTime;
	private long paidDateTime;
	private long exitDateTime;
	private float charge;
	private String barcode;
	private STATE state_;
	
	private enum STATE { ISSUED, CURRENT, PAID, EXITED }

	
	
	public AdhocTicket(String carparkId, int ticketNo, String barcode) {
		this.carparkId_ = carparkId;
		this.ticketNo_ = ticketNo;
		this.barcode = barcode;
		this.state_ = STATE.ISSUED;		
	}

	
	
	@Override
	public String getBarcode() {
		return barcode;
	}


	
	@Override
	public String getCarparkId() {
		return carparkId_;
	}

	
	
	@Override
	public int getTicketNo() {
		return ticketNo_;
	}
	

	
	@Override
	public void enter(long entryDateTime) {
		this.entryDateTime = entryDateTime;
		this.state_ = STATE.CURRENT;		
	}
	
	
	
	@Override
	public long getEntryDateTime() {
		return entryDateTime;
	}

	
	
	@Override
	public void pay(long paidDateTime, float charge) {
		this.paidDateTime = paidDateTime;
		this.charge = charge;
		state_ = STATE.PAID;
	}
	
	
	
	@Override
	public long getPaidDateTime() {
		return paidDateTime;
	}



	@Override
	public float getCharge() {
		return charge;
	}

	
	
	public String toString() {
		Date entryDate = new Date(entryDateTime);
		Date paidDate = new Date(paidDateTime);
		Date exitDate = new Date(exitDateTime);

		return "Carpark    : " + carparkId_ + "\n" +
		       "Ticket No  : " + ticketNo_ + "\n" +
		       "Entry Time : " + entryDate + "\n" + 
		       "Paid Time  : " + paidDate + "\n" + 
		       "Exit Time  : " + exitDate + "\n" +
		       "State      : " + state_ + "\n" +
		       "Barcode    : " + barcode;		
	}



	@Override
	public boolean isCurrent() {
		return state_ == STATE.CURRENT;
	}



	@Override
	public boolean isPaid() {
		return state_ == STATE.PAID;
	}



	@Override
	public void exit(long dateTime) {
		exitDateTime = dateTime;
		state_ = STATE.EXITED;
	}



	@Override
	public long getExitDateTime() {
		return exitDateTime;
	}



	@Override
	public boolean hasExited() {
		return state_ == STATE.EXITED;
	}


}
