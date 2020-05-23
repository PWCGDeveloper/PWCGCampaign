package pwcg.mission.flight.packages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBorderBuilder;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.infantry.BalloonUnit;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

public class BalloonDefensePackageTest
{
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
    }

    @Test
    public void balloonBustFlightTest() throws PWCGException
    {
        IFlight flight = buildFlight();
        verifyBalloonPosition(flight);
   }

    private IFlight buildFlight() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = new Mission(campaign, MissionProfile.DAY_TACTICAL_MISSION, participatingPlayers, missionBorders);
        mission.generate(Arrays.asList(FlightTypes.BALLOON_DEFENSE));
        campaign.setCurrentMission(mission);

        return mission.getMissionFlightBuilder().getPlayerFlights().get(0);
    }

    private void verifyBalloonPosition(IFlight flight) throws PWCGException
    {
        List<BalloonUnit> balloons = getBalloonUnits(flight);

        boolean closeToBalloon = false;
        for (BalloonUnit balloonUnit : balloons)
        {
            for (McuWaypoint balloonDefenseWaypoint : flight.getWaypointPackage().getAllWaypoints())
            {
                if (balloonDefenseWaypoint.getWaypointType() == WaypointType.PATROL_WAYPOINT)
                {
                    double distanceFromBalloon = MathUtils.calcDist(balloonDefenseWaypoint.getPosition(), balloonUnit.getPosition());
                    if (distanceFromBalloon < 8000)
                    {
                        closeToBalloon = true;
                    }
                }
            }
        }
        assert(closeToBalloon);
    }
    

    private List<BalloonUnit> getBalloonUnits(IFlight flight) throws PWCGException
    {
        List<BalloonUnit> balloons = new ArrayList<>();
        for (IGroundUnitCollection groundUnitCollection : flight.getMission().getMissionGroundUnitBuilder().getBalloonUnits())
        {
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                if (groundUnit instanceof BalloonUnit)
                {
                    if (flight.getSquadron().getCountry().getSide() == groundUnit.getCountry().getSide().getOppositeSide())
                    {
                        BalloonUnit balloonUnit = (BalloonUnit) groundUnit;
                        balloons.add(balloonUnit);
                    }
                }
            }
        }
        assert(balloons.size() > 0);
        return balloons;
    }

}
