package pwcg.campaign.medals;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.product.fc.country.FCServiceManager;
import pwcg.product.fc.medals.GermanMedalManager;

@RunWith(MockitoJUnitRunner.Silent.class) 
public class RoFGermanMedalManagerTest extends MedalManagerTestBase
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE, DateUtils.getDateYYYYMMDD("19171001"));

        super.setup();
        Mockito.when(country.isCountry(Country.GERMANY)).thenReturn(true);
        Mockito.when(player.getPlayerRegion()).thenReturn("");
        ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }
    
    @Test
    public void testGermanMedals () throws PWCGException
    {            	
        awardMedal(GermanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(GermanMedalManager.IRON_CROSS_2, 1, 1);
		awardMedal(GermanMedalManager.IRON_CROSS_1, 6, 1);
		awardMedal(GermanMedalManager.ORDER_HOUSE_HOHENZOLLERN, 13, 1);
		awardMedal(GermanMedalManager.POUR_LE_MERIT, 20, 1);
		awardMedal(GermanMedalManager.ORDER_RED_EAGLE, 70, 1);
    }

	@Test
	public void testBavarianMedals () throws PWCGException
	{            	
        Mockito.when(player.getPlayerRegion()).thenReturn(SquadronMember.BAVARIA);

        awardMedal(GermanMedalManager.B_PILOTS_BADGE, 0, 0);
		awardMedal(GermanMedalManager.IRON_CROSS_2, 1, 1);
		awardMedal(GermanMedalManager.IRON_CROSS_1, 6, 1);
		awardMedal(GermanMedalManager.ORDER_HOUSE_HOHENZOLLERN, 13, 1);
		awardMedal(GermanMedalManager.POUR_LE_MERIT, 20, 1);
		awardMedal(GermanMedalManager.ORDER_RED_EAGLE, 70, 1);
		
		awardMedal(GermanMedalManager.B_MEDAL_BRAVERY, 4, 1);
		awardMedal(GermanMedalManager.B_MILITARY_MERIT, 8, 1);
		awardMedal(GermanMedalManager.B_MAX_JOSEPH, 35, 1);
	}

	@Test
	public void testPrussianMedals () throws PWCGException
	{            	
        Mockito.when(player.getPlayerRegion()).thenReturn(SquadronMember.PRUSSIA);

        awardMedal(GermanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(GermanMedalManager.IRON_CROSS_2, 1, 1);
		awardMedal(GermanMedalManager.IRON_CROSS_1, 6, 1);
		awardMedal(GermanMedalManager.ORDER_HOUSE_HOHENZOLLERN, 13, 1);
		awardMedal(GermanMedalManager.POUR_LE_MERIT, 20, 1);
		awardMedal(GermanMedalManager.ORDER_RED_EAGLE, 70, 1);
		
		awardMedal(GermanMedalManager.P_WAR_MERIT_MEDAL, 4, 1);

		Mockito.when(player.getRank()).thenReturn("Feldwebel");        
		awardMedal(GermanMedalManager.P_MILITARY_MERIT_CROSS, 8, 1);
	}

	@Test
	public void testWurttemburgMedals () throws PWCGException
	{            	
        Mockito.when(player.getPlayerRegion()).thenReturn(SquadronMember.WURTTEMBURG);

        awardMedal(GermanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(GermanMedalManager.IRON_CROSS_2, 1, 1);
		awardMedal(GermanMedalManager.IRON_CROSS_1, 6, 1);
		awardMedal(GermanMedalManager.ORDER_HOUSE_HOHENZOLLERN, 13, 1);
		awardMedal(GermanMedalManager.POUR_LE_MERIT, 20, 1);
		awardMedal(GermanMedalManager.ORDER_RED_EAGLE, 70, 1);
		
		awardMedal(GermanMedalManager.W_MILITARY_MERIT, 8, 1);
		awardMedal(GermanMedalManager.W_FREDRICH_ORDER, 25, 1);
	}

	@Test
	public void testSaxonyMedals () throws PWCGException
	{            	
        Mockito.when(player.getPlayerRegion()).thenReturn(SquadronMember.SAXONY);

        awardMedal(GermanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(GermanMedalManager.IRON_CROSS_2, 1, 1);
		awardMedal(GermanMedalManager.IRON_CROSS_1, 6, 1);
		awardMedal(GermanMedalManager.ORDER_HOUSE_HOHENZOLLERN, 13, 1);
		awardMedal(GermanMedalManager.POUR_LE_MERIT, 20, 1);
		awardMedal(GermanMedalManager.ORDER_RED_EAGLE, 70, 1);
		
		awardMedal(GermanMedalManager.S_WAR_MERIT_CROSS, 4, 1);
		awardMedal(GermanMedalManager.S_ORDER_ALBERT, 8, 1);
		awardMedal(GermanMedalManager.S_MILITARY_ORDER_ST_HENRY, 30, 1);
	}

    @Test
    public void testIronCrossFirstClassFail () throws PWCGException
    {            
        Mockito.when(player.getPlayerRegion()).thenReturn(SquadronMember.PRUSSIA);
        awardMedal(GermanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(GermanMedalManager.IRON_CROSS_2, 1, 1);

        makeVictories(1);
        Medal medal = medalManager.award(campaign, player, service, 3);
        assert (medal == null);
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Axis\\" + medal.getMedalImage();
	        ImageIcon medalIcon = ImageCache.getInstance().getImageIcon(medalPath);
	        assert (medalIcon != null);
    	}
    }
}
