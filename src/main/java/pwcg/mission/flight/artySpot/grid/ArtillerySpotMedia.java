package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuMedia;
import pwcg.mission.mcu.McuTimer;

public class ArtillerySpotMedia
{
    private McuTimer startMediaTimer = new McuTimer();
    private McuTimer stopMediaTimer = new McuTimer();
    private McuMedia startMedia = null;
    private McuMedia stopMedia = null;
    
    public static final Integer[] rowEventId =    {14, 42, 43, 44, 45, 46, 47, 48};
    public static final Integer[] columnEventId = {49, 50, 51, 52, 53, 54, 55, 56};

    /**
     * @throws PWCGException 
     * 
     */
    public void createMedia(ArtillerySpotGrid artySpotGrid, Coordinate gridPosition) throws PWCGException
    {
        Coordinate mediaPosition = MathUtils.calcNextCoord(gridPosition, 0, 2000);
   
        // Timer values
        startMediaTimer.setName("Spot Start Media Timer");
        startMediaTimer.setPosition(mediaPosition);
        startMediaTimer.setTimer(3);

        stopMediaTimer.setName("Spot Stop Media Timer");
        stopMediaTimer.setPosition(mediaPosition);
        stopMediaTimer.setTimer(2);

        startMedia = new McuMedia(McuMedia.MEDIA_TYPE_START);
        startMedia.setConfig("swf\\artcorrection.gfx");
        startMedia.setName("GridMediaStart");        
        startMedia.setDesc("Grid Media Start");      
        startMedia.setExpandTime(3);
        startMedia.setFadeTime(3);
        startMedia.setPosition(mediaPosition);

        stopMedia = new McuMedia(McuMedia.MEDIA_TYPE_STOP);
        stopMedia.setConfig("");
        stopMedia.setName("GridMediaStop");     
        stopMedia.setDesc("Grid Media Stop");       
        stopMedia.setExpandTime(1);
        stopMedia.setFadeTime(1);
        stopMedia.setPosition(mediaPosition);

        createMediaEvents(artySpotGrid);
    }


    /**
     * 
     */
    private void createMediaEvents(ArtillerySpotGrid artySpotGrid)
    {
        // Associate the media event with a header
        // The rest of the column
        for (int i = 0; i < ArtillerySpotGrid.GRID_ELEMENTS; ++i)
        {
            McuEvent event = new McuEvent();
            event.setType(rowEventId[i]);  // Don't ask why these values
            
            // Associate the media event with a column header
            ArtillerySpotActivate[] rowActivates = artySpotGrid.getArtillerySpotActivateSet().getRowActivates();
            event.setTarId(rowActivates[i].getActivateTimer().getIndex());
            
            startMedia.addEvent(event);
        }
        
        for (int i = 0; i < ArtillerySpotGrid.GRID_ELEMENTS; ++i)
        {
            McuEvent event = new McuEvent();
            event.setType(columnEventId[i]);  // Don't ask why these values
            
            // Associate the media event with a row header
            ArtillerySpotActivate[] columnActivates = artySpotGrid.getArtillerySpotActivateSet().getColumnActivates();
            event.setTarId(columnActivates[i].getActivateTimer().getIndex());
            
            startMedia.addEvent(event);
        }
        
        
        // Start the media
        startMediaTimer.setTarget(startMedia.getIndex());
        stopMediaTimer.setTarget(stopMedia.getIndex());
    }


    /**
     * @param writer
     * @throws PWCGIOException 
     */
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        startMediaTimer.write(writer);
        startMedia.write(writer);

        stopMediaTimer.write(writer);
        stopMedia.write(writer);
    }


    /**
     * @return the startMediaTimer
     */
    public McuTimer getStartMediaTimer()
    {
        return this.startMediaTimer;
    }


    /**
     * @return the stopMediaTimer
     */
    public McuTimer getStopMediaTimer()
    {
        return this.stopMediaTimer;
    }


    /**
     * @return the startMedia
     */
    public McuMedia getStartMedia()
    {
        return this.startMedia;
    }


    /**
     * @return the stopMedia
     */
    public McuMedia getStopMedia()
    {
        return this.stopMedia;
    }
    
    

}
