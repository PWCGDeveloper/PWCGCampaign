package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.plane.Plane;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuMessage;
import pwcg.mission.mcu.McuMissionObjective;
import pwcg.mission.mcu.McuTimer;

/**
 * @author Patrick Wilson
 *
 */
public class MissionObjectiveGroup
{
    private McuMissionObjective missionObjective = new McuMissionObjective();
    private MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
    private McuTimer missionObjectiveTimer = new McuTimer();
    
    private int index = IndexGenerator.getInstance().getNextIndex();;
    
    public MissionObjectiveGroup()
    {
        index = IndexGenerator.getInstance().getNextIndex();
    }
    
    /**
     * In the success scenario we trigger success immediately, thus making missions
     * a success by default.
     * @throws PWCGException 
     * 
     * @
     */
    public void createSuccessMissionObjective() throws PWCGException 
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        Coordinate squadronLocation = campaign.getPosition();
        
        missionBeginUnit.initialize(squadronLocation);
        
        missionObjective.setCoalition(campaign.determineCountry());
        missionObjective.setSuccess(1);
        missionObjective.setPosition(squadronLocation);

        missionBeginUnit.initialize(campaign.determineSquadron().determineCurrentAirfieldCurrentMap(campaign.getDate()).getPosition());
        missionObjectiveTimer.setPosition(squadronLocation);
        missionBeginUnit.linkToMissionBegin(missionObjectiveTimer.getIndex());
        missionObjectiveTimer.setTarget(missionObjective.getIndex());
    }
    
    /**
     * The failure scenario is triggered by the loss of your plane
     * 
     * @param myPlane
     * @throws PWCGException 
     * @
     */
    public void createFailureMissionObjective(Plane myPlane) throws PWCGException 
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        Coordinate squadronLocation = campaign.getPosition();

        missionBeginUnit.initialize(squadronLocation);

        missionObjective.setCoalition(campaign.determineCountry());
        missionObjective.setPosition(squadronLocation);
        missionObjective.setSuccess(0);

        myPlane.getEntity().setOnMessages(
                        McuMessage.ONKILL,
                        missionBeginUnit.getStartTimeindex(),
                        missionObjectiveTimer.getIndex());
        
        missionObjectiveTimer.setTarget(missionObjective.getIndex());
        missionObjectiveTimer.setPosition(squadronLocation);

        McuEvent planeDamagedEvent = new McuEvent();
        planeDamagedEvent.setType(McuEvent.ONPLANECRASHED);
        planeDamagedEvent.setTarId(missionObjectiveTimer.getIndex());
        myPlane.getEntity().addEvent(planeDamagedEvent);
    }
    

    /**
     * Write the mission objective group
     * 
     * @param writer
     * @throws PWCGException 
     * @
     */
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
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

}
