package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;
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
        List<FixedPosition> targetStructures = targetDefinitionStructureFinder.findTargetStructures();
        for (FixedPosition structure : targetStructures)
        {
            ICountry structureCountry = structure.getCountry(flightInformation.getCampaign().getDate());
            if (structureCountry.getSide() == flightInformation.getCountry().getSide().getOppositeSide())
            {
                PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding(structure.getModel());
                if (building != PwcgStructure.CHURCH && building != PwcgStructure.STATIC_VEHICLE && building != PwcgStructure.UNKNOWN)
                {
                    createTargetDefinitionFromStructures(structure, building.toTargetType(), building);
                }
            }
        }

        return targetDefinitions;
    }

    private void createTargetDefinitionFromStructures(FixedPosition structure, TargetType targetType, PwcgStructure building) throws PWCGException
    {
        TargetDefinition targetDefinition = new TargetDefinition(targetType, structure.getPosition().copy(), structure.getCountry(flightInformation.getCampaign().getDate()),building.getDescription());
        targetDefinitions.add(targetDefinition);
    }    
}