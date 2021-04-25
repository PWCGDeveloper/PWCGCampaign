package pwcg.campaign.api;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public interface IFixedPosition extends IPWCGObject
{
	public ICountry determineCountryOnDate(Date date) throws PWCGException;
    public ICountry getCountry(Date date) throws PWCGException;
    public Coordinate getPosition() throws PWCGException;
}
