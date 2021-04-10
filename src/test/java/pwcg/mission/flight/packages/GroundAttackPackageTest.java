package pwcg.mission.flight.packages;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBorderBuilder;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;
import pwcg.testutils.TestMissionFlightTypeBuilder;

public class GroundAttackPackageTest
{

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void groundAttackInfantryTargetTest() throws PWCGException
    {
        TestDriver.getInstance().setEnabled(true);
        TestDriver.getInstance().setTestPlayerTacticalTargetType(TargetType.TARGET_INFANTRY);
        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        IFlight flight = buildFlight(campaign);
        assert(flight.getFlightInformation().getTargetDefinition().getTargetType() == TargetType.TARGET_INFANTRY);
        verifyProximityToTargetUnit(flight);

        assert(flight.getLinkedFlights().getLinkedFlights().size() == 1);

        TestDriver.getInstance().setEnabled(false);
    }

    @Test
    public void groundAttackAirfieldTargetTest() throws PWCGException
    {
        TestDriver.getInstance().setEnabled(true);
        TestDriver.getInstance().setTestPlayerTacticalTargetType(TargetType.TARGET_AIRFIELD);
        
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        IFlight flight = buildFlight(campaign);
        assert(flight.getFlightInformation().getTargetDefinition().getTargetType() == TargetType.TARGET_AIRFIELD);
        verifyProximityToTargetAirfield(flight);
        
        assert(flight.getLinkedFlights().getLinkedFlights().size() == 2);
        
        TestDriver.getInstance().setEnabled(false);
    }

    private IFlight buildFlight(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        MissionSquadronFlightTypes playerFlightTypes = TestMissionFlightTypeBuilder.buildFlightType(campaign, FlightTypes.GROUND_ATTACK);
        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);

        campaign.setCurrentMission(mission);
        return mission.getMissionFlights().getPlayerFlights().get(0);
    }

    private void verifyProximityToTargetUnit(IFlight flight) throws PWCGException
    {
        System.out.println("Target type is " + flight.getFlightInformation().getTargetSearchStartLocation());

        MissionPointAttackSet attackMissionPoint = (MissionPointAttackSet)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ATTACK);
        boolean groundAttackCloseToTarget = false;
        for (GroundUnitCollection groundUnitCollection : flight.getMission().getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                Coordinate attackPosition = attackMissionPoint.getAttackSequence().getAttackAreaMcu().getPosition();
                double distanceFromGroundUnit = MathUtils.calcDist(attackPosition, groundUnit.getPosition());
                if (distanceFromGroundUnit < 5000)
                {
                    groundAttackCloseToTarget = true;
                    System.out.println("CLOSE TO " + groundUnit.getVehicleClass().getName());
                }
                else
                {
                    System.out.println("Not close to " + groundUnit.getVehicleClass().getName());
                }
            }
        }
        assert (groundAttackCloseToTarget == true);
    }
    

    private void verifyProximityToTargetAirfield(IFlight flight) throws PWCGException
    {
        System.out.println("Target type is " + flight.getFlightInformation().getTargetSearchStartLocation());

        MissionPointAttackSet attackMissionPoint = (MissionPointAttackSet)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ATTACK);
        boolean groundAttackCloseToTarget = false;
        for (Airfield airfield : flight.getMission().getMissionAirfieldBuilder().getFieldsForPatrol())
        {
            Coordinate attackPosition = attackMissionPoint.getAttackSequence().getAttackAreaMcu().getPosition();
            double distanceFromAirfield = MathUtils.calcDist(attackPosition, airfield.getPosition());
            if (distanceFromAirfield < 5000)
            {
                groundAttackCloseToTarget = true;
                System.out.println("CLOSE TO " + airfield.getName());
            }
            else
            {
                System.out.println("Not close to " + airfield.getName());
            }
        }
        assert (groundAttackCloseToTarget == true);
    }

}
