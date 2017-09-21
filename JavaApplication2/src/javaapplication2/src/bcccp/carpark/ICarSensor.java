package bcccp.carpark;

public interface ICarSensor {
	public void registerResponder(ICarSensorResponder responder);
	public void deregisterResponder(ICarSensorResponder responder);
	
	public String getId();
	public boolean carIsDetected();

}
