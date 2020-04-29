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
import pwcg.product.fc.country.FCServiceManager;
import pwcg.product.fc.medals.FrenchMedalManager;

@RunWith(MockitoJUnitRunner.Silent.class) 
public class RoFFrenchMedalManagerTest extends MedalManagerTestBase
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        super.setup();
        Mockito.when(country.isCountry(Country.FRANCE)).thenReturn(true);
        ICountry country = CountryFactory.makeCountryByCountry(Country.FRANCE);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }
    

    @Test
    public void testFrenchMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19180801"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(FCServiceManager.LAVIATION_MILITAIRE, campaign.getDate());
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);

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
