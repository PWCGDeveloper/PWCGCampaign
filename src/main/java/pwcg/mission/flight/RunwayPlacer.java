package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.plane.PlaneType.PlaneSize;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.plane.Plane;

public class RunwayPlacer
{
	private Campaign campaign;
	
    public RunwayPlacer (Campaign campaign)
    {
    	this.campaign = campaign;
    }

    public List<Coordinate> getFlightTakeoffPositions(Flight flight, IAirfield airfield) throws PWCGException
    {
        int takeoffSpacing = calculateTakeoffSpacing(flight.getLeadPlane());

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

    protected int calculateTakeoffSpacing(Plane flightLeader) throws PWCGException
    {
        int offsetForBigPlane = 0;
        if (flightLeader.getPlaneSize() == PlaneSize.PLANE_SIZE_MEDIUM)
        {
            offsetForBigPlane = 10;
        }
        else if (flightLeader.getPlaneSize() == PlaneSize.PLANE_SIZE_LARGE)
        {
            offsetForBigPlane = 25;
        }
        
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int takeoffSpacing = configManager.getIntConfigParam(ConfigItemKeys.TakeoffSpacingKey) + offsetForBigPlane;
        return takeoffSpacing;
    }

 
}
