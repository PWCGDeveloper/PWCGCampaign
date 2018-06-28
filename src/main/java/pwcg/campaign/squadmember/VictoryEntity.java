package pwcg.campaign.squadmember;

import java.util.Date;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class VictoryEntity
{
    private int airOrGround = Victory.UNSPECIFIED_VICTORY;
    private String type = "";
    private String squadronName = "";
    private Integer pilotSerialNumber = SerialNumber.NO_SERIAL_NUMBER;
    private String pilotName ="Unknown";
    public String getPilotName()
    {
        return pilotName;
    }

    private int pilotStatus = SquadronMemberStatus.STATUS_ACTIVE;

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
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(logPlane.getSquadronId());
        LogPilot logPilot = logPlane.getLogPilot();

        airOrGround = Victory.AIR_VICTORY;
        type = logPlane.getVehicleType();
        pilotStatus = logPilot.getStatus();
        pilotSerialNumber = logPilot.getSerialNumber();
        squadronName = squadron.determineDisplayName(victoryDate);
        this.pilotName = pilotName;
    }

    private void initializeForBalloon(LogBalloon logBalloon) throws PWCGException
    {
        airOrGround = Victory.AIR_VICTORY;
        type = logBalloon.getVehicleType();
    }

    private void initializeForGround(LogGroundUnit logGrountUnit) throws PWCGException
    {
        airOrGround = Victory.GROUND_VICTORY;
        type = logGrountUnit.getVehicleType();
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

    public void setPilotName(String pilotName)
    {
        this.pilotName = pilotName;
    }
    
    
}