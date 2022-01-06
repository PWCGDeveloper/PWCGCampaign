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
import pwcg.product.fc.country.FCServiceManager;
import pwcg.product.fc.medals.BelgianMedalManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoFBelgianMedalManagerTest extends MedalManagerTestBase
{
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        super.setupBase();
        Mockito.when(country.isCountry(Country.BELGIUM)).thenReturn(true);
        ICountry country = CountryFactory.makeCountryByCountry(Country.BELGIUM);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }
    
    @Test
    public void testBelgianMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(FCServiceManager.AVIATION_MILITAIRE_BELGE, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

        awardMedal(BelgianMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(BelgianMedalManager.MILITARY_MEDAL, 2, 1);
		awardMedal(BelgianMedalManager.BEL_CROIX_DE_GUERRE, 3, 1);
		awardMedal(BelgianMedalManager.CROIX_DE_GUERRE, 5, 1);
		awardMedal(BelgianMedalManager.CROIX_DE_GUERRE_BRONZE_STAR, 7, 1);
		awardMedal(BelgianMedalManager.BEL_ORDRE_DE_LA_COURONNE, 8, 1);
		awardMedal(BelgianMedalManager.MEDAILLE_DE_HONNEUR, 10, 1);
		awardMedal(BelgianMedalManager.BEL_ORDRE_DE_LEOPOLD, 12, 1);
		awardMedal(BelgianMedalManager.LEGION_DE_HONNEUR, 30, 1);
    }

    @Test
    public void testMedalFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(FCServiceManager.USAS, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

        awardMedal(BelgianMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(BelgianMedalManager.MILITARY_MEDAL, 2, 1);
		awardMedal(BelgianMedalManager.BEL_CROIX_DE_GUERRE, 3, 1);
		awardMedal(BelgianMedalManager.CROIX_DE_GUERRE, 5, 1);
		awardMedal(BelgianMedalManager.CROIX_DE_GUERRE_BRONZE_STAR, 7, 1);
		awardMedal(BelgianMedalManager.BEL_ORDRE_DE_LA_COURONNE, 8, 1);
		awardMedal(BelgianMedalManager.MEDAILLE_DE_HONNEUR, 10, 1);
		awardMedal(BelgianMedalManager.BEL_ORDRE_DE_LEOPOLD, 12, 1);

    	makeVictories(29);
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
