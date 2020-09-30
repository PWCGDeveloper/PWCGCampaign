package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.CoalitionFactory;
import pwcg.mission.mcu.McuDelete;
import pwcg.mission.mcu.McuProximity;
import pwcg.mission.mcu.McuTimer;

public class PlaneRemoverCoop implements IPlaneRemover
{
    protected McuProximity outOfEnemyRangeProximity = null;
    protected McuTimer outOfEnemyRangeProximityTimer = new McuTimer();
    
    protected McuTimer deletePlaneTimer = new McuTimer();
    protected McuDelete deletePlane = new McuDelete();
        
    protected int index = IndexGenerator.getInstance().getNextIndex();;
    
    public PlaneRemoverCoop()
    {
        index = IndexGenerator.getInstance().getNextIndex();
    }

    public void initialize(IFlight flight, PlaneMcu planeToRemove, PlaneMcu playerPlane) throws PWCGException 
    {
        initialize(flight, planeToRemove);
    }
    
    protected void initialize(IFlight flight, PlaneMcu planeToRemove) throws PWCGException 
    {
        outOfEnemyRangeProximity = new McuProximity();
        outOfEnemyRangeProximity.addCoalition(CoalitionFactory.getFriendlyCoalition(planeToRemove.getCountry()));
        outOfEnemyRangeProximity.addCoalition(CoalitionFactory.getEnemyCoalition(planeToRemove.getCountry()));
        
        // set position
        outOfEnemyRangeProximityTimer.setPosition(planeToRemove.getPosition().copy());
        outOfEnemyRangeProximity.setPosition(planeToRemove.getPosition().copy());
        deletePlaneTimer.setPosition(planeToRemove.getPosition().copy());
        deletePlane.setPosition(planeToRemove.getPosition().copy());

        // set name
        outOfEnemyRangeProximityTimer.setName("outOfEnemyRangeProximityTimer");
        outOfEnemyRangeProximity.setName("outOfEnemyRangeProximity");
        deletePlaneTimer.setName("deletePlaneTimer");
        deletePlane.setName("deletePlane");

        // set name
        outOfEnemyRangeProximityTimer.setDesc("outOfEnemyRangeProximityTimer");
        outOfEnemyRangeProximity.setDesc("outOfEnemyRangeProximity");
        deletePlaneTimer.setDesc("deletePlaneTimer");
        deletePlane.setDesc("deletePlane");
        
        // Timer values
        outOfEnemyRangeProximityTimer.setTimer(60);
        deletePlaneTimer.setTimer(3);
        
        // Coalition is enemy
        ConfigManagerCampaign configManager = flight.getCampaign().getCampaignConfigManager();

        // Enemy proximity is based on coalition
        int enemyDistance = configManager.getIntConfigParam(ConfigItemKeys.PlaneDeleteEnemyDistanceKey);
        outOfEnemyRangeProximity.setCloser(0);
        outOfEnemyRangeProximity.setDistance(enemyDistance);
        
        // Link up targets
        outOfEnemyRangeProximityTimer.setTarget(outOfEnemyRangeProximity.getIndex());
        outOfEnemyRangeProximity.setTarget(deletePlaneTimer.getIndex());
        deletePlaneTimer.setTarget(deletePlane.getIndex());
        deletePlane.setObject(planeToRemove.getEntity().getIndex());

        // Link only the target plane for coop
        outOfEnemyRangeProximity.setObject(planeToRemove.getEntity().getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        try
        {

            if (deletePlane.getObjects().size() == 0)
            {
                return;
            }

            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"Plane Remover\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Plane Remover\";");
            writer.newLine();
            
            outOfEnemyRangeProximityTimer.write(writer);
            outOfEnemyRangeProximity.write(writer);
            
            deletePlaneTimer.write(writer);
            deletePlane.write(writer);
            
            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public McuTimer getDeletePlaneTimer()
    {
        return deletePlaneTimer;
    }

    public McuTimer getEntryPoint()
    {
        return this.outOfEnemyRangeProximityTimer;
    }

}
