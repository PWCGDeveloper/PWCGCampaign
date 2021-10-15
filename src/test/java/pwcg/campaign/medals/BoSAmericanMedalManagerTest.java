package pwcg.campaign.medals;

import javax.swing.ImageIcon;

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
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.product.bos.medals.AmericanMedalManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BoSAmericanMedalManagerTest extends MedalManagerTestBase
{
	
    @BeforeEach
    public void setupTest() throws PWCGException
    {
    	PWCGContext.setProduct(PWCGProduct.BOS);
        super.setupBase();
        ICountry country = CountryFactory.makeCountryByCountry(Country.USA);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }

    @Test
    public void testAmericanMedals () throws PWCGException
    {               
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19441001"));
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.USAAF, campaign.getDate());

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
	        ImageIcon medalIcon = ImageIconCache.getInstance().getImageIcon(medalPath);
	        assert (medalIcon != null);
    	}
    }
}
