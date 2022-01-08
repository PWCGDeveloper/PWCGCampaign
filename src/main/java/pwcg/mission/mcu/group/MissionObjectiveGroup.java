package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuMessage;
import pwcg.mission.mcu.McuMissionObjective;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.playerunit.PlayerUnit;
import pwcg.mission.playerunit.TankMcu;

public class MissionObjectiveGroup
{
    private McuMissionObjective missionObjective = new McuMissionObjective();
    private MissionBeginUnit missionBeginUnit;
    private McuTimer missionObjectiveTimer = new McuTimer();
    
    private int index = IndexGenerator.getInstance().getNextIndex();;
    
    public MissionObjectiveGroup()
    {
        index = IndexGenerator.getInstance().getNextIndex();
    }

    public void createSuccessMissionObjective(Campaign campaign, Mission mission) throws PWCGException 
    {
        PlayerUnit playerUnit = mission.getUnits().getReferencePlayerUnit();
        Coordinate squadronLocation = playerUnit.getCompany().determineCurrentPosition(campaign.getDate());
        missionBeginUnit = new MissionBeginUnit(squadronLocation.copy());            
                
        missionObjective.setCoalition(playerUnit.getCompany().getCountry());
        missionObjective.setSuccess(1);
        missionObjective.setPosition(squadronLocation);

        missionObjectiveTimer.setPosition(squadronLocation);
        missionBeginUnit.linkToMissionBegin(missionObjectiveTimer.getIndex());
        missionObjectiveTimer.setTarget(missionObjective.getIndex());
    }

    public void createFailureMissionObjective(Campaign campaign, Mission mission) throws PWCGException 
    {
        PlayerUnit playerUnit = mission.getUnits().getReferencePlayerUnit();
        Coordinate squadronLocation = playerUnit.getCompany().determineCurrentPosition(campaign.getDate());
        missionBeginUnit = new MissionBeginUnit(squadronLocation.copy());            

        missionObjective.setCoalition(playerUnit.getCompany().getCountry());
        missionObjective.setPosition(squadronLocation);
        missionObjective.setSuccess(0);

        TankMcu referenceVehicle = mission.getUnits().getReferencePlayerUnit().getTanks().get(0);
        referenceVehicle.setOnMessages(
                        McuMessage.ONKILL,
                        missionBeginUnit.getStartTimeindex(),
                        missionObjectiveTimer.getIndex());
        
        missionObjectiveTimer.setTarget(missionObjective.getIndex());
        missionObjectiveTimer.setPosition(squadronLocation);

        McuEvent planeDamagedEvent = new McuEvent(McuEvent.ONPLANECRASHED, missionObjectiveTimer.getIndex());
        referenceVehicle.addEvent(planeDamagedEvent);
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"Mission Objective\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Mission Objective\";");
            writer.newLine();
            
            missionObjective.write(writer);
            missionBeginUnit.write(writer);
            missionObjectiveTimer.write(writer);

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

}
