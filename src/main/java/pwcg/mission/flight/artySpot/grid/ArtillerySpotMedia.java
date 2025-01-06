package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
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
    private Campaign campaign;

    public static final Integer[] rowEventId =    {14, 42, 43, 44, 45, 46, 47, 48};
    public static final Integer[] columnEventId = {49, 50, 51, 52, 53, 54, 55, 56};

    public ArtillerySpotMedia (Campaign campaign)
    {
    	this.campaign = campaign;
    }

    public void createMedia(ArtillerySpotGrid artySpotGrid, Coordinate gridPosition) throws PWCGException
    {
        Coordinate mediaPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), gridPosition, 0, 2000);
   
        // Timer values
        startMediaTimer.setName("Spot Start Media Timer");
        startMediaTimer.setPosition(mediaPosition);
        startMediaTimer.setTime(3);

        stopMediaTimer.setName("Spot Stop Media Timer");
        stopMediaTimer.setPosition(mediaPosition);
        stopMediaTimer.setTime(2);

        startMedia = new McuMedia(McuMedia.MEDIA_TYPE_START);
        startMedia.setConfig("swf\\extensions\\artcorrection");
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
            // Associate the media event with a column header
            ArtillerySpotActivate[] rowActivates = artySpotGrid.getArtillerySpotActivateSet().getRowActivates();
            McuEvent event = new McuEvent(rowEventId[i], rowActivates[i].getActivateTimer().getIndex()); // Don't ask why these values
            startMedia.addEvent(event);
        }
        
        for (int i = 0; i < ArtillerySpotGrid.GRID_ELEMENTS; ++i)
        {
            // Associate the media event with a row header
            ArtillerySpotActivate[] columnActivates = artySpotGrid.getArtillerySpotActivateSet().getColumnActivates();
            McuEvent event = new McuEvent(columnEventId[i], columnActivates[i].getActivateTimer().getIndex());  // Don't ask why these values
            startMedia.addEvent(event);
        }
        
        
        // Start the media
        startMediaTimer.setTimerTarget(startMedia.getIndex());
        stopMediaTimer.setTimerTarget(stopMedia.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        startMediaTimer.write(writer);
        startMedia.write(writer);

        stopMediaTimer.write(writer);
        stopMedia.write(writer);
    }

    public McuTimer getStartMediaTimer()
    {
        return this.startMediaTimer;
    }

    public McuTimer getStopMediaTimer()
    {
        return this.stopMediaTimer;
    }

    public McuMedia getStartMedia()
    {
        return this.startMedia;
    }

    public McuMedia getStopMedia()
    {
        return this.stopMedia;
    }
}
