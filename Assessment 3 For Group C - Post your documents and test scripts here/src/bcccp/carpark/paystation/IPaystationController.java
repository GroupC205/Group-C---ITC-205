package bcccp.carpark.paystation;

public interface IPaystationController {
	
	public void ticketInserted(String barcode);
	public void ticketPaid();
	public void ticketTaken();

}
