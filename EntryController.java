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

	private IGate entryGate;
	private ICarSensor outsideSensor; 
	private ICarSensor insideSensor;
	private IEntryUI ui;

	private ICarpark carpark;
	private IAdhocTicket  adhocTicket = null;
	private String seasonTicketId = null;
	Boolean flagEntering = false;

	public EntryController(Carpark carpark, IGate entryGate, 
			ICarSensor os, 
			ICarSensor is,
			IEntryUI ui) {
		this.carpark = carpark;
		this.entryGate = entryGate;
		this.outsideSensor = os;
		this.insideSensor = is;
		this.ui = ui;
		entryControllerRegister();

	}
	private void entryControllerRegister() {
		outsideSensor.registerResponder(this);
		insideSensor.registerResponder(this);
		ui.registerController(this);
		carpark.register(this);

	}
	@Override
	public void buttonPushed() {
		if (carpark.isFull()){
			ui.display("Carpark Full");
			return;
		}
		if (outsideSensor.carIsDetected()){
			adhocTicket = carpark.issueAdhocTicket();
			ui.printTicket(adhocTicket.getCarparkId(), adhocTicket.getTicketNo(),
					adhocTicket.getEntryDateTime(), adhocTicket.getBarcode());
			ui.display("Take Ticket");
		}

	}

	@Override
	public void ticketInserted(String barcode) {
		if (outsideSensor.carIsDetected()){
			if (carpark.isSeasonTicketValid(barcode)){
				if(!carpark.isSeasonTicketInUse(barcode)){
					seasonTicketId = barcode;
					ui.display("Take Ticket");
				}
			}
			else {

				ui.display("Remove Invalid Ticket");
			}
		}

	}
	@Override
	public void ticketTaken() {
		if (seasonTicketId != null || adhocTicket != null){
			entryGate.raise();
			ui.display("Enter Carpark");
		} else {
			ui.display("Push Button");
		}


	}
	@Override
	public void notifyCarparkEvent() {
		if (!carpark.isFull()){
			this.carEventDetected(outsideSensor.getId(), outsideSensor.carIsDetected());
		} else {
			ui.display("Carpark Full");
		}

	}

	@Override
	public void carEventDetected(String detectorId, boolean detected) {
	}

}
