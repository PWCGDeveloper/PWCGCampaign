package pwcg.mission.flight.packages;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBorderBuilder;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.attack.GroundAttackPackage;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class GroundAttackPackageTest
{

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void playerPackageTest() throws PWCGException
    {
        for (int i = 0; i < 10; ++i)
        {
            IFlight flight = buildFlight();
            verifyProximityToTarget(flight);
        }
    }

    private IFlight buildFlight() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(Arrays.asList(FlightTypes.GROUND_ATTACK));

        campaign.setCurrentMission(mission);

        GroundAttackPackage flightPackage = new GroundAttackPackage();
        boolean isPlayerFlight = true;
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.FG_362_PROFILE.getSquadronId());
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, isPlayerFlight);
        IFlight flight = flightPackage.createPackage(flightBuildInformation);
        return flight;
    }

    private void verifyProximityToTarget(IFlight flight) throws PWCGException
    {
        System.out.println("Target type is " + flight.getTargetDefinition().getTargetCategory());

        MissionPointAttackSet attackMissionPoint = (MissionPointAttackSet)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ATTACK);
        boolean groundAttackCloseToTarget = false;
        for (IGroundUnitCollection groundUnitCollection : flight.getMission().getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                if (!groundUnit.getVehicleClass().getName().contains("AA"))
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
        }
        assert (groundAttackCloseToTarget == true);
    }
}
