package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.campaign.plane.PlaneStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

public class LogPlane extends LogAIEntity
{
    private Coordinate landAt = null;
    private Integer squadronId;
    private boolean crashedInSight = false;
    private LogPilot logPilot;
    private int pilotSerialNumber;
    private int planeSerialNumber;
    private int planeStatus = PlaneStatus.STATUS_DEPLOYED;

    public LogPlane()
    {
    }

    public Coordinate getLandAt()
    {
        return landAt;
    }

    public void setLandAt(Coordinate landAt)
    {
        this.landAt = landAt;
    }

    public void initializeFromMissionPlane(PwcgGeneratedMissionPlaneData missionPlane)
    {        
        this.squadronId = missionPlane.getSquadronId();
        this.pilotSerialNumber = missionPlane.getPilotSerialNumber();
        this.planeSerialNumber = missionPlane.getPlaneSerialNumber();
        intializePilot(missionPlane.getPilotSerialNumber());
    }

    public void intializePilot(int serialNumber)
    {
        logPilot = new LogPilot();
        logPilot.setSerialNumber(serialNumber);
    }
    
    public void mapBotToCrew(String botId) throws PWCGException
    {
        if (logPilot != null)
        {
            logPilot.setBotId(botId);
        }
        else
        {
            Logger.log(LogLevel.ERROR, "While mapping bot = No crew member found for bot: " + botId);
        }
    }

    public boolean isWithPlane(String searchId)
    {
        if (getId().equals(searchId))
        {
            return true;
        }
        if (isBot(searchId))
        {
            return true;
        }
 
        return false;
    }

    public boolean isBot(String botId)
    {
        if (logPilot.getBotId().equals(botId))
        {
            return true;
        }
        
        return false;
    }

    public boolean isCrewMember(int serialNumber)
    {
        if (logPilot.getSerialNumber() == serialNumber)
        {
            return true;
        }
        
        return false;
    }

    public LogPilot getLogPilot()
    {
        return logPilot;
    }

    public boolean isCrashedInSight()
    {
        return crashedInSight;
    }

    public void setCrashedInSight(boolean crashedInSight)
    {
        this.crashedInSight = crashedInSight;
    }    

    public Integer getSquadronId()
    {
        return squadronId;
    }

    public void setSquadronId(Integer squadronId)
    {
        this.squadronId = squadronId;
    }

    public int getPilotSerialNumber()
    {
        return pilotSerialNumber;
    }

    public void setPilotSerialNumber(int pilotSerialNumber)
    {
        this.pilotSerialNumber = pilotSerialNumber;
    }

    public int getPlaneSerialNumber()
    {
        return planeSerialNumber;
    }

    public void setPlaneSerialNumber(int planeSerialNumber)
    {
        this.planeSerialNumber = planeSerialNumber;
    }

    public int getPlaneStatus()
    {
        return planeStatus;
    }

    public void setPlaneStatus(int planeStatus)
    {
        this.planeStatus = planeStatus;
    }
}
