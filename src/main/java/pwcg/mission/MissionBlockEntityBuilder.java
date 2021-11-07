package pwcg.mission;

import java.util.List;

import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;
import pwcg.mission.ground.building.PwcgStructure;
import pwcg.product.bos.plane.BoSStaticPlane;
import pwcg.product.fc.plane.FCStaticPlane;

public class MissionBlockEntityBuilder
{
    private Mission mission;
    public MissionBlockEntityBuilder(Mission mission)
    {
        this.mission = mission;
    }

    public void buildEntitiesForTargetStructures(MissionBlocks missionBlocks) throws PWCGException
    {
        List<Coordinate> missionTargetCoordiinates = mission.getFlights().getTargetCoordinatesForPlayerFlights();
        for (FixedPosition structure : missionBlocks.getAllStructuresForMission())
        {
            if (isNearTarget(structure, missionTargetCoordiinates))
            {
                if (isBuildEntity(structure))
                {
                    structure.buildEntity();
                }
            }
        }
    }

    private boolean isNearTarget(FixedPosition structure, List<Coordinate> missionTargetCoordiinates) throws PWCGException
    {
        List<Coordinate> nearbyTargetLocations = Coordinate.findWithinRadius(missionTargetCoordiinates, structure.getPosition(), 7000.0);
        if (nearbyTargetLocations.size() > 0)
        {
            return true;
        }
        return false;
    }
    
    
    private boolean isBuildEntity(FixedPosition structure) throws PWCGException
    {
        if (structure.determineCountry().isNeutral())
        {
            return false;
        }        
        if (structure instanceof BoSStaticPlane)
        {
            return true;
        }
        
        if (structure instanceof FCStaticPlane)
        {
            return true;
        }
        
        if (structure instanceof Bridge)
        {
            return true;
        }
        
        if (PwcgBuildingIdentifier.identifyBuilding(structure.getModel()) != PwcgStructure.UNKNOWN)
        {
            return true;
        }
        
        return false;
    }

}
