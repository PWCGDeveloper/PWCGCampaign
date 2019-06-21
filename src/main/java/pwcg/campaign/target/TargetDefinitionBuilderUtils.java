package pwcg.campaign.target;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;

public class TargetDefinitionBuilderUtils
{
    public static void chooseSides(FlightInformation flightInformation) throws PWCGException
    {
        TargetDefinition targetDefinition = flightInformation.getTargetDefinition();
        
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 60)
        {
            targetDefinition.setAttackingCountry(flightInformation.getSquadron().determineEnemyCountry(flightInformation.getCampaign(), flightInformation.getCampaign().getDate()));
            targetDefinition.setTargetCountry(flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()));
        }
        else
        {
            targetDefinition.setTargetCountry(flightInformation.getSquadron().determineEnemyCountry(flightInformation.getCampaign(), flightInformation.getCampaign().getDate()));
            targetDefinition.setAttackingCountry(flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()));
        }
    }

    public static String buildTargetName(ICountry targetCountry, TacticalTarget targetType)
    {
        String nationality = targetCountry.getNationality();
        String name = nationality + " " + targetType.getTargetName();
        return name;
    }

}
