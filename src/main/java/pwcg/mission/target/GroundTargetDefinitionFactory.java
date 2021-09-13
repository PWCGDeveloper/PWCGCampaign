package pwcg.mission.target;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public class GroundTargetDefinitionFactory
{
    public static TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        TargetDefinition targetDefinition = null;
        
        if (flightInformation.getMission().isAAATruckMission())
        {
            TargetDefinitionBuilderAAATruck aaaTruckTargetDefinitionBuilder = new TargetDefinitionBuilderAAATruck(flightInformation);
            targetDefinition = aaaTruckTargetDefinitionBuilder.buildTargetDefinition();
        }
        else if (SkirmishTargetDefinitionBuilder.isUseIconicMission(flightInformation))
        {
            SkirmishTargetDefinitionBuilder skirmishTargetDefinitionBuilder = new SkirmishTargetDefinitionBuilder(flightInformation);
            targetDefinition = skirmishTargetDefinitionBuilder.buildTargetDefinition();
        }
        
        if (targetDefinition == null)
        {
            TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(flightInformation); 
            targetDefinition =  targetDefinitionBuilder.buildTargetDefinition();
        }
        
        return targetDefinition;
    }
}
