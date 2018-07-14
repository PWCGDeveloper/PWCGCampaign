package pwcg.campaign.api;

import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBattle;
import pwcg.mission.ground.AssaultGenerator.BattleSize;

public interface IAssaultGenerator
{

    MissionBattle generateAssault(TargetDefinition targetDefinition, BattleSize battleSize) throws PWCGException;
}