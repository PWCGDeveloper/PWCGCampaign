package pwcg.campaign.group;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;

public class AirfieldManagerTest
{
	@Before
	public void setup() throws PWCGException
	{
		PWCGContextManager.setRoF(true);
	}
	

	@Test
	public void airfieldValidityCheckFranceTest() throws PWCGException 
	{
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);
        PWCGContextManager.getInstance().setTestUseMovingFront(false);
        AirfieldManager airfieldManager = PWCGContextManager.getInstance().getMapByMapId(FrontMapIdentifier.FRANCE_MAP).getAirfieldManager();
        for (IAirfield airfield : airfieldManager.getAllAirfields().values())
        {
        	assert (airfield.getPlanePosition() != null);
        }
	}

}
