package pwcg.mission.ground;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.Mission;
import pwcg.mission.MissionBalloonBuilder;
import pwcg.mission.MissionShipBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetType;

public class MissionGroundUnitBuilder
{
    private Mission mission = null;
    private Campaign campaign = null;

    private List<GroundUnitCollection> missionBattles = new ArrayList<>();
    private List<GroundUnitCollection> missionTrains = new ArrayList<>();
    private List<GroundUnitCollection> missionTrucks = new ArrayList<>();
    private List<GroundUnitCollection> missionDrifters = new ArrayList<>();
    private List<GroundUnitCollection> missionBalloons = new ArrayList<>();
    private List<GroundUnitCollection> missionShips = new ArrayList<>();
    private List<GroundUnitCollection> flightSpecificGroundUnits = new ArrayList<>();
    private List<GroundUnitCollection> AA = new ArrayList<>();

    public MissionGroundUnitBuilder (Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
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
        generateAmphibiousAssBattles();

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
        for (GroundUnitCollection groundUnit : getAllMissionGroundUnits())
        {
            groundUnit.write(writer);
        }
    }
    
    public List<GroundUnitCollection> getAllMissionGroundUnits()
    {
        List<GroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
        allMissionGroundUnits.addAll(missionBattles);
        allMissionGroundUnits.addAll(missionTrains);
        allMissionGroundUnits.addAll(missionTrucks);
        allMissionGroundUnits.addAll(missionBalloons);
        allMissionGroundUnits.addAll(missionShips);
        allMissionGroundUnits.addAll(missionDrifters);
        allMissionGroundUnits.addAll(AA);
        allMissionGroundUnits.addAll(flightSpecificGroundUnits);
        return allMissionGroundUnits;
    }
    
    public List<GroundUnitCollection> getAllInterestingMissionGroundUnits()
    {
        List<GroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
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
    
    public List<GroundUnitCollection> getBattleMissionGroundUnits()
    {
        List<GroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
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
        for (GroundUnitCollection groundUnitCollection : getAllMissionGroundUnits())
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
        for (GroundUnitCollection groundUnitCollection : getAllMissionGroundUnits())
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
        int shipCount = 0;
        
        for (GroundUnitCollection groundUnitCollection : missionTrains)
        {
            trainCount += groundUnitCollection.getUnitCount();
        }
        for (GroundUnitCollection groundUnitCollection : missionTrucks)
        {
            truckCount += groundUnitCollection.getUnitCount();
        }
        for (GroundUnitCollection groundUnitCollection : AA)
        {
            aaCount += groundUnitCollection.getUnitCount();
        }
        for (GroundUnitCollection groundUnitCollection : missionBattles)
        {
            battleCount += groundUnitCollection.getUnitCount();
        }
        for (GroundUnitCollection groundUnitCollection : missionShips)
        {
            shipCount += groundUnitCollection.getUnitCount();
        }

        System.out.println("Mission unit count battle : " + battleCount);
        System.out.println("Mission unit count train : " + trainCount);
        System.out.println("Mission unit count truck : " + truckCount);
        System.out.println("Mission unit count ships : " + shipCount);
        System.out.println("Mission unit count AA : " + aaCount);
        
        int missionUnitCount = trainCount + truckCount + aaCount + battleCount + shipCount;
        System.out.println("Mission unit count total : " + missionUnitCount);
        return missionUnitCount;

    }

    public List<GroundUnitCollection> getBalloonUnits()
    {
        return missionBalloons;
    }

    public void addFlightSpecificGroundUnit(GroundUnitCollection flightSpecificGroundUnit)
    {
        flightSpecificGroundUnits.add(flightSpecificGroundUnit);        
    }

    public GroundUnitCollection getClosestGroundUnitForSide(Coordinate position, Side side) throws PWCGException
    {
        GroundUnitCollection closestGroundUnitForSide = null;
        
        double closestDistanceToPosition = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        for (GroundUnitCollection groundUnitCollection : getAllInterestingMissionGroundUnits())
        {
            if (!groundUnitCollection.getGroundUnitsForSide(side).isEmpty())
            {
                double distanceToPosition = MathUtils.calcDist(position, groundUnitCollection.getPosition());
                if (distanceToPosition < closestDistanceToPosition)
                {
                    closestDistanceToPosition = distanceToPosition;
                    closestGroundUnitForSide = groundUnitCollection;
                    
                }
            }
        }
        return closestGroundUnitForSide;
    }
 }
