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
import pwcg.product.bos.medals.ItalianMedalManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BoSItalianMedalManagerTest extends MedalManagerTestBase
{
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        super.setupBase();
        ICountry country = CountryFactory.makeCountryByCountry(Country.ITALY);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }

    @Test
    public void testItalianMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.REGIA_AERONAUTICA, campaign.getDate());

        awardMedal(ItalianMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(ItalianMedalManager.MEDAL_MILITARY_VALOR_BRONZE, 1, 1);
		awardMedal(ItalianMedalManager.MEDAL_MILITARY_VALOR_SILVER, 5, 1);
		awardMedal(ItalianMedalManager.MEDAL_MILITARY_VALOR_GOLD, 13, 1);
		awardMedal(ItalianMedalManager.CROSS_WAR_MERIT, 18, 1);
		awardMedal(ItalianMedalManager.CROSS_MILITARY_VALOR, 25, 1);
    }

    @Test
    public void testMedalFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(BoSServiceManager.REGIA_AERONAUTICA, campaign.getDate());

        awardMedal(ItalianMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(ItalianMedalManager.MEDAL_MILITARY_VALOR_BRONZE, 1, 1);
		awardMedal(ItalianMedalManager.MEDAL_MILITARY_VALOR_SILVER, 5, 1);
		awardMedal(ItalianMedalManager.MEDAL_MILITARY_VALOR_GOLD, 13, 1);
		awardMedal(ItalianMedalManager.CROSS_WAR_MERIT, 18, 1);

    	makeVictories(24);
        Medal medal = medalManager.award(campaign, player, service, 1);
        assert (medal == null);
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Axis\\" + medal.getMedalImage();
	        ImageIcon medalIcon = ImageIconCache.getInstance().getImageIcon(medalPath);
	        assert (medalIcon != null);
    	}
    }
}
