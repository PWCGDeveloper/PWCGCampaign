package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.group.SmokeEffect;
import pwcg.mission.mcu.group.SmokeGroup;

public class MissionBlockSmoke
{
    private static final int DISTANCE_TO_FRONT_LINE_FOR_SMOKE = 10000;
    private static final int SAFE_DISTANCE_TO_AIRFIELD = 5000;
    
    private Mission mission;
    private List<SmokeGroup> smokingPositions = new ArrayList<>();
    private int maxSmokingPositions = 100;
    
    public MissionBlockSmoke(Mission mission)
    {
        this.mission = mission;        
    }
    
    public List<SmokeGroup> addSmokeToDamagedAreas(List<FixedPosition> fixedPositions) throws PWCGException
    {                
        List<FixedPosition> filteredPositions = filterPositions(fixedPositions);
                
        ConfigManagerCampaign configManager = mission.getCampaign().getCampaignConfigManager();
        maxSmokingPositions = configManager.getIntConfigParam(ConfigItemKeys.MaxSmokeInMissionKey);

        smokeNearBattle(filteredPositions);        
        smokeNearPlayer(filteredPositions);
        
        return smokingPositions;
    }
    
    private  List<FixedPosition> filterPositions(List<FixedPosition> fixedPositions) throws PWCGException 
    {
        List<FixedPosition> filteredPositions = new ArrayList<>();
        for (FixedPosition fixedPosition : fixedPositions)
        {
            if (fixedPosition.getName().toLowerCase().contains("bridge"))
            {
                continue;
            }
            
            if (isNearAirfield(fixedPosition))
            {
                continue;
            }
            
            if (!isNearFront(fixedPosition))
            {
                continue;
            }
            
            filteredPositions.add(fixedPosition);
        }
        return filteredPositions;
    }

    private boolean isNearFront(FixedPosition fixedPosition) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(mission.getCampaign().getDate());

        FrontLinePoint closestAlliedPosition = frontLinesForMap.findClosestFrontPositionForSide(fixedPosition.getPosition(), Side.ALLIED);
        double distanceToAlliedLines = MathUtils.calcDist(fixedPosition.getPosition(), closestAlliedPosition.getPosition());
        if (distanceToAlliedLines < DISTANCE_TO_FRONT_LINE_FOR_SMOKE)
        {
            return true;
        }
        
        FrontLinePoint closestAxisPosition = frontLinesForMap.findClosestFrontPositionForSide(fixedPosition.getPosition(), Side.AXIS);
        double distanceToAxisLines = MathUtils.calcDist(fixedPosition.getPosition(), closestAxisPosition.getPosition());
        if (distanceToAxisLines < DISTANCE_TO_FRONT_LINE_FOR_SMOKE)
        {
            return true;
        }

        return true;
    }

    private boolean isNearAirfield(FixedPosition fixedPosition)
    {
        Airfield airfield = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getClosestAirfield(fixedPosition.getPosition());
        if (airfield != null)
        {
            double distanceToAirfield = MathUtils.calcDist(fixedPosition.getPosition(), airfield.getPosition());
            if (distanceToAirfield < SAFE_DISTANCE_TO_AIRFIELD)
            {
                return true;
            }
        }
        
        return false;
    }

    private void smokeNearBattle(List<FixedPosition> fixedPositions) throws PWCGException
    {
        for (FixedPosition fixedPosition : fixedPositions)
        {
            if (mission.getBattleManager().isNearAnyBattle(fixedPosition.getPosition()))
            {
                createSmokeForStructure(fixedPosition.getPosition(), SmokeEffect.SMOKE_CITY);
            }
        }
    }

    private void smokeNearPlayer(List<FixedPosition> fixedPositions) throws PWCGException
    {
        for (FixedPosition fixedPosition : fixedPositions)
        {
            int roll =  RandomNumberGenerator.getRandom(100);
            if (roll <= 10)
            {
                createSmokeForStructure(fixedPosition.getPosition(), SmokeEffect.SMOKE_VILLAGE);
            }
        }
    }

    private void createSmokeForStructure(Coordinate position, SmokeEffect smokeEffect) throws PWCGException
    {
        List<Integer> triggerUnits = mission.getUnits().getPlayersInMission();
        
        List<Integer> smoketriggers = new ArrayList<>();
        smoketriggers.addAll(triggerUnits);

        SmokeGroup smokeGroup = new SmokeGroup(smoketriggers);
        
        int smokeDirection = mission.getWeather().getWindDirection();
        smokeGroup.buildSmokeGroup(position, smokeDirection, smokeEffect);
        addSmokingPosition(smokeGroup);
    }

    private void addSmokingPosition(SmokeGroup smokeGroup) throws PWCGException
    {
        if (smokingPositions.size() <= maxSmokingPositions)
        {
            MissionBlockSmokeProximityCalculator smokeProximityCalculator = new MissionBlockSmokeProximityCalculator(mission.getCampaign().getCampaignConfigManager(), smokingPositions);
            if (!smokeProximityCalculator.isCoordinateSaturated(smokeGroup.getPosition()))
            {
                smokingPositions.add(smokeGroup);
                mission.getMissionEffects().addSmokeGroup(smokeGroup);
            }
        }
    }
}
