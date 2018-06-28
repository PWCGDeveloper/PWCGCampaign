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
import pwcg.campaign.ww1.medals.FrenchMedalManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.utils.ContextSpecificImages;

@RunWith(MockitoJUnitRunner.class)
public class RoFFrenchMedalManagerTest extends MedalManagerTestBase
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        super.setup();
        Mockito.when(country.isCountry(Country.FRANCE)).thenReturn(true);
        medalManager = MedalManagerFactory.createMedalManager(campaign);
        medals.clear();
    }
    

    @Test
    public void testFrenchMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.LAVIATION_MILITAIRE, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(FrenchMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(FrenchMedalManager.MILITARY_MEDAL, 2, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE, 3, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_STAR, 5, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_STAR, 7, 1);
		awardMedal(FrenchMedalManager.MEDAILLE_DE_HONNEUR, 9, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_GILT_STAR, 11, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_PALM, 14, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_PALM, 18, 1);
		awardMedal(FrenchMedalManager.LEGION_DE_HONNEUR, 25, 1);
    }

    @Test
    public void testMMMFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.LAVIATION_MARINE, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(FrenchMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(FrenchMedalManager.MILITARY_MEDAL, 2, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE, 3, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_STAR, 5, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_STAR, 7, 1);
		awardMedal(FrenchMedalManager.MEDAILLE_DE_HONNEUR, 9, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_GILT_STAR, 11, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_BRONZE_PALM, 14, 1);
		awardMedal(FrenchMedalManager.CROIX_DE_GUERRE_SILVER_PALM, 18, 1);

    	makeVictories(24);
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
