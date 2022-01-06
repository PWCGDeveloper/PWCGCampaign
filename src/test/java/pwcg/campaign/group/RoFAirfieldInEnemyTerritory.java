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
        PWCGContext.setProduct(PWCGProduct.BOS);
	}

	@Test
	public void airfieldCheckArrasTest() throws PWCGException 
	{
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);

        RoFAirfieldInEnemyTerritory airfieldFInder = new RoFAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.STALINGRAD_MAP, DateUtils.getDateYYYYMMDD("19160101"), DateUtils.getDateYYYYMMDD("19181111"));
	}
}
