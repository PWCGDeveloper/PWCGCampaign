package pwcg.campaign.medals;

import java.util.Date;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.ww1.country.RoFServiceManager;
import pwcg.campaign.ww1.medals.AmericanMedalManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.utils.ContextSpecificImages;

@RunWith(MockitoJUnitRunner.class)
public class RoFAmericanMedalManagerTest extends MedalManagerTestBase
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        super.setup();
        Mockito.when(country.isCountry(Country.USA)).thenReturn(true);
        medalManager = MedalManagerFactory.createMedalManager(campaign);
        medals.clear();
    }
    
    @Test
    public void testAmericanMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.USAS, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(AmericanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(AmericanMedalManager.DISTINGUISHED_SERVICE_MEDAL, 5, 1);
		awardMedal(AmericanMedalManager.DISTINGUISHED_FLYING_CROSS, 10, 1);
		awardMedal(AmericanMedalManager.MEDAL_OF_HONOR, 15, 3);
    }

    @Test
    public void testMoHFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.USAS, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(AmericanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(AmericanMedalManager.DISTINGUISHED_SERVICE_MEDAL, 5, 1);
		awardMedal(AmericanMedalManager.DISTINGUISHED_FLYING_CROSS, 10, 1);

    	makeVictories(15);
        Medal medal = medalManager.award(campaign, player, service, 2);
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
