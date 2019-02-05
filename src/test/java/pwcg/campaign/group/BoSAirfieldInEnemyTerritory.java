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

        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.MOSCOW_MAP, DateUtils.getDateYYYYMMDD("19411001"), DateUtils.getDateYYYYMMDD("19420301"));
	}
	   
    @Test
    public void airfieldCheckStalingradEarlyTest() throws PWCGException 
    {
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);

        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.STALINGRAD_MAP, DateUtils.getDateYYYYMMDD("19420301"), DateUtils.getDateYYYYMMDD("19420601"));
    }

	@Test
	public void airfieldCheckStalingradTest() throws PWCGException 
	{
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);

        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.STALINGRAD_MAP, DateUtils.getDateYYYYMMDD("19420801"), DateUtils.getDateYYYYMMDD("19430301"));
	}
	
	@Test
	public void airfieldCheckKubanEarlyTest() throws PWCGException 
	{
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);

        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.KUBAN_MAP, DateUtils.getDateYYYYMMDD("19420601"), DateUtils.getDateYYYYMMDD("19420801"));
	}
    
    @Test
    public void airfieldCheckKubanTest() throws PWCGException 
    {
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);

        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.KUBAN_MAP, DateUtils.getDateYYYYMMDD("19430301"), DateUtils.getDateYYYYMMDD("19431201"));
    }
    

    @Test
    public void airfieldCheckBodenplatteTest() throws PWCGException 
    {
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.BODENPLATTE_MAP);

        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.BODENPLATTE_MAP, DateUtils.getDateYYYYMMDD("19440801"), DateUtils.getDateYYYYMMDD("19450503"));
    }

}
