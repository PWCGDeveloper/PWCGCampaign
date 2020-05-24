package pwcg.campaign.group;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class AirfieldManagerTest
{
    Campaign campaign;
    
    @Mock
    IFlight flight;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
    }

	@Test
	public void airfieldValidityCheckFranceTest() throws PWCGException 
	{
        AirfieldManager airfieldManager = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.ARRAS_MAP).getAirfieldManager();
        for (IAirfield airfield : airfieldManager.getAllAirfields().values())
        {
            assert (airfield.getTakeoffLocation() != null);
            assert (airfield.getLandingLocation() != null);
        }
	}

}
