package pwcg.mission.target;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.options.MissionType;

public class GroundTargetDefinitionFactory
{
    public static TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        if (flightInformation.getMission().getMissionOptions().getMissionType() == MissionType.SINGLE_AAA_MISSION)
        {
            TargetDefinitionBuilderAAATruck aaaTruckTargetDefinitionBuilder = new TargetDefinitionBuilderAAATruck(flightInformation);
            return aaaTruckTargetDefinitionBuilder.buildTargetDefinition();
        }
        else
        {
            SkirmishTargetDefinitionBuilder skirmishTargetDefinitionBuilder = new SkirmishTargetDefinitionBuilder(flightInformation);
            if (skirmishTargetDefinitionBuilder.isUseIconicMission())
            {
                return skirmishTargetDefinitionBuilder.buildTargetDefinition();
            }
            else
            {
                TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(flightInformation); 
                return targetDefinitionBuilder.buildTargetDefinition();
            }
        }
    }
    
}
