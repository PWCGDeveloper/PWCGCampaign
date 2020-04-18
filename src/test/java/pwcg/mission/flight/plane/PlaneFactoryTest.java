package pwcg.mission.flight.plane;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PlaneFactoryTest
{
    Campaign campaign;
    Mission mission;
    @Mock
    IFlight flight;
    @Mock
    IFlightInformation flightInformation;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.BOMB, MissionProfile.DAY_TACTICAL_MISSION);
    }

    @Test
    public void testPlayerPlaneGeneration() throws PWCGException
    {

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.KG53_PROFILE.getSquadronId());

        boolean isPlayerFlight = true;
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, isPlayerFlight);

        IFlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BOMB);

        PlaneMCUFactory planeFactory = new PlaneMCUFactory(flightInformation);
        List<PlaneMcu> assignedPlanes = planeFactory.createPlanesForFlight(4);

        boolean playerFound = false;
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.KG53_PROFILE.getSquadronId());
        int callnum = 1;
        for (PlaneMcu plane : assignedPlanes)
        {
            assert (squadronPersonnel.isActiveSquadronMember(plane.getPilot().getSerialNumber()));
            assert (plane.getCallsign() == Callsign.SEAGULL);
            assert (plane.getCallnum() == callnum++);
            List<SquadronMember> players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
            for (SquadronMember player : players)
            {
                if (plane.getPilot().getSerialNumber() == player.getSerialNumber())
                {
                    playerFound = true;
                }
            }
        }

        assert (playerFound);
    }

    @Test
    public void testAiPlaneGeneration() throws PWCGException
    {

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(20111052);

        boolean isPlayerFlight = false;
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, isPlayerFlight);

        IFlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BOMB);

        PlaneMCUFactory planeFactory = new PlaneMCUFactory(flightInformation);
        List<PlaneMcu> assignedPlanes = planeFactory.createPlanesForFlight(4);

        List<SquadronMember> players = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList();
        boolean playerFound = false;
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId());
        int callnum = 1;
        for (PlaneMcu plane : assignedPlanes)
        {
            assert (squadronPersonnel.isActiveSquadronMember(plane.getPilot().getSerialNumber()));
            assert (plane.getCallsign() == Callsign.ROOK);
            assert (plane.getCallnum() == callnum++);
            if (plane.getPilot().getSerialNumber() == players.get(0).getSerialNumber())
            {
                playerFound = true;
            }
        }

        assert (!playerFound);
    }

}
