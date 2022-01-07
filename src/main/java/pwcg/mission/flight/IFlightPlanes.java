package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMcu;

public interface IFlightPlanes
{

    void enableNonVirtualFlight();

    List<PlaneMcu> getAiPlanes() throws PWCGException;

    PlaneMcu getPlaneByLinkTrId(Integer planeLinkTrId);

    PlaneMcu getFlightLeader();

    List<PlaneMcu> getPlanes();

    void setFuelForFlight(double myFuel);

    void setPlanes(List<PlaneMcu> planes) throws PWCGException;

    List<Integer> getPlaneLinkTrIds();

    void setPlanePosition(Integer planeLinkTrId, Coordinate planeCoords, Orientation planeOrientation, int startingPoint);

    void write(BufferedWriter writer) throws PWCGException;

    int getFlightSize();

    void finalize() throws PWCGException;

}