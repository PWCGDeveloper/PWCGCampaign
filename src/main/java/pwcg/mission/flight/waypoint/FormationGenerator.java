package pwcg.mission.flight.waypoint;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlightPlanes;

public class FormationGenerator
{
    public static List<Coordinate> createPlaneFormationPositions(
                    IFlightPlanes planes, 
                    Coordinate startPosition,
                    Orientation orientation) throws PWCGException 
    {
        List<Coordinate> flightCoordinates = new ArrayList<Coordinate>();
        
        Coordinate leadPlaneCoords = startPosition.copy();
        flightCoordinates.add(leadPlaneCoords.copy());
        
        for (int i = 1; i < planes.getPlanes().size(); ++i)
        {
            Coordinate nextPlaneCoords = generatePositionForPlaneInFormation(orientation, leadPlaneCoords, i);
            flightCoordinates.add(nextPlaneCoords);
        }
        
        return flightCoordinates;
    }

    public static Coordinate generatePositionForPlaneInFormation(Orientation orientation, Coordinate leadPlaneCoords, int placeInFormation) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int horizontalSpacing = productSpecific.getFormationHorizontalSpacing();
        int verticalSpacing = productSpecific.getFormationVerticalSpacing();
        
        double echelonLeftAngle = MathUtils.adjustAngle(orientation.getyOri(), 330);
        Coordinate nextPlaneCoords = MathUtils.calcNextCoord(leadPlaneCoords, echelonLeftAngle, horizontalSpacing * placeInFormation);
        nextPlaneCoords.setYPos(leadPlaneCoords.getYPos() + (verticalSpacing * placeInFormation));
        return nextPlaneCoords;
    }
}
