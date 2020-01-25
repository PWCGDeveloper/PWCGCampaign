package pwcg.mission.flight.initialposition;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.plane.PlaneType.PlaneSize;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class RunwayPlacer
{	
    public RunwayPlacer ()
    {
    }

    public List<Coordinate> getFlightTakeoffPositions(IFlight flight) throws PWCGException
    {
        int takeoffSpacing = calculateTakeoffSpacing(flight.getFlightPlanes().getFlightLeader());
        IAirfield airfield = flight.getFlightInformation().getAirfield();

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        IRunwayPlacer runwayPlacer = null;
        if (productSpecificConfiguration.getTakeoffFormation() == TakeoffFormation.LINE_ABREAST)
        {
            runwayPlacer = new RunwayPlacerLineAbreast(flight, airfield, takeoffSpacing);
        }
        else if (productSpecificConfiguration.getTakeoffFormation() == TakeoffFormation.LINE_ASTERN)
        {
            runwayPlacer = new RunwayPlacerLineAstern(flight, airfield, takeoffSpacing);
        }
        else if (productSpecificConfiguration.getTakeoffFormation() == TakeoffFormation.STAGGERED)
        {
            runwayPlacer = new RunwayPlacerStaggered(flight, airfield, takeoffSpacing);
        }
        
        List<Coordinate> takeOffPositions = runwayPlacer.getFlightTakeoffPositions();
        return takeOffPositions;
    }

    protected int calculateTakeoffSpacing(PlaneMcu flightLeader) throws PWCGException
    {
        int offsetForBigPlane = 0;
        if (flightLeader.getPlaneSize() == PlaneSize.PLANE_SIZE_MEDIUM)
        {
            offsetForBigPlane = 10;
        }
        else if (flightLeader.getPlaneSize() == PlaneSize.PLANE_SIZE_LARGE)
        {
            offsetForBigPlane = 20;
        }

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int takeoffSpacing = productSpecificConfiguration.getTakeoffSpacing();
        takeoffSpacing += offsetForBigPlane;

        return takeoffSpacing;
    }

 
}
