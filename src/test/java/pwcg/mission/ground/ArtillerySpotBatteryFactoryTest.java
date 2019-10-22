package pwcg.mission.ground;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;

@RunWith(MockitoJUnitRunner.class)
public class ArtillerySpotBatteryFactoryTest
{
    
    @Mock 
    private Campaign campaign;

    @Mock
    private Squadron squadron;
    
    @Mock 
    private ConfigManagerCampaign configManager;


    private Coordinate myTestPosition = new Coordinate (100000, 0, 100000);
    private ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19171010"));
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(squadron.determineCurrentAirfieldName(DateUtils.getDateYYYYMMDD("19171010"))).thenReturn("Villers Bretonneux");
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
    }

    @Test
    public void createFriendlyArtilleryBatteryForPlayerTest () throws PWCGException 
    {
        ArtillerySpotBatteryFactory artillerySpotBatteryFactory = new ArtillerySpotBatteryFactory(campaign, squadron, new Coordinate (100000, 0, 100000), myTestPosition, country);
        ArtillerySpotArtilleryGroup friendlyArtilleryGroup = artillerySpotBatteryFactory.createFriendlyArtilleryBattery(true);

        assert (friendlyArtilleryGroup.getVehicles().size() > 0);
        assert (friendlyArtilleryGroup.getCountry().getCountry() == Country.GERMANY);
    }
    
    @Test
    public void createFriendlyArtilleryBatteryForAITest () throws PWCGException 
    {
        ArtillerySpotBatteryFactory artillerySpotBatteryFactory = new ArtillerySpotBatteryFactory(campaign, squadron, new Coordinate (100000, 0, 100000), myTestPosition, country);
        ArtillerySpotArtilleryGroup friendlyArtilleryGroup = artillerySpotBatteryFactory.createFriendlyArtilleryBattery(false);

        assert (friendlyArtilleryGroup.getVehicles().size() ==1);
        assert (friendlyArtilleryGroup.getCountry().getCountry() == Country.GERMANY);
    }

}
