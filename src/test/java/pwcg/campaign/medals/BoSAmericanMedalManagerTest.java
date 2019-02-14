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
import pwcg.campaign.ww2.country.BoSServiceManager;
import pwcg.campaign.ww2.medals.AmericanMedalManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.utils.ContextSpecificImages;

@RunWith(MockitoJUnitRunner.class)
public class BoSAmericanMedalManagerTest extends MedalManagerTestBase
{
	
    @Before
    public void setup() throws PWCGException
    {
    	PWCGContextManager.setRoF(false);
        super.setup();
        Mockito.when(country.isCountry(Country.USA)).thenReturn(true);
        medalManager = MedalManagerFactory.createMedalManager(campaign);
        medals.clear();
    }

    @Test
    public void testAmericanMedals () throws PWCGException
    {               
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19441001"));
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.USAAF, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(AmericanMedalManager.PILOTS_BADGE, 0, 0);
        awardMedal(AmericanMedalManager.BRONZE_STAR, 3, 1);
        awardMedal(AmericanMedalManager.DISTINGUISHED_FLYING_CROSS, 6, 1);
        awardMedal(AmericanMedalManager.SILVER_STAR, 15, 1);
        awardMedal(AmericanMedalManager.DISTINGUISHED_SERVICE_CROSS, 20, 1);
        awardMedal(AmericanMedalManager.MEDAL_OF_HONOR, 20, 3);
    }

    @Test
    public void testAmericanMedalsAlternateMoHAward1 () throws PWCGException
    {               
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19441001"));
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.RAF, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(AmericanMedalManager.PILOTS_BADGE, 0, 0);
        awardMedal(AmericanMedalManager.BRONZE_STAR, 3, 1);
        awardMedal(AmericanMedalManager.DISTINGUISHED_FLYING_CROSS, 6, 1);
        awardMedal(AmericanMedalManager.SILVER_STAR, 15, 1);
        awardMedal(AmericanMedalManager.DISTINGUISHED_SERVICE_CROSS, 20, 1);
        awardMedal(AmericanMedalManager.MEDAL_OF_HONOR, 30, 2);
    }

    @Test
    public void testAmericanMedalsAlternateMoHAward2 () throws PWCGException
    {               
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19441001"));
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.RAF, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(AmericanMedalManager.PILOTS_BADGE, 0, 0);
        awardMedal(AmericanMedalManager.BRONZE_STAR, 3, 1);
        awardMedal(AmericanMedalManager.DISTINGUISHED_FLYING_CROSS, 6, 1);
        awardMedal(AmericanMedalManager.SILVER_STAR, 15, 1);
        awardMedal(AmericanMedalManager.DISTINGUISHED_SERVICE_CROSS, 20, 1);
        awardMedal(AmericanMedalManager.MEDAL_OF_HONOR, 35, 1);
    }

    @Test
    public void testMoHFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19441001"));
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.RAF, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(AmericanMedalManager.PILOTS_BADGE, 0, 0);
        awardMedal(AmericanMedalManager.BRONZE_STAR, 3, 1);
        awardMedal(AmericanMedalManager.DISTINGUISHED_FLYING_CROSS, 6, 1);
        awardMedal(AmericanMedalManager.SILVER_STAR, 15, 1);
        awardMedal(AmericanMedalManager.DISTINGUISHED_SERVICE_CROSS, 20, 1);

    	makeVictories(32);
        Medal medal = medalManager.award(campaign, player, service, 1);
        assert (medal == null);
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Allied\\" + medal.getMedalImage();
	        System.out.println(medalPath);
	        ImageIcon medalIcon = ImageCache.getInstance().getImageIcon(medalPath);
	        assert (medalIcon != null);
    	}
    }
}
