package pwcg.mission.flight.crew;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class FlightCrewBuilderTest 
{
    Campaign campaign;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.KG53_PROFILE);
    }

    @Test
    public void testPlayerFlightGeneration() throws PWCGException
    {
    	FlightInformation flightInformation = new FlightInformation(campaign, campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList());
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(SquadrontTestProfile.KG53_PROFILE.getSquadronId());
        flightInformation.setSquadron(squadron);
        
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        List<SquadronMember> players = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList();
        boolean playerFound = false;
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadrontTestProfile.KG53_PROFILE.getSquadronId());        
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
    	FlightInformation flightInformation = new FlightInformation(campaign, new ArrayList<>());
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(20111052);
        flightInformation.setSquadron(squadron);

        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        List<SquadronMember> players = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList();
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
