package bcccp.carpark.entry;

import bcccp.carpark.Carpark;
import bcccp.carpark.ICarSensor;
import bcccp.carpark.ICarSensorResponder;
import bcccp.carpark.ICarpark;
import bcccp.carpark.ICarparkObserver;
import bcccp.carpark.IGate;
import bcccp.tickets.adhoc.IAdhocTicket;

public class EntryController 
		implements ICarSensorResponder,
				   ICarparkObserver,
		           IEntryController {
	
	private enum STATE { IDLE, WAITING, FULL, VALIDATED, ISSUED, TAKEN, ENTERING, ENTERED, BLOCKED } 
	
	private STATE state_;
	private STATE prevState_;
	private String message;
	
	private IGate entryGate_;
	private ICarSensor outsideEntrySensor_; 
	private ICarSensor insideEntrySensor_;
	private IEntryUI ui;
	
	private ICarpark carpark;
	private IAdhocTicket  adhocTicket = null;
	private long entryTime;
	private String seasonTicketId = null;
	
	

	public EntryController(Carpark carpark, IGate entryGate, 
			ICarSensor os, 
			ICarSensor is,
			IEntryUI ui) {
		
		this.carpark = carpark;
		this.entryGate_ = entryGate;
		this.outsideEntrySensor_ = os;
		this.insideEntrySensor_ = is;
		this.ui = ui;
		
		outsideEntrySensor_.registerResponder(this);
		insideEntrySensor_.registerResponder(this);
		ui.registerController(this);
		
		setState(STATE.IDLE);
		
	}

	
	
	private void log(String message) {
		System.out.println("EntryController : " + message);
	}



	@Override
	public void carEventDetected(String detectorId, boolean carDetected) {

		log("carEventDetected: " + detectorId + ", car Detected: " + carDetected );
		
		switch (state_) {
		
		case BLOCKED: 
			if (detectorId.equals(insideEntrySensor_.getId()) && !carDetected) {
				setState(prevState_);
			}
			break;
			
		case IDLE: 
			log("eventDetected: IDLE");
			if (detectorId.equals(outsideEntrySensor_.getId()) && carDetected) {
				log("eventDetected: setting state to WAITING");
				setState(STATE.WAITING);
			}
			else if (detectorId.equals(insideEntrySensor_.getId()) && carDetected) {
				setState(STATE.BLOCKED);
			}
			break;
			
		case WAITING: 
		case FULL: 
		case VALIDATED: 
		case ISSUED: 
			if (detectorId.equals(outsideEntrySensor_.getId()) && !carDetected) {
				setState(STATE.IDLE);
			}
			else if (detectorId.equals(insideEntrySensor_.getId()) && carDetected) {
				setState(STATE.BLOCKED);
			}
			break;
			
		case TAKEN: 
			if (detectorId.equals(outsideEntrySensor_.getId()) && !carDetected) {
				setState(STATE.IDLE);
			}
			else if (detectorId.equals(insideEntrySensor_.getId()) && carDetected) {
				setState(STATE.ENTERING);
			}
			break;
			
		case ENTERING: 
			if (detectorId.equals(outsideEntrySensor_.getId()) && !carDetected) {
				setState(STATE.ENTERED);
			}
			else if (detectorId.equals(insideEntrySensor_.getId()) && !carDetected) {
				setState(STATE.TAKEN);
			}
			break;
			
		case ENTERED: 
			if (detectorId.equals(outsideEntrySensor_.getId()) && carDetected) {
				setState(STATE.ENTERING);
			}
			else if (detectorId.equals(insideEntrySensor_.getId()) && !carDetected) {
				setState(STATE.IDLE);
			}
			break;
			
		default: 
			break;
			
		}
		
	}

	
	
	private void setState(STATE newState) {
		switch (newState) {
		
		case BLOCKED: 
			log("setState: BLOCKED");
			prevState_ = state_;
			state_ = STATE.BLOCKED;
			message = "Blocked";
			ui.display(message);
			break;
			
		case IDLE: 
			log("setState: IDLE");
			if (prevState_ == STATE.ENTERED) {
				if (adhocTicket != null) {
					adhocTicket.enter(entryTime);
					carpark.recordAdhocTicketEntry();
					entryTime = 0;
					log(adhocTicket.toString() );
					adhocTicket = null;
				}
				else if (seasonTicketId != null) {
					carpark.recordSeasonTicketEntry(seasonTicketId);
					seasonTicketId = null;
				}
			}
			message = "Idle";
			state_ = STATE.IDLE;
			prevState_ = state_;
			ui.display(message);
			if (outsideEntrySensor_.carIsDetected()) {
				setState(STATE.WAITING);
			}
			if (entryGate_.isRaised()) {
				entryGate_.lower();
			}
			ui.discardTicket();
			break;
			
		case WAITING: 
			log("setState: WAITING");
			message = "Push Button";
			state_ = STATE.WAITING;
			prevState_ = state_;
			ui.display(message);
			if (!outsideEntrySensor_.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case FULL: 
			log("setState: FULL");
			message = "Carpark Full";
			state_ = STATE.FULL;
			prevState_ = state_;
			ui.display(message);
			break;
			
		case VALIDATED: 
			log("setState: VALIDATED");
			message = "Ticket Validated";
			state_ = STATE.VALIDATED;
			prevState_ = state_;
			ui.display(message);
			if (!outsideEntrySensor_.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case ISSUED: 
			log("setState: ISSUED");
			message = "Take Ticket";
			state_ = STATE.ISSUED;
			prevState_ = state_;
			ui.display(message);
			if (!outsideEntrySensor_.carIsDetected()) {
				setState(STATE.IDLE);
			}
			break;
			
		case TAKEN: 
			log("setState: TAKEN");
			message = "Ticket Taken";
			state_ = STATE.TAKEN;
			prevState_ = state_;
			ui.display(message);
			entryGate_.raise();
			break;
			
		case ENTERING: 
			log("setState: ENTERING");
			message = "Entering";
			state_ = STATE.ENTERING;
			prevState_ = state_;
			ui.display(message);
			break;
			
		case ENTERED: 
			log("setState: ENTERED");
			message = "Entered";
			state_ = STATE.ENTERED;
			prevState_ = state_;
			ui.display(message);
			break;
			
		default: 
			break;
			
		}
				
	}

	
	
	@Override
	public void buttonPushed() {
		if (state_ == STATE.WAITING) {
			if (!carpark.isFull()) {
				adhocTicket = carpark.issueAdhocTicket();
				
				String carparkId = adhocTicket.getCarparkId();
				int ticketNo = adhocTicket.getTicketNo();
				entryTime = System.currentTimeMillis();
				//entryTime = adhocTicket.getEntryDateTime();
				String barcode = adhocTicket.getBarcode();
				
				ui.printTicket(carparkId, ticketNo, entryTime, barcode);
				setState(STATE.ISSUED);
			}
			else {
				setState(STATE.FULL);
			}
		}
		else {
			ui.beep();
			log("ButtonPushed: called while in incorrect state");
		}
		
	}

	
	
	@Override
	public void ticketInserted(String barcode) {
		if (state_ == STATE.WAITING) {
			try {
				if (carpark.isSeasonTicketValid(barcode) &&
					!carpark.isSeasonTicketInUse(barcode)) {
					this.seasonTicketId = barcode;
					setState(STATE.VALIDATED);
				}
				else {
					ui.beep();
					seasonTicketId = null;
					log("ticketInserted: invalid ticket id");				
				}
			}
			catch (NumberFormatException e) {
				ui.beep();
				seasonTicketId = null;
				log("ticketInserted: invalid ticket id");				
			}
		}
		else {
			ui.beep();
			log("ticketInserted: called while in incorrect state");
		}
		
	}
	
	
	
	@Override
	public void ticketTaken() {
		if (state_ == STATE.ISSUED || state_ == STATE.VALIDATED ) {
			setState(STATE.TAKEN);
		}
		else {
			ui.beep();
			log("ticketTaken: called while in incorrect state");
		}
		
	}



	@Override
	public void notifyCarparkEvent() {
		if (state_ == STATE.FULL) {
			if (!carpark.isFull()) {
				setState(STATE.WAITING);
			}
		}
		
	}

	

}
