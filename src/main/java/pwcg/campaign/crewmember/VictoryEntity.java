package pwcg.campaign.crewmember;

import java.util.Date;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTurret;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class VictoryEntity
{
    private int airOrGround = Victory.UNSPECIFIED_VICTORY;
    private String name = "";
    private String type = "";
    private String squadronName = "";
    private Integer crewMemberSerialNumber = SerialNumber.NO_SERIAL_NUMBER;
    private String crewMemberName ="Unknown";
    private int crewMemberStatus = CrewMemberStatus.STATUS_ACTIVE;
    private boolean isGunner = false;

    public VictoryEntity()
    {
        super();
    }

    public void initialize (Date victoryDate, LogAIEntity logEntity, String crewMemberName) throws PWCGException
    {
        if (logEntity instanceof LogPlane)
        {
            LogPlane logPlane = (LogPlane)logEntity;
            initializeForPlane(victoryDate, logPlane, crewMemberName);
        }
        else if (logEntity instanceof LogBalloon)
        {
            LogBalloon logBalloon = (LogBalloon)logEntity;
            initializeForBalloon(logBalloon);
        }
        else if (logEntity instanceof LogGroundUnit)
        {
            LogGroundUnit logGroundUnit = (LogGroundUnit)logEntity;
            initializeForGround(logGroundUnit);
        }
        else if (logEntity instanceof LogTurret)
        {
            LogTurret logTurret = (LogTurret)logEntity;
            initializeForTurret(victoryDate, logTurret, crewMemberName);
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

    private void initializeForPlane(Date victoryDate, LogPlane logPlane, String crewMemberName) throws PWCGException
    {                    
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(logPlane.getSquadronId());
        LogCrewMember logCrewMember = logPlane.getLogCrewMember();

        airOrGround = Victory.AIRCRAFT;
        setType(logPlane.getVehicleType());
        name = logPlane.getName();
        crewMemberStatus = logCrewMember.getStatus();
        crewMemberSerialNumber = logCrewMember.getSerialNumber();
        squadronName = squadron.determineDisplayName(victoryDate);
        this.crewMemberName = crewMemberName;
    }

    private void initializeForBalloon(LogBalloon logBalloon) throws PWCGException
    {
        airOrGround = Victory.AIRCRAFT;
        setType(logBalloon.getVehicleType());
        name = logBalloon.getName();
    }

    private void initializeForGround(LogGroundUnit logGrountUnit) throws PWCGException
    {
        airOrGround = Victory.VEHICLE;
        setType(logGrountUnit.getVehicleType());
        name = logGrountUnit.getName();
    }

    private void initializeForTurret(Date victoryDate, LogTurret logTurret, String crewMemberName) throws PWCGException
    {
        if (!(logTurret.getParent() instanceof LogPlane))
            throw new PWCGException("Parent of turret is not a plane");

        LogPlane logPlane = (LogPlane) logTurret.getParent();
        initializeForPlane(victoryDate, logPlane, crewMemberName);
        isGunner = true;
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

    public void setType(String vehicleType)
    {
        if (vehicleType.contains("["))
        {
            int indexEnd = vehicleType.indexOf("[");
            vehicleType = vehicleType.substring(0, indexEnd);
        }

        this.type = vehicleType;
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

    public Integer getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public void setCrewMemberSerialNumber(Integer crewMemberSerialNumber)
    {
        this.crewMemberSerialNumber = crewMemberSerialNumber;
    }

    public int getCrewMemberStatus()
    {
        return crewMemberStatus;
    }

    public void setCrewMemberStatus(int crewMemberStatus)
    {
        this.crewMemberStatus = crewMemberStatus;
    }

    public String getCrewMemberName()
    {
        return crewMemberName;
    }

    public void setCrewMemberName(String crewMemberName)
    {
        this.crewMemberName = crewMemberName;
    }

    public boolean isGunner() {
        return isGunner;
    }

    public void setGunner(boolean isGunner) {
        this.isGunner = isGunner;
    }
}