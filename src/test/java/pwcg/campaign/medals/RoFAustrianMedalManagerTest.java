package pwcg.campaign.medals;

import java.util.Date;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.product.rof.country.RoFServiceManager;
import pwcg.product.rof.medals.AustrianMedalManager;

@RunWith(MockitoJUnitRunner.class)
public class RoFAustrianMedalManagerTest extends MedalManagerTestBase
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        super.setup();
        Mockito.when(country.isCountry(Country.AUSTRIA)).thenReturn(true);
        ICountry country = CountryFactory.makeCountryByCountry(Country.AUSTRIA);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }
    
    @Test
    public void testAustrianMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.AUSTRO_HUNGARIAN_AIR_SERVICE, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(AustrianMedalManager.A_PILOTS_BADGE, 0, 0);
		awardMedal(AustrianMedalManager.A_ORDER_IRON_CROWN, 3, 1);
		awardMedal(AustrianMedalManager.A_ORDER_LEOPOLD, 8, 1);
		awardMedal(AustrianMedalManager.IRON_CROSS_2, 10, 1);
		awardMedal(AustrianMedalManager.A_MEDAL_FOR_BRAVERY, 12, 1);
		awardMedal(AustrianMedalManager.A_MILITARY_MERIT_MEDAL, 20, 1);
    }

    @Test
    public void testMMMFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.AUSTRO_HUNGARIAN_AIR_SERVICE, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(AustrianMedalManager.A_PILOTS_BADGE, 0, 0);
		awardMedal(AustrianMedalManager.A_ORDER_IRON_CROWN, 3, 1);
		awardMedal(AustrianMedalManager.A_ORDER_LEOPOLD, 8, 1);
		awardMedal(AustrianMedalManager.IRON_CROSS_2, 10, 1);
		awardMedal(AustrianMedalManager.A_MEDAL_FOR_BRAVERY, 12, 1);

    	makeVictories(19);
        Medal medal = medalManager.award(campaign, player, service, 2);
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
