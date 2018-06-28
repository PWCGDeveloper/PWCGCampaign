package pwcg.mission.ground;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAssaultGenerator;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBattle;

public abstract class AssaultGenerator  implements IAssaultGenerator
{
    public enum BattleSize
    {
        BATTLE_SIZE_SKIRMISH,
        BATTLE_SIZE_ASSAULT,
        BATTLE_SIZE_OFFENSIVE;
    }
    
    protected Campaign campaign;
    protected Mission mission;
    protected Date date;
    protected TargetDefinition targetDefinition;
    protected BattleSize battleSize;
    protected MissionBattle missionBattle = new MissionBattle();

    public AssaultGenerator(Campaign campaign, Mission mission, Date date)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.date = date;
    }
    
    @Override
    public MissionBattle generateAssault(TargetDefinition targetDefinition, BattleSize battleSize) throws PWCGException 
    {
        this.targetDefinition = targetDefinition;
        this.battleSize = battleSize;

        // RoF blows up with out of memory if there is too much packed into an assault
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int numAssaults = productSpecific.getNumAssaultSegments(battleSize);        
        
        // Find points for each element of the assault
        FrontLinesForMap frontLineMarker =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(date);
        List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(targetDefinition.getTargetCountry().getSide());
        int centerFrontIndex = frontLineMarker.findIndexForClosestPosition(targetDefinition.getTargetLocation(), targetDefinition.getTargetCountry().getSide());
        
        if (centerFrontIndex < 10)
        {
            centerFrontIndex = 10;
        }

        if (centerFrontIndex > (frontLines.size() - 10))
        {
            centerFrontIndex = frontLines.size() - 10;
        }
        
        // Generate a mini assault on each selected front point
        int startFrontIndex = centerFrontIndex - (numAssaults / 2);
        int finishFrontIndex = startFrontIndex + numAssaults;
        for (int thisIndex = startFrontIndex; thisIndex < finishFrontIndex; ++thisIndex)
        {
            Coordinate battleCoordinate = frontLineMarker.getCoordinates(thisIndex, targetDefinition.getTargetCountry().getSide());
            generateAssaultComponent (battleCoordinate);
        }
        
        mission.registerMissionBattle(missionBattle);

        return missionBattle;
    }

    
    protected abstract void generateAssaultComponent(Coordinate groundPosition) throws PWCGException;

}
