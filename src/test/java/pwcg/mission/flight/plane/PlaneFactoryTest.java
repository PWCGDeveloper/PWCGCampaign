package pwcg.mission.flight.plane;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlaneFactoryTest
{
    private Campaign campaign;    
    private static Mission mission;
    
    @Mock private IFlight flight;
    @Mock private FlightInformation flightInformation;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.BOMB, MissionProfile.DAY_TACTICAL_MISSION);
    }

    @Test
    public void testPlayerPlaneGeneration() throws PWCGException
    {

        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.KG53_PROFILE.getCompanyId());

        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, NecessaryFlightType.PLAYER_FLIGHT);

        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BOMB);

        PlaneMcuFactory planeFactory = new PlaneMcuFactory(flightInformation);
        List<PlaneMcu> assignedPlanes = planeFactory.createPlanesForFlight(4);

        boolean playerFound = false;
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.KG53_PROFILE.getCompanyId());
        int callnum = 1;
        for (PlaneMcu plane : assignedPlanes)
        {
            Assertions.assertTrue (squadronPersonnel.isActiveCrewMember(plane.getCrewMember().getSerialNumber()));
            Assertions.assertTrue (plane.getCallsign() == Callsign.SEAGULL);
            Assertions.assertTrue (plane.getCallnum() == callnum++);
            List<CrewMember> players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();
            for (CrewMember player : players)
            {
                if (plane.getCrewMember().getSerialNumber() == player.getSerialNumber())
                {
                    playerFound = true;
                }
            }
        }

        Assertions.assertTrue (playerFound);
    }

    @Test
    public void testAiPlaneGeneration() throws PWCGException
    {

        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(20111052);

        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, NecessaryFlightType.PLAYER_FLIGHT);

        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BOMB);

        PlaneMcuFactory planeFactory = new PlaneMcuFactory(flightInformation);
        List<PlaneMcu> assignedPlanes = planeFactory.createPlanesForFlight(4);

        List<CrewMember> players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();
        boolean playerFound = false;
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadron.getCompanyId());
        int callnum = 1;
        for (PlaneMcu plane : assignedPlanes)
        {
            Assertions.assertTrue (squadronPersonnel.isActiveCrewMember(plane.getCrewMember().getSerialNumber()));
            Assertions.assertTrue (plane.getCallsign() == Callsign.ROOK);
            Assertions.assertTrue (plane.getCallnum() == callnum++);
            if (plane.getCrewMember().getSerialNumber() == players.get(0).getSerialNumber())
            {
                playerFound = true;
            }
        }

        Assertions.assertTrue (!playerFound);
    }

}
