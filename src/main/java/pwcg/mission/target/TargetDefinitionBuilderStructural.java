package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.ScriptedFixedPosition;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;
import pwcg.mission.ground.building.PwcgStructure;

public class TargetDefinitionBuilderStructural
{
    private FlightInformation flightInformation;
    private List<TargetDefinition> targetDefinitions = new ArrayList<>();

    public TargetDefinitionBuilderStructural(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }

    public List<TargetDefinition> findTargetStructures() throws PWCGException
    {
        TargetDefinitionStructureFinder targetDefinitionStructureFinder = new TargetDefinitionStructureFinder(flightInformation);
        List<ScriptedFixedPosition> targetStructures = targetDefinitionStructureFinder.findTargetStructures();
        for (ScriptedFixedPosition structure : targetStructures)
        {
            ICountry structureCountry = structure.getCountry(flightInformation.getCampaignMap(), flightInformation.getCampaign().getDate());
            if (structureCountry.getSide() == flightInformation.getCountry().getSide().getOppositeSide())
            {
                PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding(structure.getModel());
                if (includeBuilding(structure, building))
                {
                    createTargetDefinitionFromStructures(structure, building.toTargetType(), building);
                }
            }
        }

        return targetDefinitions;
    }

    private boolean includeBuilding(ScriptedFixedPosition structure, PwcgStructure building) throws PWCGException
    {
        if (building == PwcgStructure.CHURCH            || 
            building == PwcgStructure.STATIC_VEHICLE    || 
            building == PwcgStructure.UNKNOWN)
        {
            return false;
        }
        
        if (building.toTargetType() == TargetType.TARGET_AIRFIELD)
        {
            boolean isvalidAirfield = isValidTargetStructureForAirfield(structure, building);
            if (!isvalidAirfield)
            {
                return false;
            }
        }
        
        return true;
    }

    private boolean isValidTargetStructureForAirfield(ScriptedFixedPosition structure, PwcgStructure building) throws PWCGException
    {
        boolean isvalidAirfield = true;

        Airfield airfield = getAirfieldForBuildingStructure(structure, building);
        if (airfield != null)
        {
            double distanceToAirfield = MathUtils.calcDist(structure.getPosition(), airfield.getPosition());
            if (distanceToAirfield > 3000)
            {
                isvalidAirfield = false;
            }
        }
        else
        {
            isvalidAirfield = false;
        }
        
        return isvalidAirfield;
    }
    
    private Airfield getAirfieldForBuildingStructure(ScriptedFixedPosition structure, PwcgStructure building) throws PWCGException
    {
        if (building.toTargetType() == TargetType.TARGET_AIRFIELD)
        {
            return  PWCGContext.getInstance().getMap(flightInformation.getCampaignMap()).getAirfieldManager().getClosestAirfield(structure.getPosition());
        }
        
        return null;
    }

    private void createTargetDefinitionFromStructures(ScriptedFixedPosition structure, TargetType targetType, PwcgStructure building) throws PWCGException
    {
        String targetName = buildTargetName(structure, building);
        TargetDefinition targetDefinition = new TargetDefinition(targetType, structure.getPosition().copy(), 
                structure.getCountry(flightInformation.getCampaignMap(), flightInformation.getCampaign().getDate()), targetName);
        targetDefinitions.add(targetDefinition);
    }
    
    private String buildTargetName(ScriptedFixedPosition structure, PwcgStructure building) throws PWCGException
    {
        Airfield airfield = getAirfieldForBuildingStructure(structure, building);
        if (airfield != null)
        {
            return airfield.getName();
        }
        
        return building.getDescription();
    }
}