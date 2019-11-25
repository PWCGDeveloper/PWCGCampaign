package pwcg.campaign.squadmember;

import java.util.Date;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBuilding;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTurret;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class VictoryEntity
{
    private int airOrGround = Victory.UNSPECIFIED_VICTORY;
    private String name = "";
    private String type = "";
    private String squadronName = "";
    private Integer pilotSerialNumber = SerialNumber.NO_SERIAL_NUMBER;
    private String pilotName ="Unknown";
    private int pilotStatus = SquadronMemberStatus.STATUS_ACTIVE;
    private boolean isGunner = false;

    public VictoryEntity()
    {
        super();
    }

    public void initialize (Date victoryDate, LogAIEntity logEntity, String pilotName) throws PWCGException
    {
        if (logEntity instanceof LogPlane)
        {
            LogPlane logPlane = (LogPlane)logEntity;
            initializeForPlane(victoryDate, logPlane, pilotName);
        }
        if (logEntity instanceof LogBalloon)
        {
            LogBalloon logBalloon = (LogBalloon)logEntity;
            initializeForBalloon(logBalloon);
        }
        if (logEntity instanceof LogGroundUnit)
        {
            LogGroundUnit logGroundUnit = (LogGroundUnit)logEntity;
            initializeForGround(logGroundUnit);
        }
        if (logEntity instanceof LogTurret)
        {
            LogTurret logTurret = (LogTurret)logEntity;
            initializeForTurret(victoryDate, logTurret, pilotName);
        }
        if (logEntity instanceof LogBuilding)
        {
            LogBuilding logGroundUnit = (LogBuilding)logEntity;
            initializeForBuilding(logGroundUnit);
        }
    }

    public boolean determineCompleteForAir()
    {
        if (type == null || type.isEmpty())
        {
            return false;
        }
        
        return true;
    }

    private void initializeForPlane(Date victoryDate, LogPlane logPlane, String pilotName) throws PWCGException
    {                    
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(logPlane.getSquadronId());
        LogPilot logPilot = logPlane.getLogPilot();

        airOrGround = Victory.AIR_VICTORY;
        type = logPlane.getVehicleType();
        name = logPlane.getName();
        pilotStatus = logPilot.getStatus();
        pilotSerialNumber = logPilot.getSerialNumber();
        squadronName = squadron.determineDisplayName(victoryDate);
        this.pilotName = pilotName;
    }

    private void initializeForBalloon(LogBalloon logBalloon) throws PWCGException
    {
        airOrGround = Victory.AIR_VICTORY;
        type = logBalloon.getVehicleType();
        name = logBalloon.getName();
    }

    private void initializeForGround(LogGroundUnit logGrountUnit) throws PWCGException
    {
        airOrGround = Victory.GROUND_VICTORY;
        type = logGrountUnit.getVehicleType();
        name = logGrountUnit.getName();
    }

    private void initializeForTurret(Date victoryDate, LogTurret logTurret, String pilotName) throws PWCGException
    {
        if (!(logTurret.getParent() instanceof LogPlane))
            throw new PWCGException("Parent of turret is not a plane");

        LogPlane logPlane = (LogPlane) logTurret.getParent();
        initializeForPlane(victoryDate, logPlane, pilotName);
        isGunner = true;
    }

    private void initializeForBuilding(LogBuilding logBuilding) throws PWCGException
    {
        airOrGround = Victory.GROUND_VICTORY;
        type = logBuilding.getVehicleType();
        name = logBuilding.getName();
    }

    public int getAirOrGround()
    {
        return airOrGround;
    }

    public void setAirOrGround(int airOrGround)
    {
        this.airOrGround = airOrGround;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSquadronName()
    {
        return squadronName;
    }

    public void setSquadronName(String squadronName)
    {
        this.squadronName = squadronName;
    }

    public Integer getPilotSerialNumber()
    {
        return pilotSerialNumber;
    }

    public void setPilotSerialNumber(Integer pilotSerialNumber)
    {
        this.pilotSerialNumber = pilotSerialNumber;
    }

    public int getPilotStatus()
    {
        return pilotStatus;
    }

    public void setPilotStatus(int pilotStatus)
    {
        this.pilotStatus = pilotStatus;
    }

    public String getPilotName()
    {
        return pilotName;
    }

    public void setPilotName(String pilotName)
    {
        this.pilotName = pilotName;
    }

    public boolean isGunner() {
        return isGunner;
    }

    public void setGunner(boolean isGunner) {
        this.isGunner = isGunner;
    }
}