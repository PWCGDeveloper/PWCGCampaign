package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;

public class GroundUnitAttackFactory
{
    private Campaign campaign;
    private Mission mission;
    private TargetDefinition targetDefinition;

    public GroundUnitAttackFactory(Campaign campaign, Mission mission, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.targetDefinition = targetDefinition;
    }
    
    public IGroundUnitCollection createTargetGroundUnits() throws PWCGException 
    {
        // This is where we will find the target from available ground units
    }
}
