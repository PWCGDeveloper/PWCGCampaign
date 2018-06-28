package pwcg.campaign.api;

import java.util.Date;

import pwcg.core.exception.PWCGException;

public interface IFixedPosition extends IPWCGObject
{
	public ICountry createCountry(Date date) throws PWCGException;
	public ICountry getCountry(Date date) throws PWCGException;
}
