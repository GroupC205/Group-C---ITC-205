package bcccp.tickets.season;

public interface IUsageRecord {
	
	public void finalise(long endDateTime);
	public long getStartTime();
	public long getEndTime();
	public String getSeasonTicketId();

}
