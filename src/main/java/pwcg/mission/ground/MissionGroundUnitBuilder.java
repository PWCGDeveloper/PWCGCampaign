package pwcg.mission.ground;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetType;

public class MissionGroundUnitBuilder
{
    private Mission mission = null;
    private Campaign campaign = null;

    private List<IGroundUnitCollection> missionTrains = new ArrayList<>();
    private List<IGroundUnitCollection> missionTrucks = new ArrayList<>();
    private List<IGroundUnitCollection> AAA = new ArrayList<>();
    private List<IGroundUnitCollection> missionBattles = new ArrayList<>();

    public MissionGroundUnitBuilder (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }

    public void generateGroundUnitsForMission() throws PWCGException 
    {
        generateTrains();
        generateTrucks();        
        createFrontLineAAA();
        
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            generateBattles();
        }
    }

    private void generateTrains() throws PWCGException 
    {
        MissionTrainBuilder trainBuilder = new MissionTrainBuilder(campaign, mission);
        missionTrains = trainBuilder.generateMissionTrains();
    }

    private void generateTrucks() throws PWCGException 
    {
        MissionTruckConvoyBuilder truckConvoyBuilder = new MissionTruckConvoyBuilder(campaign, mission);
        missionTrucks = truckConvoyBuilder.generateMissionTrucks();
    }

    private void generateBattles() throws PWCGException 
    {
        MissionBattleBuilder battleBuilder = new MissionBattleBuilder(campaign, mission);
        missionBattles = battleBuilder.generateBattles();
    }
    
    private void createFrontLineAAA() throws PWCGException 
    {
        AAAManager aaaManager = new AAAManager(campaign, mission);
        AAA = aaaManager.getAAAForMission();
    }


    public void write(BufferedWriter writer) throws PWCGException
    {
        for (IGroundUnitCollection train : missionTrains)
        {
            train.write(writer);
        }

        for (IGroundUnitCollection truckCOnvoy : missionTrucks)
        {
            truckCOnvoy.write(writer);
        }

        for (IGroundUnitCollection battle : missionBattles)
        {
            battle.write(writer);
        }

        for (IGroundUnitCollection aaa : AAA)
        {
            aaa.write(writer);
        }
    }
    
    public List<IGroundUnitCollection> getAllMissionGroundUnits()
    {
        List<IGroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
        allMissionGroundUnits.addAll(missionBattles);
        allMissionGroundUnits.addAll(missionTrucks);
        allMissionGroundUnits.addAll(missionTrains);
        allMissionGroundUnits.addAll(AAA);
        return allMissionGroundUnits;
    }
    
    public List<TargetType> getAvailableGroundUnitTargetTypesForMission() throws PWCGException
    {
        Map <TargetType, TargetType> uniqueTargetTypes = new HashMap<>();
        for (IGroundUnitCollection groundUnitCollection : getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getTargetType() != TargetType.TARGET_NONE)
            {
                uniqueTargetTypes.put(groundUnitCollection.getTargetType(), groundUnitCollection.getTargetType());
            }
        }
        return new ArrayList<>(uniqueTargetTypes.values());
    }    

    public int getUnitCount()
    {
        int trainCount = 0;
        int truckCount = 0;
        int aaCount = 0;
        int battleCount = 0;
        for (IGroundUnitCollection groundUnitCollection : missionTrains)
        {
            trainCount += groundUnitCollection.getUnitCount();
        }
        for (IGroundUnitCollection groundUnitCollection : missionTrucks)
        {
            truckCount += groundUnitCollection.getUnitCount();
        }
        for (IGroundUnitCollection groundUnitCollection : AAA)
        {
            aaCount += groundUnitCollection.getUnitCount();
        }
        for (IGroundUnitCollection groundUnitCollection : missionBattles)
        {
            battleCount += groundUnitCollection.getUnitCount();
        }

        System.out.println("Mission unit count train : " + trainCount);
        System.out.println("Mission unit count truck : " + truckCount);
        System.out.println("Mission unit count AA : " + aaCount);
        System.out.println("Mission unit count battle : " + battleCount);
        
        int missionUnitCount = trainCount + truckCount + aaCount + battleCount;
        System.out.println("Mission unit count total : " + missionUnitCount);
        return missionUnitCount;

    }
 }
