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
public class BoSAirfieldInEnemyTerritory extends AirfieldInEnemyTerritory
{
	public BoSAirfieldInEnemyTerritory ()
	{
		super(true);
	}
	
	@Before 
	public void setup() throws PWCGException
	{
    	PWCGContextManager.setRoF(false);      
	}
	
	@Test
	public void airfieldCheckMoscowTest() throws PWCGException 
	{
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);

        BoSAirfieldInEnemyTerritory airfieldFInder = new BoSAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.MOSCOW_MAP, DateUtils.getDateYYYYMMDD("19411001"), DateUtils.getDateYYYYMMDD("19420201"));
	}
	
	@Test
	public void airfieldCheckStalingradTest() throws PWCGException 
	{
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);

        BoSAirfieldInEnemyTerritory airfieldFInder = new BoSAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.STALINGRAD_MAP, DateUtils.getDateYYYYMMDD("19420801"), DateUtils.getDateYYYYMMDD("19430201"));
	}
	
	
	@Test
	public void airfieldCheckKubanTest() throws PWCGException 
	{
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);

        BoSAirfieldInEnemyTerritory airfieldFInder = new BoSAirfieldInEnemyTerritory();
        airfieldFInder.findEnemy(FrontMapIdentifier.KUBAN_MAP, DateUtils.getDateYYYYMMDD("19420601"), DateUtils.getDateYYYYMMDD("19431201"));
	}
}
