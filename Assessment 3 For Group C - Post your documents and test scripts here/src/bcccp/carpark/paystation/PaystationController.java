package bcccp.carpark.paystation;

import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.IAdhocTicket;

public class PaystationController 
		implements IPaystationController {
	
	private enum STATE { IDLE, WAITING, REJECTED, PAID } 
	
	private STATE state_;
	
	private IPaystationUI ui_;
	
	private ICarpark carpark_;

	private IAdhocTicket  adhocTicket_ = null;
	private float charge_;
	
	

	public PaystationController(ICarpark carpark, IPaystationUI ui) {
		
		this.carpark_ = carpark;
		this.ui_ = ui;
		
		ui.registerController(this);		
		setState(STATE.IDLE);		
	}

	
	
	private void log(String message) {
		System.out.println("EntryController : " + message);
	}

	
	
	private void setState(STATE newState) {
		switch (newState) {
		
		case IDLE: 
			state_ = STATE.IDLE;
			ui_.display("Idle");
			
			log("setState: IDLE");
			break;
			
		case WAITING: 
			state_ = STATE.WAITING;
			log("setState: WAITING");
			break;
			
		case REJECTED: 
			state_ = STATE.WAITING;
			log("setState: WAITING");
			break;
			
		case PAID: 
			state_ = STATE.PAID;
			ui_.display("Paid");
			log("setState: PAID");
			break;			
			
		default: 
			break;
			
		}			
	}

	
	
	@Override
	public void ticketInserted(String barcode) {
		if (state_ == STATE.IDLE) {
			adhocTicket_ = carpark_.getAdhocTicket(barcode);
			if (adhocTicket_ != null) {
				charge_ = carpark_.calculateAddHocTicketCharge(adhocTicket_.getEntryDateTime());
				ui_.display("Pay " + String.format("%.2f", charge_));
				setState(STATE.WAITING);
			}
			else {
				ui_.beep();
				ui_.display("Take Rejected Ticket");
				setState(STATE.REJECTED);
				log("ticketInserted: ticket is not current");				
			}
		}
		else {
			ui_.beep();
			log("ticketInserted: called while in incorrect state");				
		}
	}
	
	
	
	@Override
	public void ticketPaid() {
		if (state_ == STATE.WAITING) {
			long payTime = System.currentTimeMillis();
			
			adhocTicket_.pay(payTime, charge_);
			
			String carparkId = adhocTicket_.getCarparkId();
			int ticketNo = adhocTicket_.getTicketNo();
			long entryTime = adhocTicket_.getEntryDateTime();
			long paidTime = adhocTicket_.getPaidDateTime();
			float charge = adhocTicket_.getCharge();
			String barcode = adhocTicket_.getBarcode();
			
			ui_.printTicket(carparkId, ticketNo, entryTime, paidTime, charge, barcode);
			setState(STATE.PAID);
		}
		else {
			ui_.beep();
			log("ticketPaid: called while in incorrect state");				
		}
	}

	
	
	@Override
	public void ticketTaken() {
		if (state_ == STATE.IDLE) {
			ui_.beep();
			log("ticketTaken: called while in incorrect state");				
		}
		else {
			setState(STATE.IDLE);
		}
	}

}
