package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnit;

public class GroundUnitInformationFactory
{
    public static GroundUnitInformation buildGroundUnitInformation(
            Campaign campaign, 
            MissionBeginUnit missionBeginUnit, 
            ICountry country, 
            String name,
            TacticalTarget targetType,
            Coordinate startCoords, 
            Coordinate targetCoords,
            Orientation orientation,
            boolean isPlayerTarget) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = new GroundUnitInformation();
        groundUnitInformation.setMissionBeginUnit(missionBeginUnit);
        groundUnitInformation.setCountry(country);
        groundUnitInformation.setName(name);
        groundUnitInformation.setDate(campaign.getDate());
        groundUnitInformation.setTargetType(targetType);
        groundUnitInformation.setPosition(startCoords);
        groundUnitInformation.setDestination(targetCoords);
        
        GroundUnitSize unitSize = calcNumUnitsByConfig(campaign, isPlayerTarget);
        groundUnitInformation.setUnitSize(unitSize);
        
        orientation = determineOrientation(startCoords, targetCoords, orientation);
        groundUnitInformation.setOrientation(orientation);

        return groundUnitInformation;
    }

    public static GroundUnitInformation buildGroundUnitInformation(Campaign campaign, MissionBeginUnit missionBeginUnit, TargetDefinition targetDefinition) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = new GroundUnitInformation();
        groundUnitInformation.setMissionBeginUnit(missionBeginUnit);
        groundUnitInformation.setCountry(targetDefinition.getTargetCountry());
        groundUnitInformation.setName(targetDefinition.getTargetName());
        groundUnitInformation.setDate(campaign.getDate());
        groundUnitInformation.setTargetType(targetDefinition.getTargetType());
        groundUnitInformation.setPosition(targetDefinition.getTargetPosition());
        groundUnitInformation.setDestination(targetDefinition.getTargetPosition());
        
        GroundUnitSize unitSize = calcNumUnitsByConfig(campaign, targetDefinition.isPlayerTarget());
        groundUnitInformation.setUnitSize(unitSize);
        
        Orientation orientation = determineOrientation(targetDefinition.getTargetPosition(), targetDefinition.getTargetPosition(), targetDefinition.getTargetOrientation());
        groundUnitInformation.setOrientation(orientation);

        return groundUnitInformation;
    }

    private static Orientation determineOrientation(Coordinate position, Coordinate destination, Orientation orientation) throws PWCGException
    {
        if (orientation == null)
        {
            double facing = RandomNumberGenerator.getRandom(360);
            orientation = new Orientation(facing);
        }
        else
        {
            if (position.equals(destination))
            {
                return orientation;
            }
            else
            {
                orientation = createOrientation(position, destination);
            }
        }
        
        return orientation;
    }

    private static Orientation createOrientation(Coordinate position, Coordinate destination) throws PWCGException
    {
        Orientation orientation = new Orientation(RandomNumberGenerator.getRandom(360));
        if (!position.equals(destination))
        {
            double facing = MathUtils.calcAngle(position, destination);
            orientation = new Orientation(facing);
        }
        else
        {
            double facing = RandomNumberGenerator.getRandom(360);
            orientation = new Orientation(facing);
        }

        return orientation;
    }
    

    protected static GroundUnitSize calcNumUnitsByConfig(Campaign campaign, Boolean isPlayerTarget) throws PWCGException 
    {
        if (isPlayerTarget)
        {
            ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
            String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
            if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
            {
                return GroundUnitSize.GROUND_UNIT_SIZE_HIGH;
            }
            else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
            {
                return GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM;
            }
            else
            {
                return GroundUnitSize.GROUND_UNIT_SIZE_LOW;
            }
        }

        return GroundUnitSize.GROUND_UNIT_SIZE_TINY;
    }

}
