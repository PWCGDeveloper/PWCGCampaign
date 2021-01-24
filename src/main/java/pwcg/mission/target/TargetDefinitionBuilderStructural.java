package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.building.PwcgStructure;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;

public class TargetDefinitionBuilderStructural
{
    private FlightInformation flightInformation;
    private List<TargetDefinition> targetDefinitions = new ArrayList<>();

    public TargetDefinitionBuilderStructural(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }

    public List<TargetDefinition> findStructures() throws PWCGException
    {
        for (FixedPosition structure : flightInformation.getMission().getMissionBlockBuilder().getPositionsForMission())
        {
            ICountry structureCountry = structure.getCountry(flightInformation.getCampaign().getDate());
            if (structureCountry.getCountry() != Country.NEUTRAL && (structureCountry.getSide() != flightInformation.getCountry().getSide()))
            {
                PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding(structure.getModel());
                if (building != PwcgStructure.CHURCH && building != PwcgStructure.STATIC_VEHICLE && building != PwcgStructure.UNKNOWN)
                {
                    createTargetDefinitionFromStructures(structure, building.toTargetType());
                }
            }
        }

       
        return targetDefinitions;
    }

    private void createTargetDefinitionFromStructures(FixedPosition structure, TargetType targetType) throws PWCGException
    {
        TargetDefinition targetDefinition = new TargetDefinition(targetType, structure.getPosition().copy(), structure.getCountry(flightInformation.getCampaign().getDate()));
        targetDefinitions.add(targetDefinition);
    }
}
