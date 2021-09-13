package pwcg.mission.flight.packages;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.PwcgTestBase;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class RaiderAttackPackageTest extends PwcgTestBase
{
    public RaiderAttackPackageTest()
    {
        super (PWCGProduct.BOS);
    }

    @Before
    public void setup() throws PWCGException
    {
    }

    @Test
    public void raiderAttackAirfieldTargetTest() throws PWCGException
    {
        TestDriver.getInstance().enableTestDriver();
        TestDriver.getInstance().setTestPlayerTacticalTargetType(TargetType.TARGET_AIRFIELD);
        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        MissionFlights missionFlights = buildFlight(campaign);
        IFlight playerFlight = missionFlights.getPlayerFlights().get(0);
        assert(playerFlight.getTargetDefinition().getTargetType() == TargetType.TARGET_AIRFIELD);

        List<IFlight> escortFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.PLAYER_ESCORT);
        assert(escortFlights.size() == 0);

        verifyLowAltitude(playerFlight);
        verifyProximityToTarget(playerFlight);

        TestDriver.getInstance().reset();
    }

    private MissionFlights buildFlight(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);

        Squadron playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(FlightTypes.RAID, playerSquadron);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(150000,  0, 150000), 100000);

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);

        campaign.setCurrentMission(mission);
        return mission.getMissionFlights();
    }

    private void verifyProximityToTarget(IFlight flight) throws PWCGException
    {
        MissionPointAttackSet attackMissionPoint = (MissionPointAttackSet)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ATTACK);
        Coordinate attackPosition = attackMissionPoint.getAttackSequence().getAttackAreaMcu().getPosition();
        System.out.println("Attack Position at " + attackPosition);

        boolean groundAttackCloseToTarget = false;
        for (Airfield airfield : flight.getMission().getFieldsForPatrol())
        {
            double distanceFromAirfield = MathUtils.calcDist(attackPosition, airfield.getPosition());
            if (distanceFromAirfield < 5000)
            {
                groundAttackCloseToTarget = true;
                System.out.println("CLOSE TO " + airfield.getName() + " " + airfield.determineCountry().getCountryName() + " distance is " + distanceFromAirfield);
            }
            else
            {
                System.out.println("Not close to " + airfield.getName() + " " + airfield.determineCountry().getCountryName() + " distance is " + distanceFromAirfield);
            }
        }
        assert (groundAttackCloseToTarget == true);
    }

    private void verifyLowAltitude(IFlight flight) throws PWCGException
    {
        for (MissionPoint missionPoint : flight.getWaypointPackage().getFlightMissionPoints())
        {
            if (missionPoint.getAction() == WaypointAction.WP_ACTION_TARGET_FINAL || missionPoint.getAction() == WaypointAction.WP_ACTION_ATTACK)
            {
                assert (missionPoint.getPosition().getYPos() < 1000);
            }
            else
            {
                assert (missionPoint.getPosition().getYPos() < 700);
            }
        }
    }
}
