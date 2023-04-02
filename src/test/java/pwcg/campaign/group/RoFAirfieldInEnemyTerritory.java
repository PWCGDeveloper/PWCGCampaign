package pwcg.campaign.group;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class RoFAirfieldInEnemyTerritory extends AirfieldInEnemyTerritory
{
	public RoFAirfieldInEnemyTerritory () throws PWCGException
	{
		super(true);
        PWCGContext.setProduct(PWCGProduct.FC);
	}

	@Test
	public void airfieldCheckWesternFrontTest() throws PWCGException 
	{
        RoFAirfieldInEnemyTerritory airfieldFInder = new RoFAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.WESTERN_FRONT_MAP, DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19181111"));
	}
}
