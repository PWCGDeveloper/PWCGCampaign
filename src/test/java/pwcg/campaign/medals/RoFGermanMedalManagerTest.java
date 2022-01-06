package pwcg.campaign.medals;

import javax.swing.ImageIcon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.product.bos.medals.GermanMedalManager;
import pwcg.product.fc.country.FCServiceManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoFGermanMedalManagerTest extends MedalManagerTestBase
{
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        super.setupBase();
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE, DateUtils.getDateYYYYMMDD("19171001"));

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
        Mockito.when(player.getPlayerRegion()).thenReturn(CrewMember.BAVARIA);

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
        Mockito.when(player.getPlayerRegion()).thenReturn(CrewMember.PRUSSIA);

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
        Mockito.when(player.getPlayerRegion()).thenReturn(CrewMember.WURTTEMBURG);

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
        Mockito.when(player.getPlayerRegion()).thenReturn(CrewMember.SAXONY);

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
        Mockito.when(player.getPlayerRegion()).thenReturn(CrewMember.PRUSSIA);
        awardMedal(GermanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(GermanMedalManager.IRON_CROSS_2, 1, 1);

        makeVictories(1);
        Medal medal = medalManager.award(campaign, player, service, 3);
        Assertions.assertTrue (medal == null);
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Axis\\" + medal.getMedalImage();
	        ImageIcon medalIcon = ImageIconCache.getInstance().getImageIcon(medalPath);
	        Assertions.assertTrue (medalIcon != null);
    	}
    }
}
