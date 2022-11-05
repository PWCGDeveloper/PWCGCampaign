package pwcg.campaign.api;

import java.util.Date;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public interface IFixedPosition extends IPWCGObject
{
	public ICountry determineCountryOnDate(FrontMapIdentifier mapIdentifier, Date date) throws PWCGException;
    public Coordinate getPosition() throws PWCGException;
}
