package pwcg.campaign.group;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
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
		PWCGContext.setProduct(PWCGProduct.ROF);
        PWCGContext.getInstance().setTestUseMovingFront(false);
	}
	

	@Test
	public void airfieldCheckFranceTest() throws PWCGException 
	{
        PWCGContext.getInstance().setTestUseMovingFront(false);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);

        RoFAirfieldInEnemyTerritory airfieldFInder = new RoFAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.FRANCE_MAP, DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19181111"));
	}

	@Test
	public void airfieldCheckChannelTest() throws PWCGException 
	{
        PWCGContext.getInstance().setTestUseMovingFront(false);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.CHANNEL_MAP);

        RoFAirfieldInEnemyTerritory airfieldFInder = new RoFAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.CHANNEL_MAP, DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19181111"));
	}

	@Test
	public void airfieldCheckGaliciaTest() throws PWCGException 
	{
        PWCGContext.getInstance().setTestUseMovingFront(false);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.GALICIA_MAP);

        RoFAirfieldInEnemyTerritory airfieldFInder = new RoFAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.GALICIA_MAP, DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19171201"));
	}
}
