package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;


public class ArtillerySpotForceComplete
{
    
    private McuTimer forceCompleteTimer = new McuTimer();
    private McuForceComplete forceComplete = new McuForceComplete(WaypointPriority.PRIORITY_HIGH, 0);
    private Campaign campaign;

    public ArtillerySpotForceComplete (Campaign campaign)
    {
    	this.campaign = campaign;
    }

    public void create (Coordinate gridPosition, ArtillerySpotArtilleryGroup friendlyArtillery) throws PWCGException 
    {
        // Put the deactivate MCUs outside of the grid
        Coordinate deactivatePosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), gridPosition, 0, 2000);
        
        forceComplete.setName("GridForceComplete");       
        forceComplete.setDesc("Grid Force Complete"); 
        forceComplete.setPosition(deactivatePosition);
        
        forceCompleteTimer.setName("GridForceComplete Timer");       
        forceCompleteTimer.setDesc("Grid Force Complete Timer"); 
        forceCompleteTimer.setTime(0);
        forceCompleteTimer.setPosition(deactivatePosition);
        forceCompleteTimer.setTimerTarget(forceComplete.getIndex());

        forceArtilleryComplete(friendlyArtillery);
     }
    

    public void write(BufferedWriter writer) throws PWCGException 
    {
        forceCompleteTimer.write(writer);
        forceComplete.write(writer);
    }

    private void forceArtilleryComplete(ArtillerySpotArtilleryGroup friendlyArtillery)
    {
        forceComplete.setObject(friendlyArtillery.getLeadIndex());
    }

    public McuTimer getForceCompleteTimer()
    {
        return this.forceCompleteTimer;
    }
    
    
}
