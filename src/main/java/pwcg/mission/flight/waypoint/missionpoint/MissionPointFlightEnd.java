package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.PWCGLocation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.end.ApproachWaypointGenerator;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuLanding;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightEnd extends MissionPointSetSingleWaypointSet implements IMissionPointSet, IMissionPointSetLanding
{
    private IFlight flight;

    private McuLanding landingMcu;
    private IAirfield landingAirfield;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;

    public MissionPointFlightEnd(IFlight flight, IAirfield landingAirfield)
    {
        this.flight = flight;
        this.landingAirfield = landingAirfield;
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_END;
    }
    
    public void createFlightEnd() throws PWCGException, PWCGException 
    {
        createApproach();  
        createLanding();  
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints() throws PWCGException
    {
        List<MissionPoint> waypointsCoordinates =  super.getWaypointsAsMissionPoints();
        MissionPoint landPoint = new MissionPoint(landingMcu.getPosition(), WaypointAction.WP_ACTION_LANDING);
        waypointsCoordinates.add(landPoint);
        return waypointsCoordinates;
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);
        landingMcu.write(writer);
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        super.getLastWaypoint().setTarget(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return super.getFirstWaypoint().getIndex();
    }    

    @Override
    public void disableLinkToNextTarget()
    {
        linkToNextTarget = false;        
    }

    @Override
    public boolean isLinkToNextTarget()
    {
        return linkToNextTarget;
    }

    @Override
    public void finalizeMissionPointSet(PlaneMcu plane) throws PWCGException
    {
        super.finalizeMissionPointSet(plane);
        createTargetAssociations();
        createobjectAssociations(plane);
    }
    
    @Override
    public void setLandOnPlane(int planeIndex)
    {
        landingMcu.setObject(planeIndex);
    }

    private void createApproach() throws PWCGException
    {
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight, landingAirfield);
        super.addWaypoint(approachWaypoint);
    }
    
    private void createLanding() throws PWCGException
    {
        PWCGLocation location = landingAirfield.getLandingLocation(flight.getMission());
        
        landingMcu = new McuLanding();
        landingMcu.setDesc("Land");
        landingMcu.setName("Land");
        landingMcu.setPosition(location.getPosition());
        landingMcu.setOrientation(location.getOrientation());
    }
    
    public void createTargetAssociations() throws PWCGException
    {
        super.getLastWaypoint().setTarget(landingMcu.getIndex());
    }
    
    private void createobjectAssociations (PlaneMcu plane)
    {
        landingMcu.clearObjects();
        landingMcu.setObject(plane.getLinkTrId());
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        allFlightPoints.addAll(waypoints.getWaypoints());
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }
}
