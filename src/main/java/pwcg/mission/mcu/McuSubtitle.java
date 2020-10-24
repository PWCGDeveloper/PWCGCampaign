package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import pwcg.campaign.utils.LCIndexGenerator;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.MissionStringHandler;

public class McuSubtitle extends BaseFlightMcu
{
	private int lcText = LCIndexGenerator.getInstance().getNextIndex();
    private String text;
    private int duration = 3;
    private boolean useSubtitles = true;

	public McuSubtitle ()
	{
 		super();
	}

	public int getLcText() 
	{
		return lcText;
	}

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}

	public int getDuration()
    {
        return this.duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public void write(BufferedWriter writer) throws PWCGIOException
	{
        if (!useSubtitles)
        {
            return;
        }
        
		try
        {
            writer.write("MCU_TR_Subtitle");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);
            
            
            writer.write("  Enabled = 1;");
            writer.newLine();
            
            writer.write("  SubtitleInfo");
            writer.newLine();
            
            writer.write("  {");
            writer.newLine();
            
            writer.write("    Duration = " + duration + ";");
            writer.newLine();
            
            writer.write("    FontSize = 20;");
            writer.newLine();
            
            writer.write("    HAlign = 1;");
            writer.newLine();
            
            writer.write("    VAlign = 2;");
            writer.newLine();
            
            writer.write("    RColor = 255;");
            writer.newLine();
            
            writer.write("    GColor = 255;");
            writer.newLine();
            
            writer.write("    BColor = 255;");
            writer.newLine();
            
            writer.write("    LCText = " + lcText + ";");
            writer.newLine();
            
            writer.write("  }");
            writer.newLine();
            
            
            writer.write("  Coalitions = [0, 1, 2, 3, 4, 5, 6, 7];");
            writer.newLine();
            		  
            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}	
    

    public static McuSubtitle makeActivatedSubtitle(String subtitleText, Coordinate position)
    {
        McuSubtitle subtitle = new McuSubtitle();
        subtitle.setName("Subtitle");
        subtitle.setText(subtitleText);
        subtitle.setPosition(position);
        subtitle.setDuration(5);
        
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.registerMissionText(subtitle.getLcText(), subtitle.getText());
        return subtitle;
    }


    public static void writeSubtitles(List<McuSubtitle> subTitleList, BufferedWriter writer) throws PWCGIOException
    {
        for (int i = 0; i < subTitleList.size(); ++i)
        {
            try
            {
                McuSubtitle subtitle = subTitleList.get(i);
                subtitle.write(writer);
                writer.newLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
