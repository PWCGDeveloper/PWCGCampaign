package pwcg.campaign.group;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.options.MissionWeather;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AirfieldManagerTest
{
    private Campaign campaign;
    
    @Mock private Mission mission;
    @Mock private MissionWeather weather;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
        
        Mockito.when(mission.getWeather()).thenReturn(weather);
        Mockito.when(weather.getWindDirection()).thenReturn(90);        
    }

	@Test
	public void airfieldValidityCheckFranceTest() throws PWCGException 
	{
        AirfieldManager airfieldManager = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.ARRAS_MAP).getAirfieldManager();
        for (Airfield airfield : airfieldManager.getAllAirfields().values())
        {
            assert (airfield.getTakeoffLocation(mission) != null);
            assert (airfield.getLandingLocation(mission) != null);
        }
	}

}
