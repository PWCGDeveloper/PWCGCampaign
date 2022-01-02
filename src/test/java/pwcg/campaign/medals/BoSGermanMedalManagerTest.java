package pwcg.campaign.medals;

import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.product.bos.medals.GermanMedalManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BoSGermanMedalManagerTest extends MedalManagerTestBase
{
	
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        super.setupBase();
        Mockito.when(country.isCountry(Country.GERMANY)).thenReturn(true);
        ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }

    @Test
    public void testGermanMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.LUFTWAFFE, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

        awardMedal(GermanMedalManager.PILOTS_BADGE, 0, 0);
        awardMedal(GermanMedalManager.IRON_CROSS_2, 2, 1);
		awardMedal(GermanMedalManager.IRON_CROSS_1, 10, 1);
		awardMedal(GermanMedalManager.GERMAN_CROSS_GOLD, 25, 1);
		awardMedal(GermanMedalManager.KNIGHTS_CROSS, 40, 1);
		awardMedal(GermanMedalManager.KNIGHTS_CROSS_OAK_LEAVES, 100, 2);
		awardMedal(GermanMedalManager.KNIGHTS_CROSS_SWORDS, 150, 2);
		awardMedal(GermanMedalManager.KNIGHTS_CROSS_DIAMONDS, 200, 2);
    }

    @Test
    public void testKCDiamondsFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.LUFTWAFFE, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

        awardMedal(GermanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(GermanMedalManager.IRON_CROSS_2, 2, 1);
		awardMedal(GermanMedalManager.IRON_CROSS_1, 10, 1);
		awardMedal(GermanMedalManager.GERMAN_CROSS_GOLD, 25, 1);
		awardMedal(GermanMedalManager.KNIGHTS_CROSS, 40, 1);
		awardMedal(GermanMedalManager.KNIGHTS_CROSS_OAK_LEAVES, 100, 2);
		awardMedal(GermanMedalManager.KNIGHTS_CROSS_SWORDS, 150, 2);

    	makeVictories(199);
        Medal medal = medalManager.award(campaign, player, service, 1);
        Assertions.assertTrue (medal == null);
    }



    @Test
    public void testAwardConsolidation () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.LUFTWAFFE, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

        awardMedal(GermanMedalManager.PILOTS_BADGE, 0, 0);
        awardMedal(GermanMedalManager.IRON_CROSS_2, 2, 1);
        awardMedal(GermanMedalManager.IRON_CROSS_1, 10, 1);
        awardWoundedAward(player, service);
        awardMedal(GermanMedalManager.GERMAN_CROSS_GOLD, 25, 1);
        awardMedal(GermanMedalManager.KNIGHTS_CROSS, 40, 1);
        awardWoundedAward(player, service);
        awardMedal(GermanMedalManager.KNIGHTS_CROSS_OAK_LEAVES, 100, 2);
        awardMedal(GermanMedalManager.KNIGHTS_CROSS_SWORDS, 150, 2);
        awardMedal(GermanMedalManager.KNIGHTS_CROSS_DIAMONDS, 200, 2);

        List<Medal> highestOrderMedals = medalManager.getMedalsWithHighestOrderOnly(player.getMedals());
        Assertions.assertEquals (6, highestOrderMedals.size());
        Assertions.assertEquals ("CrewMembers Badge", highestOrderMedals.get(0).getMedalName());
        Assertions.assertEquals ("Iron Cross 2nd Class", highestOrderMedals.get(1).getMedalName());
        Assertions.assertEquals ("Iron Cross 1st Class", highestOrderMedals.get(2).getMedalName());
        Assertions.assertEquals ("German Cross Gold", highestOrderMedals.get(3).getMedalName());
        Assertions.assertEquals (MedalManager.KNIGHTS_CROSS_NAME + " with Diamonds", highestOrderMedals.get(4).getMedalName());
        Assertions.assertEquals (MedalManager.GERMAN_WOUND_BADGE + "(Silver)", highestOrderMedals.get(5).getMedalName());
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	assert( medalManager.getAllAwardsForService().size() > 0);
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Axis\\" + medal.getMedalImage();
	        ImageIcon medalIcon = ImageIconCache.getInstance().getImageIcon(medalPath);
	        Assertions.assertTrue (medalIcon != null);
    	}
    }
}
