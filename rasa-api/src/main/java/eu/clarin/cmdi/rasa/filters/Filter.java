package eu.clarin.cmdi.rasa.filters;

import java.sql.Timestamp;

public interface Filter<T>{

	public T setUrlIs(String url);
	
	public T setUrlIn(String... urls);
	
	public T setSourceIs(String source);
	
	public T setProviderGroupIs(String providerGroup);
	
	public T setRecordIs(String record);
	
	public T setIngestionDateIs(Timestamp ingestionDate);
	
    public T setLimit(int offset, int limit);
    
    public T setIsActive(boolean active);
    
    public T setOrderByCheckingDate(boolean isAscending);
}
