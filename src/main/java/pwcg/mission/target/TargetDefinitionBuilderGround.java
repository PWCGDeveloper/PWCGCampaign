package pwcg.mission.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;

public class TargetDefinitionBuilderGround
{
    private Campaign campaign;
    private TargetDefinition targetDefinition = new TargetDefinition();

    public TargetDefinitionBuilderGround (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public TargetDefinition buildTargetDefinitionBattle (
            ICountry attackingCountry, 
            ICountry targetCountry, 
            TacticalTarget targetType, 
            Coordinate targetPosition,
            boolean isPlayerTarget) throws PWCGException
    {
        targetDefinition.setTargetType(targetType);
        targetDefinition.setAttackingSquadron(null);
        targetDefinition.setTargetName(buildTargetName(targetCountry, targetType));

        targetDefinition.setAttackingCountry(attackingCountry);
        targetDefinition.setTargetCountry(targetCountry);
        targetDefinition.setDate(campaign.getDate());
        
        targetDefinition.setPreferredRadius(5000);
        targetDefinition.setMaximumRadius(10000);

        targetDefinition.setTargetPosition(targetPosition);
        targetDefinition.setTargetOrientation(new Orientation());
        targetDefinition.setPlayerTarget(isPlayerTarget);

        return targetDefinition;
    }

    public TargetDefinition buildTargetDefinitionAmbient (
            ICountry targetCountry, 
            TacticalTarget targetType, 
            Coordinate targetPosition,
            boolean isPlayerTarget) throws PWCGException
    {
        targetDefinition.setTargetType(targetType);
        targetDefinition.setTargetName(buildTargetName(targetCountry, targetType));

        targetDefinition.setAttackingCountry(targetCountry.getOppositeSideCountry());
        targetDefinition.setTargetCountry(targetCountry);
        targetDefinition.setDate(campaign.getDate());
        
        targetDefinition.setPreferredRadius(5000);
        targetDefinition.setMaximumRadius(10000);

        targetDefinition.setTargetPosition(targetPosition);
        targetDefinition.setTargetOrientation(new Orientation());
        targetDefinition.setPlayerTarget(isPlayerTarget);

        return targetDefinition;
    }

    private String buildTargetName(ICountry targetCountry, TacticalTarget targetType)
    {
        String nationality = targetCountry.getNationality();
        String name = nationality + " " + targetType.getTargetName();
        return name;
    }
}
