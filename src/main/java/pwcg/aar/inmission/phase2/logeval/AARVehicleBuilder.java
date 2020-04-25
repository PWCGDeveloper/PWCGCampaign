package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBuilding;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTurret;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.plane.Balloon;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

public class AARVehicleBuilder
{
    private AARBotVehicleMapper botPlaneMapper;
    private AARVehiclePlaneLanded landedMapper;
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    
    private Map <String, LogPlane> logPlanes = new HashMap<>();
    private Map <String, LogBalloon> logBalloons = new HashMap<>();
    private Map <String, LogGroundUnit> logGroundUnits = new HashMap<>();
    private Map <String, LogTurret> logTurrets = new HashMap<>();

    private Map <String, LogBuilding> logBuildingsByName = new HashMap<>();
    private Map <String, LogBuilding> logBuildingsById = new HashMap<>();

    public AARVehicleBuilder(
            AARBotVehicleMapper botPlaneMapper,
            AARVehiclePlaneLanded landedMapper, 
            PwcgMissionDataEvaluator pwcgMissionDataEvaluator)
    {
        this.botPlaneMapper = botPlaneMapper;
        this.landedMapper = landedMapper;
        this.pwcgMissionDataEvaluator = pwcgMissionDataEvaluator;
    }
    
    public void buildVehicleListsByVehicleType(AARLogEventData logEventData) throws PWCGException 
    {
        sortVehiclesByType(logEventData.getVehicles());
        createTurretEntitiesForVehicle(logEventData.getTurrets());
        botPlaneMapper.mapBotsToCrews(logPlanes);        
        landedMapper.buildLandedLocations(logPlanes);
    }

    public LogAIEntity getVehicle(String id) throws PWCGException
    {
        if (logGroundUnits.containsKey(id))
        {
            return logGroundUnits.get(id);
        }
        else if (logPlanes.containsKey(id))
        {
            return logPlanes.get(id);
        }
        else if (logBalloons.containsKey(id))
        {
            return logBalloons.get(id);
        }
        else if (logTurrets.containsKey(id))
        {
            return findEntityForTurret(id);
        }
        else if (logBuildingsById.containsKey(id))
        {
            return logBuildingsById.get(id);
        }

        return null;
    }    

    public LogAIEntity getPlaneByName(Integer serialNumber) throws PWCGException
    {
        for (LogPlane logPlane : logPlanes.values())
        {
            if (logPlane.isCrewMember(serialNumber))
            {
                return logPlane;
            }
        }

        return null;
    }

    public List<LogPlane> getPlayerLogPlanes()
    {
        List<LogPlane> playerLogPlanes = new ArrayList<>();
        for (LogPlane logPlane : logPlanes.values())
        {
            if (SerialNumber.getSerialNumberClassification(logPlane.getPilotSerialNumber()) == SerialNumberClassification.PLAYER)
            {
                playerLogPlanes.add(logPlane);
            }
        }
        return playerLogPlanes;
    }

    private void sortVehiclesByType(List<IAType12> vehicleList) throws PWCGException
    {        
        for (IAType12 atype12 : vehicleList)
        {
            if (pwcgMissionDataEvaluator.wasPilotAssignedToMissionByName(atype12.getName()))
            {
                createLogPlane(atype12);
            }
            else if (Balloon.isBalloonName(atype12.getName()))
            {
                createLogBalloon(atype12);
            }
            else if (atype12.getName().contains("BlocksArray"))
            {
                handleBuildingSpawn(atype12);
            }
            else
            {
                createLogGroundUNit(atype12);
            }
        }
        
        if (logPlanes.isEmpty())
        {
        	throw new PWCGException("No planes found in logs to associate with the latest mission");
        }
    }

    private void createLogPlane(IAType12 atype12) throws PWCGException
    {        
        LogPlane logPlane = makePlaneFromMissionAndLog(atype12);
                        
        logPlanes.put(atype12.getId(), logPlane);
        PWCGLogger.log(LogLevel.DEBUG, "Add Plane: " + atype12.getName() + " ID:" + atype12.getId() + " Type:" + atype12.getType());
    }

    private LogPlane makePlaneFromMissionAndLog(IAType12 atype12) throws PWCGException
    {
        PwcgGeneratedMissionPlaneData missionPlane = pwcgMissionDataEvaluator.getPlaneForPilotByName(atype12.getName());
        LogPlane logPlane = new LogPlane(atype12.getSequenceNum());
        logPlane.initializeEntityFromEvent(atype12);
        logPlane.initializeFromMissionPlane(missionPlane);
        return logPlane;
    }

    private void createLogBalloon(IAType12 atype12) throws PWCGException
    {
        LogAIEntity logEntity;
        logEntity = new LogBalloon(atype12.getSequenceNum());
        logEntity.initializeEntityFromEvent(atype12);
        logEntity.setRole(Role.ROLE_BALLOON);
        
        logBalloons.put(atype12.getId(), (LogBalloon)logEntity);
        PWCGLogger.log(LogLevel.DEBUG, "Add Plane: " + atype12.getName() + " ID:" + atype12.getId() + " Type:" + atype12.getType());
    }

    private void createLogGroundUNit(IAType12 atype12) throws PWCGException
    {
        LogAIEntity logEntity;
        logEntity = new LogGroundUnit(atype12.getSequenceNum());
        logEntity.initializeEntityFromEvent(atype12);
        
        logGroundUnits.put(atype12.getId(), (LogGroundUnit)logEntity);
        PWCGLogger.log(LogLevel.DEBUG, "Add Entity: " + atype12.getName() + " ID:" + atype12.getId() + " Type:" + atype12.getType());
    }
    
    private void createTurretEntitiesForVehicle(List<IAType12> turretList) throws PWCGException
    {
        for (IAType12 atype12 : turretList)
        {
            LogPlane planeResult = logPlanes.get(atype12.getPid());
            if (planeResult != null)
            {
                logTurrets.put(atype12.getId(), planeResult.createTurret(atype12));
            }
            
            LogGroundUnit groundUnitResult = logGroundUnits.get(atype12.getPid());
            if (groundUnitResult != null)
            {
                logTurrets.put(atype12.getId(), groundUnitResult.createTurret(atype12));
            }
        }
    }
    
    private LogAIEntity findEntityForTurret(String turretId) throws PWCGException
    {
        for (LogPlane logPlane : logPlanes.values())
        {
            if (logPlane.ownsTurret(turretId))
            {
                return logTurrets.get(turretId);
            }
        }
        
        for (LogGroundUnit logGroundUnit : logGroundUnits.values())
        {
            if (logGroundUnit.ownsTurret(turretId))
            {
                return logGroundUnit;
            }
        }

        return null;
    }

    private void handleBuildingSpawn(IAType12 atype12) throws PWCGException {
        LogBuilding logBuilding;
        if (logBuildingsByName.containsKey(atype12.getType()))
        {
            logBuilding = logBuildingsByName.get(atype12.getType());
            logBuildingsById.put(atype12.getId(), logBuilding);
        }
        else if (!atype12.getType().equals("BlocksArray"))
        {
            logBuilding = new LogBuilding(atype12.getSequenceNum());
            logBuilding.initializeEntityFromEvent(atype12);

            logBuildingsByName.put(atype12.getType(), logBuilding);
            logBuildingsById.put(atype12.getId(), logBuilding);
        }
    }

    public Map<String, LogPlane> getLogPlanes()
    {
        return logPlanes;
    }

    public Map<String, LogBalloon> getLogBalloons()
    {
        return logBalloons;
    }

    public Map<String, LogGroundUnit> getLogGroundUNits()
    {
        return logGroundUnits;
    }
}
