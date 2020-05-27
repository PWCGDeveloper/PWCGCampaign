package pwcg.mission.ground;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionBalloonBuilder;
import pwcg.mission.MissionShipBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetType;

public class MissionGroundUnitBuilder
{
    private Mission mission = null;
    private Campaign campaign = null;

    private List<IGroundUnitCollection> missionBattles = new ArrayList<>();
    private List<IGroundUnitCollection> missionTrains = new ArrayList<>();
    private List<IGroundUnitCollection> missionTrucks = new ArrayList<>();
    private List<IGroundUnitCollection> missionDrifters = new ArrayList<>();
    private List<IGroundUnitCollection> missionBalloons = new ArrayList<>();
    private List<IGroundUnitCollection> missionShips = new ArrayList<>();
    private List<IGroundUnitCollection> AA = new ArrayList<>();

    public MissionGroundUnitBuilder (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }

    public void generateGroundUnitsForMission() throws PWCGException 
    {
        generateBattles();
        generateTrains();
        generateTrucks();
        generateDrifters();
        generateBalloons();
        generateShips();
        createFrontLineAAA();
    }

    private void generateBattles() throws PWCGException 
    {
        MissionBattleBuilder battleBuilder = new MissionBattleBuilder(campaign, mission);
        missionBattles = battleBuilder.generateBattles();
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

    private void generateDrifters() throws PWCGException
    {
        MissionDrifterBuilder drifterBuilder = new MissionDrifterBuilder(campaign, mission);
        missionDrifters = drifterBuilder.generateMissionDrifters();
    }

    private void createFrontLineAAA() throws PWCGException 
    {
        AAAManager aaaManager = new AAAManager(campaign, mission);
        AA = aaaManager.getAAAForMission();
    }

    private void generateBalloons() throws PWCGException
    {
        MissionBalloonBuilder balloonBuilder = new MissionBalloonBuilder(mission);
        missionBalloons = balloonBuilder.createMissionBalloons();
    }


    private void generateShips() throws PWCGException
    {
        MissionShipBuilder shipBuilder = new MissionShipBuilder(mission);
        missionShips = shipBuilder.createMissionShips();
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        for (IGroundUnitCollection battle : missionBattles)
        {
            battle.write(writer);
        }

        for (IGroundUnitCollection train : missionTrains)
        {
            train.write(writer);
        }

        for (IGroundUnitCollection truckConvoy : missionTrucks)
        {
            truckConvoy.write(writer);
        }

        for (IGroundUnitCollection drifter : missionDrifters)
        {
            drifter.write(writer);
        }

        for (IGroundUnitCollection balloons : missionBalloons)
        {
            balloons.write(writer);
        }

        for (IGroundUnitCollection aa : AA)
        {
            aa.write(writer);
        }
    }
    
    public List<IGroundUnitCollection> getAllMissionGroundUnits()
    {
        List<IGroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
        allMissionGroundUnits.addAll(missionBattles);
        allMissionGroundUnits.addAll(missionTrucks);
        allMissionGroundUnits.addAll(missionTrains);
        allMissionGroundUnits.addAll(missionDrifters);
        allMissionGroundUnits.addAll(missionBalloons);
        allMissionGroundUnits.addAll(missionShips);
        allMissionGroundUnits.addAll(AA);
        return allMissionGroundUnits;
    }
    
    public List<IGroundUnitCollection> getAllInterestingMissionGroundUnits()
    {
        List<IGroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
        allMissionGroundUnits.addAll(missionBattles);
        allMissionGroundUnits.addAll(missionTrucks);
        allMissionGroundUnits.addAll(missionTrains);
        allMissionGroundUnits.addAll(missionDrifters);
        allMissionGroundUnits.addAll(missionBalloons);
        allMissionGroundUnits.addAll(missionShips);

        if (allMissionGroundUnits.size() == 0)
        {
            allMissionGroundUnits.addAll(AA);
        }
        return allMissionGroundUnits;
    }
    
    public List<IGroundUnitCollection> getBattleMissionGroundUnits()
    {
        List<IGroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
        allMissionGroundUnits.addAll(missionBattles);
        if (allMissionGroundUnits.size() == 0)
        {
            allMissionGroundUnits.addAll(AA);
        }
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
    
    public List<TargetType> getAvailableGroundUnitTargetTypesForMissionForSide(Side side) throws PWCGException
    {
        Map <TargetType, TargetType> uniqueTargetTypesForSide = new HashMap<>();
        for (IGroundUnitCollection groundUnitCollection : getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getTargetType() != TargetType.TARGET_NONE)
            {
                if (groundUnitCollection.getGroundUnitsForSide(side).size() > 0)
                {
                    uniqueTargetTypesForSide.put(groundUnitCollection.getTargetType(), groundUnitCollection.getTargetType());
                }
            }
        }
        return new ArrayList<>(uniqueTargetTypesForSide.values());
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
        for (IGroundUnitCollection groundUnitCollection : AA)
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

    public List<IGroundUnitCollection> getBalloonUnits()
    {
        return missionBalloons;
    }
 }
