package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.group.FixedPosition;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.ground.unittypes.GroundUnit;
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
        ConfigManagerCampaign configManager = mission.getCampaign().getCampaignConfigManager();
        maxSmokingPositions = configManager.getIntConfigParam(ConfigItemKeys.MaxSmokeInMissionKey);

        smokeNearBattle(fixedPositions);        
        smokeNearInfantry(fixedPositions);
        smokeNearPlayer(fixedPositions);
        
        return smokingPositions;
    }

    private void smokeNearBattle(List<FixedPosition> fixedPositions) throws PWCGException
    {
        for (FixedPosition fixedPosition : fixedPositions)
        {
            if (mission.getMissionBattleManager().isNearAnyBattle(fixedPosition.getPosition()))
            {
                createSmoke(fixedPosition.getPosition(), SmokeEffect.SMOKE_CITY);
            }
        }
    }

    private void smokeNearInfantry(List<FixedPosition> fixedPositions) throws PWCGException
    {
        for (FixedPosition fixedPosition : fixedPositions)
        {
            if (isCloseToInfantry(fixedPosition))
            {
                createSmoke(fixedPosition.getPosition(), SmokeEffect.SMOKE_CITY_SMALL);
            }
        }
    }

    private boolean isCloseToInfantry(FixedPosition fixedPosition)
    {
        for (Flight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            for (Unit unit : flight.getLinkedUnits())
            {
                if (unit instanceof GroundUnit)
                {
                    GroundUnit groundUnit = (GroundUnit)unit;
                    if (groundUnit.isCombatUnit())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void smokeNearPlayer(List<FixedPosition> fixedPositions) throws PWCGException
    {
        for (FixedPosition fixedPosition : fixedPositions)
        {
            int roll =  RandomNumberGenerator.getRandom(100);
            if (roll <= 10)
            {
                createSmoke(fixedPosition.getPosition(), SmokeEffect.SMOKE_VILLAGE);
            }
        }
    }

    private void createSmoke(Coordinate position, SmokeEffect smokeEffect) throws PWCGException
    {
        SmokeGroup smokeGroup = new SmokeGroup();   
        smokeGroup.buildSmokeGroup(mission, position, smokeEffect);
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
