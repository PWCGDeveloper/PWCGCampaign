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
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.IBattleBuilder;
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
    private List<GroundUnitCollection> AAA = new ArrayList<>();
    private List<GroundUnitCollection> airfieldVehicles = new ArrayList<>();

    public MissionGroundUnitBuilder (Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public void generateGroundUnitsForMission() throws PWCGException 
    {
        generateBattle();
        generateTrains();
        generateTrucks();
        generateDrifters();
        createAAAForMission();
    }

    private void generateBattle() throws PWCGException 
    {
        IBattleBuilder battleBuilder = MissionBattleBuilderFactory.getBattleBuilder(mission);
        missionBattles = battleBuilder.generateBattle();
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

    private void createAAAForMission() throws PWCGException 
    {
        AAAManager aaaManager = new AAAManager(campaign, mission);
        aaaManager.getAAAForMission(this);
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
        allMissionGroundUnits.addAll(AAA);
        allMissionGroundUnits.addAll(airfieldVehicles);
        allMissionGroundUnits.addAll(flightSpecificGroundUnits);
        return allMissionGroundUnits;
    }
    
    public List<GroundUnitCollection> getAllInterestingMissionGroundUnits()
    {
        List<GroundUnitCollection> allInterestingMissionGroundUnits = new ArrayList<>();
        allInterestingMissionGroundUnits.addAll(missionBattles);
        allInterestingMissionGroundUnits.addAll(missionTrucks);
        allInterestingMissionGroundUnits.addAll(missionTrains);
        allInterestingMissionGroundUnits.addAll(missionDrifters);
        allInterestingMissionGroundUnits.addAll(missionBalloons);
        allInterestingMissionGroundUnits.addAll(missionShips);
        allInterestingMissionGroundUnits.addAll(airfieldVehicles);

        if (allInterestingMissionGroundUnits.size() == 0)
        {
            allInterestingMissionGroundUnits.addAll(AAA);
        }
        return allInterestingMissionGroundUnits;
    }
    
    public void finalizeGroundUnits() throws PWCGException
    {
        eliminateDuplicateGroundUnits();
    }
    
    private void eliminateDuplicateGroundUnits() throws PWCGException
    {
        eliminateDuplicateGroundUnitsFromCollection(flightSpecificGroundUnits);
        eliminateDuplicateGroundUnitsFromCollection(missionBalloons);
        eliminateDuplicateGroundUnitsFromCollection(missionBattles);
        eliminateDuplicateGroundUnitsFromCollection(missionTrucks);
        eliminateDuplicateGroundUnitsFromCollection(missionTrains);
        eliminateDuplicateGroundUnitsFromCollection(missionDrifters);
        eliminateDuplicateGroundUnitsFromCollection(missionShips);
        eliminateDuplicateGroundUnitsFromCollection(airfieldVehicles);
        eliminateDuplicateGroundUnitsFromCollection(AAA);
    }
    
    private void eliminateDuplicateGroundUnitsFromCollection(List<GroundUnitCollection> groundUnitCollections) throws PWCGException
    {
        while (eliminateDuplicateGroundUnitFromCollection(groundUnitCollections))
        {
            
        }
    }
    
    private boolean eliminateDuplicateGroundUnitFromCollection(List<GroundUnitCollection> groundUnitCollections) throws PWCGException
    {
        GroundUnitPositionDuplicateDetector groundUnitPositionVerifier = new GroundUnitPositionDuplicateDetector();        
        for (GroundUnitCollection groundUnitCollection : groundUnitCollections)
        {
            if (!groundUnitPositionVerifier.verifyGroundCollectionUnitPositionsNotDuplicated(getAllMissionGroundUnits(), groundUnitCollection))
            {
                groundUnitCollections.remove(groundUnitCollection);
                return true;
            }
        }
                        
        return false;
    }

    public List<GroundUnitCollection> getBattleMissionGroundUnits()
    {
        List<GroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
        allMissionGroundUnits.addAll(missionBattles);
        return allMissionGroundUnits;
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

    public List<GroundUnitCollection> getGroundUnitsForSide(Side side) throws PWCGException
    {
        List<GroundUnitCollection> groundUnitsForSide = new ArrayList<>();
        for (GroundUnitCollection groundUnitCollection : getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getGroundUnitsForSide(side).size() > 0)
            {
                groundUnitsForSide.add(groundUnitCollection);
            }
        }
        return groundUnitsForSide;
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
        for (GroundUnitCollection groundUnitCollection : airfieldVehicles)
        {
            truckCount += groundUnitCollection.getUnitCount();
        }
        for (GroundUnitCollection groundUnitCollection : AAA)
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

        PWCGLogger.log(LogLevel.INFO, "Mission unit count battle : " + battleCount);
        PWCGLogger.log(LogLevel.INFO, "Mission unit count train : " + trainCount);
        PWCGLogger.log(LogLevel.INFO, "Mission unit count truck : " + truckCount);
        PWCGLogger.log(LogLevel.INFO, "Mission unit count ships : " + shipCount);
        PWCGLogger.log(LogLevel.INFO, "Mission unit count AA : " + aaCount);
        
        int missionUnitCount = trainCount + truckCount + aaCount + battleCount + shipCount;
        PWCGLogger.log(LogLevel.INFO, "Mission unit count total : " + missionUnitCount);
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
    

    public void addAirfieldVehicle(GroundUnitCollection vehicle)
    {
        airfieldVehicles.add(vehicle);
    }

    public void addMissionAAA(GroundUnitCollection aaa)
    {
        AAA.add(aaa);
    }

    public List<GroundUnitCollection> getAAA()
    {
        return AAA;
    }

    public List<GroundUnitCollection> getAssaults()
    {
        return missionBattles;
    }

    public void removeExtraUnits(Coordinate coordinate, int keepRadius) throws PWCGException
    {
        removeExtraUnitsFromGroundUnitList(missionBattles, coordinate, keepRadius);
        removeExtraUnitsFromGroundUnitList(missionTrains, coordinate, keepRadius);
        removeExtraUnitsFromGroundUnitList(missionTrucks, coordinate, keepRadius);
        removeExtraUnitsFromGroundUnitList(missionDrifters, coordinate, keepRadius);
        removeExtraUnitsFromGroundUnitList(missionBalloons, coordinate, keepRadius);
        removeExtraUnitsFromGroundUnitList(flightSpecificGroundUnits, coordinate, keepRadius);
        removeExtraUnitsFromGroundUnitList(AAA, coordinate, keepRadius);
        removeExtraUnitsFromGroundUnitList(airfieldVehicles, coordinate, keepRadius);
    }
    
    private void removeExtraUnitsFromGroundUnitList(List<GroundUnitCollection> groundUnitCollections, Coordinate coordinate, int keepRadius) throws PWCGException
    {
        for (GroundUnitCollection groundUnitCollection : groundUnitCollections)
        {
            groundUnitCollection.removeExtraUnits(coordinate, keepRadius);
        }
    }
 }
