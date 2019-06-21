package pwcg.mission.ambient;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.AssaultInformation;
import pwcg.mission.Mission;
import pwcg.mission.ground.AAAManager;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.ground.unittypes.transport.GroundTruckConvoyUnit;

public class AmbientGroundUnitBuilder
{
    private Mission mission = null;
    private Campaign campaign = null;

    private List<GroundTrainUnit> ambientTrains = new ArrayList<GroundTrainUnit>();
    private List<GroundTruckConvoyUnit> ambientTrucks = new ArrayList<GroundTruckConvoyUnit>();
    private List<GroundUnitSpawning> AAA = new ArrayList<GroundUnitSpawning>();
    private List<AssaultInformation> ambientBattles = new ArrayList<AssaultInformation>();

    public AmbientGroundUnitBuilder (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }

    public void generateAmbientGroundUnits() throws PWCGException 
    {
        if (campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.UseAmbientGroundUnitsKey) == 1)
        {
            generateAmbientTrains();
            generateAmbientTrucks();        
            createFrontLineAAA();
            
            if (!PWCGContextManager.isRoF())
            {
                generateAmbientBattles();
            }
        }
    }

    private void generateAmbientTrains() throws PWCGException 
    {
        AmbientTrainBuilder ambientTrainBuilder = new AmbientTrainBuilder(campaign, mission);
        ambientTrains = ambientTrainBuilder.generateAmbientTrains();
    }

    private void generateAmbientTrucks() throws PWCGException 
    {
        AmbientTruckConvoyBuilder ambientTruckConvoyBuilder = new AmbientTruckConvoyBuilder(campaign, mission);
        ambientTrucks = ambientTruckConvoyBuilder.generateAmbientTrucks();
    }

    private void generateAmbientBattles() throws PWCGException 
    {
        AmbientBattleBuilder ambientBattleBuilder = new AmbientBattleBuilder(campaign, mission);
        ambientBattles = ambientBattleBuilder.generateAmbientBattles();
    }
    
    private void createFrontLineAAA() throws PWCGException 
    {
        AAAManager aaaManager = new AAAManager(campaign, mission);
        AAA = aaaManager.getAAAForMission();
    }

    public List<GroundTrainUnit> getAmbientTrains()
    {
        return ambientTrains;
    }

    public List<GroundTruckConvoyUnit> getAmbientTrucks()
    {
        return ambientTrucks;
    }

    public List<AssaultInformation> getAmbientBattles()
    {
        return ambientBattles;
    }

    public List<GroundUnitSpawning> getAAA()
    {
        return AAA;
    }
 }
