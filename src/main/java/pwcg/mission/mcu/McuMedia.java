
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;

public class McuMedia extends BaseFlightMcu
{
	public static final int MEDIA_TYPE_UNKNOWN = -1;
	public static final int MEDIA_TYPE_START = 2;
	public static final int MEDIA_TYPE_STOP = 4;
	
	private int enabled = 1;
	private String config = "swf\\photorecon.gfx";
	private int totalTime = 10000;
	private int expandTime = 1;
	private int fadeTime = 1;
	private int opacity = 100;
	private int rColor = 255;
	private int gColor = 255;
	private int bColor = 255;
	private int mediaType = MEDIA_TYPE_UNKNOWN;
	private List<McuEvent> eventList = new ArrayList<McuEvent>();

    public McuMedia ()
    {
    }

    public McuMedia (int mediaType)
    {
        super();
        this.mediaType = mediaType;
    }

	public void write(BufferedWriter writer) throws PWCGIOException
	{
		try
        {
            writer.write("MCU_TR_Media");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writer.write("  Enabled = " + enabled + ";");
            writer.newLine();
            writer.write("  Config = \"" + config + "\";");
            writer.newLine();
            writer.write("  TotalTime = " + totalTime + ";");
            writer.newLine();
            writer.write("  ExpandTime = " + expandTime + ";");
            writer.newLine();
            writer.write("  FadeTime = " + fadeTime + ";");
            writer.newLine();
            writer.write("  Opacity = " + opacity + ";");
            writer.newLine();
            writer.write("  RColor = " + rColor + ";");
            writer.newLine();
            writer.write("  GColor = " + gColor + ";");
            writer.newLine();
            writer.write("  BColor = " + bColor + ";");
            writer.newLine();
            
            writer.write("  MediaType = " + mediaType + ";");
            writer.newLine();

            if (eventList.size() > 0)
            {
            	writer.write("  OnEvents");
            	writer.newLine();
            	writer.write("  {");
            	writer.newLine();
            	for (McuEvent event : eventList)
            	{
            		writer.write("      OnEvent");
            		writer.newLine();
            		writer.write("      {");
            		writer.newLine();
            		writer.write("          Type = " + event.getType() + ";");
            		writer.newLine();
            		writer.write("          TarId = " + event.getTarId() + ";");
            		writer.newLine();
            		writer.write("      }");
            		writer.newLine();
            		
            	}
            	writer.write("  }");
            	writer.newLine();
            }
            
            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public int getExpandTime() {
		return expandTime;
	}

	public void setExpandTime(int expandTime) {
		this.expandTime = expandTime;
	}

	public int getFadeTime() {
		return fadeTime;
	}

	public void setFadeTime(int fadeTime) {
		this.fadeTime = fadeTime;
	}

	public int getOpacity() {
		return opacity;
	}

	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}

	public int getrColor() {
		return rColor;
	}

	public void setrColor(int rColor) {
		this.rColor = rColor;
	}

	public int getgColor() {
		return gColor;
	}

	public void setgColor(int gColor) {
		this.gColor = gColor;
	}

	public int getbColor() {
		return bColor;
	}

	public void setbColor(int bColor) {
		this.bColor = bColor;
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public List<McuEvent> getEventList() {
		return eventList;
	}

	public void addEvent(McuEvent event) {
		this.eventList.add(event);
	}
	
}
