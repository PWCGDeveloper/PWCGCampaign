package pwcg.mission.target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.group.ScriptedFixedPosition;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;
import pwcg.mission.ground.building.PwcgStructure;

public class TargetDefinitionStructureFinder
{
    private FlightInformation flightInformation;
    private Map<Integer, ScriptedFixedPosition> targetStructures = new HashMap<>();

    public TargetDefinitionStructureFinder(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }

    public List<ScriptedFixedPosition> findTargetStructures() throws PWCGException
    {        
        addTargetStructuresFromMissionBox();
        addTargetStructuresFromNearbyAirfields();
        addTargetStructuresFromNearbyCities();
        return new ArrayList<>(targetStructures.values());
    }

    private void addTargetStructuresFromMissionBox() throws PWCGException
    {
        for (ScriptedFixedPosition structure : flightInformation.getMission().getMissionBlocks().getStructuresWithinMissionBorders())
        {
            ICountry structureCountry = structure.getCountry(flightInformation.getCampaign().getDate());
            if (structureCountry.getSide() == flightInformation.getCountry().getSide().getOppositeSide())
            {
                targetStructures.put(structure.getIndex(), structure);
            }
        }
    }

    private void addTargetStructuresFromNearbyAirfields() throws PWCGException
    {
        GroupManager groupData = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        for (Block structure : groupData.getAirfieldBlocks())
        {
            ICountry structureCountry = structure.getCountry(flightInformation.getCampaign().getDate());
            if (structureCountry.getSide() == flightInformation.getCountry().getSide().getOppositeSide())
            {
                if (isBlockNearMissionCenterAirfield(structure))
                {
                    targetStructures.put(structure.getIndex(), structure);
                }
            }
        }
    }

    private void addTargetStructuresFromNearbyCities() throws PWCGException
    {
        GroupManager groupData = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        for (Block structure : groupData.getStandaloneBlocks())
        {
            ICountry structureCountry = structure.getCountry(flightInformation.getCampaign().getDate());
            if (structureCountry.getSide() == flightInformation.getCountry().getSide().getOppositeSide())
            {
                if (isBlockNearCityInMissionArea(structure))
                {
                    targetStructures.put(structure.getIndex(), structure);
                }
            }
        }
    }

    private boolean isBlockNearMissionCenterAirfield(Block structure) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        Coordinate missionCenter =  flightInformation.getMission().getMissionBorders().getCenter();
        int searchRadius = productSpecific.getMediumMissionRadius();
        List<Airfield> airfieldsCloseToMissionCenter = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().getWithinRadius(missionCenter, searchRadius);
        for (Airfield airfield : airfieldsCloseToMissionCenter)
        {
            double distanceFromAirfield = MathUtils.calcDist(structure.getPosition(), airfield.getPosition());
            if (distanceFromAirfield < 5000)
            {
                return true;
            }
        }
        return false;
    }

    private boolean isBlockNearCityInMissionArea(Block structure) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        Coordinate missionCenter =  flightInformation.getMission().getMissionBorders().getCenter();
        int searchRadius = productSpecific.getMediumMissionRadius();        
        double distanceFromMissionCenter = MathUtils.calcDist(structure.getPosition(), missionCenter);
        if (distanceFromMissionCenter < searchRadius)
        {
            PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding(structure.getModel());
            TargetType buildingTargetType = building.toTargetType();
            if (TargetType.isCityTargetType(buildingTargetType))
            {
                return true;
            }
        }
        return false;
    }
}
