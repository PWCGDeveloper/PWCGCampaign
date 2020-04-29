package pwcg.campaign.medals;

import java.util.Date;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.dialogs.ImageIconCache;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.product.bos.medals.RussianMedalManager;

@RunWith(MockitoJUnitRunner.Silent.class) 
public class BoSRussianMedalManagerTest extends MedalManagerTestBase
{
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        super.setup();
        Mockito.when(country.isCountry(Country.RUSSIA)).thenReturn(true);
        ICountry country = CountryFactory.makeCountryByCountry(Country.RUSSIA);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }


    @Test
    public void testRussianMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.VVS, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

        awardMedal(RussianMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(RussianMedalManager.ORDER_RED_STAR, 2, 1);
		awardMedal(RussianMedalManager.ORDER_OF_GLORY, 5, 1);
		awardMedal(RussianMedalManager.ORDER_PATRIOTIC_WAR_2, 6, 1);
		awardMedal(RussianMedalManager.ORDER_PATRIOTIC_WAR_1, 15, 1);
		awardMedal(RussianMedalManager.ORDER_RED_BANNER, 20, 2);
		awardMedal(RussianMedalManager.HERO_SOVIET_UNION, 30, 2);
    }

    @Test
    public void testMMMFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.VVS, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

        awardMedal(RussianMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(RussianMedalManager.ORDER_RED_STAR, 2, 1);
		awardMedal(RussianMedalManager.ORDER_OF_GLORY, 5, 1);
		awardMedal(RussianMedalManager.ORDER_PATRIOTIC_WAR_2, 6, 1);
		awardMedal(RussianMedalManager.ORDER_PATRIOTIC_WAR_1, 15, 1);
		awardMedal(RussianMedalManager.ORDER_RED_BANNER, 20, 2);

    	makeVictories(30);
        Medal medal = medalManager.award(campaign, player, service, 1);
        assert (medal == null);
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Allied\\" + medal.getMedalImage();
	        ImageIcon medalIcon = ImageIconCache.getInstance().getImageIcon(medalPath);
	        assert (medalIcon != null);
    	}
    }
}
