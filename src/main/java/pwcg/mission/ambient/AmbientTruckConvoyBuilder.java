package pwcg.mission.ambient;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.TargetDefinitionBuilderGround;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.TargetSide;
import pwcg.mission.ground.factory.TruckUnitFactory;
import pwcg.mission.ground.unittypes.transport.GroundTruckConvoyUnit;

public class AmbientTruckConvoyBuilder extends AmbientUnitBuilder
{
    private List<GroundTruckConvoyUnit> ambientTrucks = new ArrayList<>();

    public AmbientTruckConvoyBuilder (Campaign campaign, Mission mission)
    {
        super(campaign, mission);
    }

    public List<GroundTruckConvoyUnit> generateAmbientTrucks() throws PWCGException 
    {
        Side targetSide = TargetSide.ambientTargetSide(campaign);
        int maxTrucks = getMaxTruckConvoys(campaign);
        for (Bridge bridge : getBridgesAmbientConvoys(targetSide))
        {
            if (ambientTrucks.size() >= maxTrucks)
            {
                break;
            }
            
            possibleAmbientConvoy(targetSide, bridge);
        }
        
        return ambientTrucks;
    }

    private ArrayList<Bridge> getBridgesAmbientConvoys(Side targetSide) throws PWCGException 
    {
        ArrayList<Bridge> selectedBridges = new ArrayList<Bridge>();

        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();

        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);        
        CoordinateBox missionBorders = mission.getMissionBorders().expandBox(keepGroupSpread);

        GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        for (Bridge bridge : groupData.getBridgeFinder().findAllBridges())
        {
            if (bridge.createCountry(campaign.getDate()).getSide() == targetSide)
            {
            	if (missionBorders.isInBox(bridge.getPosition()))
                {
                    selectedBridges.add(bridge);
                }
            }
        }
        
        return selectedBridges;
    }

    private void possibleAmbientConvoy(Side targetSide, Bridge bridge) throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 40)
        {
            boolean isPlayerTarget = true;
            ICountry truckCountry = CountryFactory.makeMapReferenceCountry(targetSide);
            TargetDefinitionBuilderGround targetDefinitionBuilder = new TargetDefinitionBuilderGround(campaign);
            TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionGround(truckCountry, TacticalTarget.TARGET_TRANSPORT, bridge.getPosition(), isPlayerTarget);
            TruckUnitFactory groundUnitFactory =  new TruckUnitFactory(campaign, targetDefinition);
            GroundTruckConvoyUnit truckUnit = groundUnitFactory.createTruckConvoy();
            if (truckUnit != null)
            {
                addAmbientTruckConvoy(truckUnit, bridge);
            }
        }
    }


    private int getMaxTruckConvoys(Campaign campaign) throws PWCGException
    {
        int maxTrucks = 3;
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            maxTrucks = 2;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            maxTrucks = 5;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            maxTrucks = 8;
        }
        return maxTrucks;
    }

    private void addAmbientTruckConvoy(GroundTruckConvoyUnit truckUnit, Bridge bridge)
    {
        if (!mission.getMissionGroundUnitManager().isBridgeInUse(bridge.getIndex()))
        {
            mission.getMissionGroundUnitManager().registerBridge(bridge);
            ambientTrucks.add(truckUnit);
        }
    }
 }
