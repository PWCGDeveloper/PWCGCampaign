package pwcg.campaign.group;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class RoFAirfieldInEnemyTerritory extends AirfieldInEnemyTerritory
{
	public RoFAirfieldInEnemyTerritory ()
	{
		super(true);
	}
	
	@Before
	public void setup() throws PWCGException
	{
		PWCGContextManager.setRoF(true);
        PWCGContextManager.getInstance().setTestUseMovingFront(false);
	}
	

	@Test
	public void airfieldCheckFranceTest() throws PWCGException 
	{
        PWCGContextManager.getInstance().setTestUseMovingFront(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);

        RoFAirfieldInEnemyTerritory airfieldFInder = new RoFAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.FRANCE_MAP, DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19181111"));
	}

	@Test
	public void airfieldCheckChannelTest() throws PWCGException 
	{
        PWCGContextManager.getInstance().setTestUseMovingFront(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.CHANNEL_MAP);

        RoFAirfieldInEnemyTerritory airfieldFInder = new RoFAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.CHANNEL_MAP, DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19181111"));
	}

	@Test
	public void airfieldCheckGaliciaTest() throws PWCGException 
	{
        PWCGContextManager.getInstance().setTestUseMovingFront(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.GALICIA_MAP);

        RoFAirfieldInEnemyTerritory airfieldFInder = new RoFAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.GALICIA_MAP, DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19171201"));
	}
}
