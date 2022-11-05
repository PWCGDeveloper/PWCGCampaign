package pwcg.campaign.group;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class BoSAirfieldInEnemyTerritory extends AirfieldInEnemyTerritory
{
	public BoSAirfieldInEnemyTerritory () throws PWCGException
	{
		super(true);
        PWCGContext.setProduct(PWCGProduct.BOS);
	}
	
	@BeforeEach
	public void setupTest() throws PWCGException
	{
	}
	
	@Test
	public void airfieldCheckMoscowTest() throws PWCGException 
	{
        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.MOSCOW_MAP, DateUtils.getDateYYYYMMDD("19411001"), DateUtils.getDateYYYYMMDD("19420301"));
	}
	   
    @Test
    public void airfieldCheckStalingradEarlyTest() throws PWCGException 
    {
        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.STALINGRAD_MAP, DateUtils.getDateYYYYMMDD("19420301"), DateUtils.getDateYYYYMMDD("19420601"));
    }

	@Test
	public void airfieldCheckStalingradTest() throws PWCGException 
	{
        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.STALINGRAD_MAP, DateUtils.getDateYYYYMMDD("19420801"), DateUtils.getDateYYYYMMDD("19430301"));
	}

    @Test
    public void airfieldCheckEast1944Test() throws PWCGException 
    {
        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.EAST1944_MAP, DateUtils.getDateYYYYMMDD("19440101"), DateUtils.getDateYYYYMMDD("19441101"));
    }

    @Test
    public void airfieldCheckEast1945Test() throws PWCGException 
    {
        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.EAST1945_MAP, DateUtils.getDateYYYYMMDD("19450101"), DateUtils.getDateYYYYMMDD("19450503"));
    }

	@Test
	public void airfieldCheckKubanEarlyTest() throws PWCGException 
	{
        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.KUBAN_MAP, DateUtils.getDateYYYYMMDD("19420601"), DateUtils.getDateYYYYMMDD("19420801"));
	}
    
    @Test
    public void airfieldCheckKubanTest() throws PWCGException 
    {
        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.KUBAN_MAP, DateUtils.getDateYYYYMMDD("19430301"), DateUtils.getDateYYYYMMDD("19431201"));
    }

    @Test
    public void airfieldCheckBodenplatteTest() throws PWCGException 
    {
        BoSAirfieldInEnemyTerritory airfieldFinder = new BoSAirfieldInEnemyTerritory();
        airfieldFinder.findEnemy(FrontMapIdentifier.BODENPLATTE_MAP, DateUtils.getDateYYYYMMDD("19440901"), DateUtils.getDateYYYYMMDD("19450503"));
    }

}
