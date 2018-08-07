package pwcg.mission.flight.crew;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

public class FlightCrewBuilderTest 
{
    Campaign campaign;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(CampaignCacheBoS.KG53_PROFILE);
    }

    @Test
    public void testPlayerFlightGeneration() throws PWCGException
    {
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(campaign, squadron);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        List<SquadronMember> players = campaign.getPlayers();
        boolean playerFound = false;
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId());        
        for (SquadronMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveSquadronMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == players.get(0).getSerialNumber())
            {
                playerFound = true;
            }
        }

        assert(playerFound);
    }

    @Test
    public void testAiFlightGeneration() throws PWCGException
    {
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(20111052);
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(campaign, squadron);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        List<SquadronMember> players = campaign.getPlayers();
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId());        
        boolean playerFound = false;
        for (SquadronMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveSquadronMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == players.get(0).getSerialNumber())
            {
                playerFound = true;
            }
        }
        assert(!playerFound);
    }
}
