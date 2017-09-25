package bcccp.tickets.season;

import java.util.List;

public interface ISeasonTicket {
	
	public String getId();
	public String getCarparkId();
	public long getStartValidPeriod();
	public long getEndValidPeriod();
	
	public boolean inUse();
	public void recordUsage(IUsageRecord record);
	public IUsageRecord getCurrentUsageRecord();
	public void endUsage(long dateTime);
	
	public List<IUsageRecord> getUsageRecords();

}
