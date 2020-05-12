package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class AirfieldApproachAABuilder
{
    private List<IGroundUnitCollection> airfieldApproachAA = new ArrayList<>();

    public List<IGroundUnitCollection> addAirfieldApproachAA(IFlight flight) throws PWCGException
    {
        MissionPoint approachPositionMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_LANDING_APPROACH);
        MissionPoint landingPositionMissionPoint = flight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_LANDING);
        
        if (approachPositionMissionPoint == null || landingPositionMissionPoint == null)
        {
            return airfieldApproachAA;
        }
        
        Coordinate approachPosition = approachPositionMissionPoint.getPosition().copy();
        Coordinate landingPosition = landingPositionMissionPoint.getPosition().copy();
        double angleOut = MathUtils.calcAngle(landingPosition, approachPosition);
        
        double angleLeft = MathUtils.adjustAngle(angleOut, 270);
        double angleRight = MathUtils.adjustAngle(angleOut, 90);
        Coordinate firstAAPoint = MathUtils.calcNextCoord(landingPosition, angleOut, 500);
        
        List<Coordinate> aaCoordinates = buildAirfieldAAPositions(angleOut, angleLeft, angleRight, firstAAPoint);
        buildFlightPathAA(flight, aaCoordinates);

        return airfieldApproachAA;
    }

    private List<Coordinate> buildAirfieldAAPositions(double angleOut, double angleLeft, double angleRight, Coordinate firstAAPoint) throws PWCGException
    {
        List<Coordinate> aaCoordinates = new ArrayList<>();
        for (int i = 0; i < 5; ++i)
        {
            Coordinate aaCenterCoordinate = MathUtils.calcNextCoord(firstAAPoint, angleOut, (i * 1000));
            Coordinate leftAAPoint = MathUtils.calcNextCoord(aaCenterCoordinate, angleLeft, 500);
            Coordinate rightAAPoint = MathUtils.calcNextCoord(aaCenterCoordinate, angleRight, 500);
            
            aaCoordinates.add(leftAAPoint);
            aaCoordinates.add(rightAAPoint);
        }
        return aaCoordinates;
    }

    private void buildFlightPathAA(IFlight flight, List<Coordinate> aaCoordinates) throws PWCGException
    {
        for (Coordinate aaPoint : aaCoordinates)
        {
            AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(flight.getCampaign(), flight.getSquadron().getCountry(), aaPoint);
            IGroundUnitCollection aaa = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
            if (aaa != null)
            {
                for (IGroundUnit aaGun : aaa.getGroundUnits())
                {
                    aaGun.setAiLevel(AiSkillLevel.COMMON);
                }
                airfieldApproachAA.add(aaa);
            }
        }
    }
}
