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
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
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
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlaneFactoryWWITest
{
    private Campaign campaign;    
    private static Mission mission;
    
    @Mock private IFlight flight;
    @Mock private FlightInformation flightInformation;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.ESC_2_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.BOMB, MissionProfile.DAY_TACTICAL_MISSION);
    }

    @Test
    public void testGenerateFarman() throws PWCGException
    {

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.ESC_2_PROFILE.getSquadronId());

        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, NecessaryFlightType.PLAYER_FLIGHT);

        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BOMB);

        PlaneMCUFactory planeFactory = new PlaneMCUFactory(flightInformation);
        List<PlaneMcu> assignedPlanes = planeFactory.createPlanesForFlight(4);

        for (PlaneMcu plane : assignedPlanes)
        {
            Assertions.assertTrue (plane.getType().equals("fe2b"));
        }

    }

    @Test
    public void testGenerateDFW() throws PWCGException
    {
        int fa18SquadronId = 401218;
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(fa18SquadronId);

        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, NecessaryFlightType.PLAYER_FLIGHT);

        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BOMB);

        PlaneMCUFactory planeFactory = new PlaneMCUFactory(flightInformation);
        List<PlaneMcu> assignedPlanes = planeFactory.createPlanesForFlight(4);

        for (PlaneMcu plane : assignedPlanes)
        {
            Assertions.assertTrue (plane.getType().equals("dfwc5"));
        }

    }
}
