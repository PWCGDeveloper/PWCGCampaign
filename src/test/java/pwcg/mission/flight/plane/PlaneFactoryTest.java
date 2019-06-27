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
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PlaneFactoryTest
{
    Campaign campaign;
    Mission mission;
    @Mock Flight flight;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.KG53_PROFILE);
        
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate(FlightTypes.BOMB);
    }

    @Test
    public void testPlayerPlaneGeneration() throws PWCGException
    {
        Mockito.when(flight.isVirtual()).thenReturn(false);
        
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(SquadrontTestProfile.KG53_PROFILE.getSquadronId());
        FlightInformation flightInformation = FlightInformationFactory.buildPlayerFlightInformation(squadron, mission, FlightTypes.BOMB);

        PlaneMCUFactory planeFactory = new PlaneMCUFactory(flightInformation);
        List<PlaneMCU> assignedPlanes = planeFactory.createPlanesForFlight(4);
        
        boolean playerFound = false;
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadrontTestProfile.KG53_PROFILE.getSquadronId());        
        int callnum = 1;
        for (PlaneMCU plane : assignedPlanes)
        {
            assert(squadronPersonnel.isActiveSquadronMember(plane.getPilot().getSerialNumber()));
            assert(plane.getCallsign() == Callsign.SEAGULL);
            assert(plane.getCallnum() == callnum++);
            List<SquadronMember> players = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList();
            for (SquadronMember player : players)
            {
                if (plane.getPilot().getSerialNumber() == player.getSerialNumber())
                {
                    playerFound = true;
                }
            }
        }

        assert(playerFound);
    }

    @Test
    public void testAiPlaneGeneration() throws PWCGException
    {
        Mockito.when(flight.isVirtual()).thenReturn(true);
        
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(20111052);
        FlightInformation flightInformation = FlightInformationFactory.buildAiFlightInformation(squadron, mission, FlightTypes.BOMB);

        PlaneMCUFactory planeFactory = new PlaneMCUFactory(flightInformation);
        List<PlaneMCU> assignedPlanes = planeFactory.createPlanesForFlight(4);
        
        List<SquadronMember> players = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList();
        boolean playerFound = false;
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId());        
        int callnum = 1;
        for (PlaneMCU plane : assignedPlanes)
        {
            assert(squadronPersonnel.isActiveSquadronMember(plane.getPilot().getSerialNumber()));
            assert(plane.getCallsign() == Callsign.ROOK);
            assert(plane.getCallnum() == callnum++);
            if (plane.getPilot().getSerialNumber() == players.get(0).getSerialNumber())
            {
                playerFound = true;
            }
        }

        assert(!playerFound);
    }

}
