package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.group.FixedPosition;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.group.SmokeEffect;
import pwcg.mission.mcu.group.SmokeGroup;

public class MissionBlockSmoke
{
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
    
    private  List<FixedPosition> filterPositions(List<FixedPosition> fixedPositions) 
    {
        List<FixedPosition> filteredPositions = new ArrayList<>();
        for (FixedPosition fixedPosition : fixedPositions)
        {
            if (fixedPosition.getName().toLowerCase().contains("bridge"))
            {
                continue;
            }
            
            filteredPositions.add(fixedPosition);
        }
        return filteredPositions;
    }

    private void smokeNearBattle(List<FixedPosition> fixedPositions) throws PWCGException
    {
        for (FixedPosition fixedPosition : fixedPositions)
        {
            if (mission.getMissionBattleManager().isNearAnyBattle(fixedPosition.getPosition()))
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
        List<Integer> triggerPlanes = mission.getMissionFlights().getPlayersInMission();
        List<Integer> triggerAAATrucks = mission.getMissionAAATrucks().getPlayerTruckIds();
        
        List<Integer> smoketriggers = new ArrayList<>();
        smoketriggers.addAll(triggerPlanes);
        smoketriggers.addAll(triggerAAATrucks);

        SmokeGroup smokeGroup = new SmokeGroup(smoketriggers);   
        smokeGroup.buildSmokeGroup(position, smokeEffect);
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
