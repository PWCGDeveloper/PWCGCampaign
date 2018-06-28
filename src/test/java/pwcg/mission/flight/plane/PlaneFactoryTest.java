package pwcg.mission.flight.plane;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

@RunWith(MockitoJUnitRunner.class)
public class PlaneFactoryTest
{
    Campaign campaign;
    
    @Mock
    Flight flight;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(CampaignCacheBoS.KG53_PROFILE);
    }

    @Test
    public void testPlayerPlaneGeneration() throws PWCGException
    {
        Mockito.when(flight.isVirtual()).thenReturn(false);

        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        PlaneFactory planeFactory = new PlaneFactory(campaign, squadron, flight);
        List<Plane> assignedPlanes = planeFactory.createPlanesForFlight(4);
        
        SquadronMember player = campaign.getPlayer();        
        boolean playerFound = false;
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId());        
        for (Plane plane : assignedPlanes)
        {
            assert(squadronPersonnel.isActiveSquadronMember(plane.getPilot().getSerialNumber()));
            if (plane.getPilot().getSerialNumber() == player.getSerialNumber())
            {
                playerFound = true;
            }
        }

        assert(playerFound);
    }

    @Test
    public void testAiPlaneGeneration() throws PWCGException
    {
        Mockito.when(flight.isVirtual()).thenReturn(true);

        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(20111052);
        PlaneFactory planeFactory = new PlaneFactory(campaign, squadron, flight);
        List<Plane> assignedPlanes = planeFactory.createPlanesForFlight(4);
        
        SquadronMember player = campaign.getPlayer();        
        boolean playerFound = false;
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId());        
        for (Plane plane : assignedPlanes)
        {
            assert(squadronPersonnel.isActiveSquadronMember(plane.getPilot().getSerialNumber()));
            if (plane.getPilot().getSerialNumber() == player.getSerialNumber())
            {
                playerFound = true;
            }
        }

        assert(!playerFound);
    }

}
