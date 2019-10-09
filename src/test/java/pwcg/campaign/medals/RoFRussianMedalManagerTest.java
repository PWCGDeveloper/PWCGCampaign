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
import pwcg.product.rof.medals.RussianMedalManager;

@RunWith(MockitoJUnitRunner.class)
public class RoFRussianMedalManagerTest extends MedalManagerTestBase
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        super.setup();
        Mockito.when(country.isCountry(Country.RUSSIA)).thenReturn(true);
        ICountry country = CountryFactory.makeCountryByCountry(Country.RUSSIA);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }

    @Test
    public void testRussianMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19160801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.RUSSIAN_AIR_SERVICE, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(RussianMedalManager.R_PILOTS_BADGE, 0, 0);
		awardMedal(RussianMedalManager.R_ORDER_ST_GEORGE, 3, 1);
		awardMedal(RussianMedalManager.R_ORDER_ST_VLADIMIR, 7, 1);
		awardMedal(RussianMedalManager.R_ORDER_ST_STANISLAW, 18, 1);
		awardMedal(RussianMedalManager.R_ORDER_ST_ANNE, 10, 2);
    }

    @Test
    public void testMMMFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19160801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.RUSSIAN_AIR_SERVICE, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(RussianMedalManager.R_PILOTS_BADGE, 0, 0);
		awardMedal(RussianMedalManager.R_ORDER_ST_GEORGE, 3, 1);
		awardMedal(RussianMedalManager.R_ORDER_ST_VLADIMIR, 7, 1);
		awardMedal(RussianMedalManager.R_ORDER_ST_STANISLAW, 18, 1);

    	makeVictories(20);
        Medal medal = medalManager.award(campaign, player, service, 1);
        assert (medal == null);
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Allied\\" + medal.getMedalImage();
	        ImageIcon medalIcon = ImageCache.getInstance().getImageIcon(medalPath);
	        assert (medalIcon != null);
    	}
    }
}
