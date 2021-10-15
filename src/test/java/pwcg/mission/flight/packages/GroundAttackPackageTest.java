package pwcg.mission.flight.packages;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TargetVicinityValidator;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroundAttackPackageTest
{
    private Campaign campaign;
     
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
    }

    @Test
    public void groundAttackInfantryTargetTest() throws PWCGException
    {
        TestDriver.getInstance().enableTestDriver();
        TestDriver.getInstance().setTestPlayerTacticalTargetType(TargetType.TARGET_INFANTRY);
        
        MissionFlights missionFlights = buildFlight(campaign);
        IFlight playerFlight = missionFlights.getPlayerFlights().get(0);
        assert(playerFlight.getTargetDefinition().getTargetType() == TargetType.TARGET_INFANTRY);
        TargetVicinityValidator.verifyProximityToTargetUnit(playerFlight);

        List<IFlight> escortFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.PLAYER_ESCORT);
        assert(escortFlights.size() == 1);
        
        assert(playerFlight.getAssociatedFlight() != null);
        assert(playerFlight.getAssociatedFlight().getFlightInformation().getNecessaryFlightType() == NecessaryFlightType.PLAYER_ESCORT);

        TestDriver.getInstance().reset();
    }

    @Test
    public void groundAttackAirfieldTargetTest() throws PWCGException
    {
        TestDriver.getInstance().enableTestDriver();
        TestDriver.getInstance().setTestPlayerTacticalTargetType(TargetType.TARGET_AIRFIELD);
                
        MissionFlights missionFlights = buildFlight(campaign);
        
        IFlight playerFlight = missionFlights.getPlayerFlights().get(0);
        assert(playerFlight.getTargetDefinition().getTargetType() == TargetType.TARGET_AIRFIELD);
        
        List<IFlight> escortFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.PLAYER_ESCORT);
        assert(escortFlights.size() == 1);
        
        List<IFlight> opposingFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.OPPOSING_FLIGHT);
        assert(opposingFlights.size() == 1);
        
        assert(playerFlight.getAssociatedFlight() != null);
        assert(playerFlight.getAssociatedFlight().getFlightInformation().getNecessaryFlightType() == NecessaryFlightType.PLAYER_ESCORT);

        TargetVicinityValidator.verifyProximityToTargetAirfield(playerFlight);

        TestDriver.getInstance().reset();
    }

    private MissionFlights buildFlight(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);

        Squadron playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(FlightTypes.GROUND_ATTACK, playerSquadron);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(150000,  0, 150000), 100000);

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);

        campaign.setCurrentMission(mission);
        return mission.getMissionFlights();
    }
}
