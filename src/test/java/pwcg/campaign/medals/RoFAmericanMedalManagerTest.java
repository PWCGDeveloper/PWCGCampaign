package pwcg.campaign.medals;

import java.util.Date;

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
import pwcg.product.bos.medals.AmericanMedalManager;
import pwcg.product.fc.country.FCServiceManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoFAmericanMedalManagerTest extends MedalManagerTestBase
{
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        super.setupBase();
        Mockito.when(country.isCountry(Country.USA)).thenReturn(true);
        ICountry country = CountryFactory.makeCountryByCountry(Country.USA);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }
    
    @Test
    public void testAmericanMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(FCServiceManager.USAS, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

        awardMedal(AmericanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(AmericanMedalManager.DISTINGUISHED_SERVICE_MEDAL, 5, 1);
		awardMedal(AmericanMedalManager.DISTINGUISHED_FLYING_CROSS, 10, 1);
		awardMedal(AmericanMedalManager.MEDAL_OF_HONOR, 15, 3);
    }

    @Test
    public void testMoHFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(FCServiceManager.USAS, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

        awardMedal(AmericanMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(AmericanMedalManager.DISTINGUISHED_SERVICE_MEDAL, 5, 1);
		awardMedal(AmericanMedalManager.DISTINGUISHED_FLYING_CROSS, 10, 1);

    	makeVictories(15);
        Medal medal = medalManager.award(campaign, player, service, 2);
        Assertions.assertTrue (medal == null);
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Allied\\" + medal.getMedalImage();
	        ImageIcon medalIcon = ImageIconCache.getInstance().getImageIcon(medalPath);
	        Assertions.assertTrue (medalIcon != null);
    	}
    }
}
