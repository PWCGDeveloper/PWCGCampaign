package pwcg.campaign.group;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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
		PWCGContext.setProduct(PWCGProduct.FC);
	}
	

	@Test
	public void airfieldCheckArrasTest() throws PWCGException 
	{
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);

        RoFAirfieldInEnemyTerritory airfieldFInder = new RoFAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.ARRAS_MAP, DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19181111"));
	}
}
