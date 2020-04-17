package pwcg.mission.flight.packages;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Balloon;
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
import pwcg.mission.flight.balloondefense.BalloonDefensePackage;
import pwcg.mission.ground.org.GroundUnitElement;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.infantry.BalloonUnit;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetType;
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
        IGroundUnitCollection balloonUnitCollection = veryBalloonUInitCollection(flight);        
        Coordinate balloonPosition = verifyBalloonPosition(balloonUnitCollection);
        verifyBalloonDefenseIsCloseToBalloon(flight, balloonPosition);        
        for (IFlight opposingFlight : flight.getLinkedFlights().getLinkedFlights())
        {
            verifyBalloonBustIsCloseToBalloon(opposingFlight, balloonPosition);
        }
   }

    private IFlight buildFlight() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox(Arrays.asList(FlightTypes.BALLOON_DEFENSE));

        Mission mission = new Mission(campaign, MissionProfile.DAY_TACTICAL_MISSION, participatingPlayers, missionBorders);
        campaign.setCurrentMission(mission);

        BalloonDefensePackage flightPackage = new BalloonDefensePackage();
        boolean isPlayerFlight = true;
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, isPlayerFlight);
        IFlight flight = flightPackage.createPackage(flightBuildInformation);
        return flight;
    }

    private IGroundUnitCollection veryBalloonUInitCollection(IFlight flight)
    {
        assert(flight.getLinkedGroundUnits().getLinkedGroundUnits().size() == 1);
        IGroundUnitCollection balloonUnitCollection = flight.getLinkedGroundUnits().getLinkedGroundUnits().get(0);
        assert(balloonUnitCollection.getTargetType() == TargetType.TARGET_BALLOON);
        return balloonUnitCollection;
    }

    private Coordinate verifyBalloonPosition(IGroundUnitCollection balloonUnitCollection)
    {
        BalloonUnit balloonUnit = null;
        for (IGroundUnit groundUnit : balloonUnitCollection.getGroundUnits())
        {
            if (groundUnit instanceof BalloonUnit)
            {
                balloonUnit = (BalloonUnit) groundUnit;
            }
        }
        assert(balloonUnit != null);
        
        GroundUnitElement balloonElement = null;
        for(GroundUnitElement groundUnitElement : balloonUnit.getGroundElements())
        {
            if (Balloon.isBalloonName(groundUnitElement.getVehicle().getScript()))
            {
                balloonElement = groundUnitElement;
            }
        }
        assert(balloonElement != null);

        Coordinate balloonPosition = balloonElement.getSpawn().getPosition();
        return balloonPosition;
    }

    private void verifyBalloonDefenseIsCloseToBalloon(IFlight flight, Coordinate balloonPosition)
    {
        boolean balloonBustIsCloseToBalloon = false;
        for (McuWaypoint waypoint : flight.getWaypointPackage().getAllWaypoints())
        {
            double distanceFromBalloon = MathUtils.calcDist(waypoint.getPosition(), balloonPosition);
            if (distanceFromBalloon < 7000)
            {
                balloonBustIsCloseToBalloon = true;
            }
        }
        assert(balloonBustIsCloseToBalloon == true);
    }

    private void verifyBalloonBustIsCloseToBalloon(IFlight opposingFlight, Coordinate balloonPosition)
    {
        boolean balloonDefenseIsCloseToBalloon = false;
        for (McuWaypoint waypoint : opposingFlight.getWaypointPackage().getAllWaypoints())
        {
            double distanceFromBalloon = MathUtils.calcDist(waypoint.getPosition(), balloonPosition);
            if (distanceFromBalloon < 3000)
            {
                balloonDefenseIsCloseToBalloon = true;
            }
        }
        assert(balloonDefenseIsCloseToBalloon == true);
    }
}
