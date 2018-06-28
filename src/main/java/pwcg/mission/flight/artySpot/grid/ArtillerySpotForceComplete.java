package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;

public class ArtillerySpotForceComplete
{
    
    private McuTimer forceCompleteTimer = new McuTimer();
    private McuForceComplete forceComplete = new McuForceComplete();
    
    /**
     * @param position
     * @throws PWCGException 
     */
    public void create (Coordinate gridPosition, ArtillerySpotArtilleryGroup friendlyArtillery) throws PWCGException 
    {
        // Put the deactivate MCUs outside of the grid
        Coordinate deactivatePosition = MathUtils.calcNextCoord(gridPosition, 0, 2000);
        
        forceComplete.setName("GridForceComplete");       
        forceComplete.setDesc("Grid Force Complete"); 
        forceComplete.setPosition(deactivatePosition);
        
        forceCompleteTimer.setName("GridForceComplete Timer");       
        forceCompleteTimer.setDesc("Grid Force Complete Timer"); 
        forceCompleteTimer.setTimer(0);
        forceCompleteTimer.setPosition(deactivatePosition);
        forceCompleteTimer.setTarget(forceComplete.getIndex());

        forceArtilleryComplete(friendlyArtillery);
     }
    

    /**
     * @param writer
     * @throws PWCGIOException 
     */
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        forceCompleteTimer.write(writer);
        forceComplete.write(writer);
    }

    /**
     * @param objectEntityIndex
     */
    private void forceArtilleryComplete(ArtillerySpotArtilleryGroup friendlyArtillery)
    {
        forceComplete.setObject(friendlyArtillery.getLeadIndex());
    }


    /**s
     * @return the forceCompleteTimer
     */
    public McuTimer getForceCompleteTimer()
    {
        return this.forceCompleteTimer;
    }
    
    
}
