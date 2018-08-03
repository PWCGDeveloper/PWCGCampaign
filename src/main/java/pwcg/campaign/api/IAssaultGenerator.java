package pwcg.campaign.api;

import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.AssaultInformation;
import pwcg.mission.ground.BattleSize;

public interface IAssaultGenerator
{

    AssaultInformation generateAssault(TargetDefinition targetDefinition, BattleSize battleSize) throws PWCGException;
}