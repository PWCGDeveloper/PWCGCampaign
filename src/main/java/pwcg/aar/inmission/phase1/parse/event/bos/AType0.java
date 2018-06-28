package pwcg.aar.inmission.phase1.parse.event.bos;

import java.io.BufferedWriter;

import pwcg.aar.inmission.phase1.parse.event.ATypeBase;
import pwcg.aar.inmission.phase1.parse.event.IAType0;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

// T:0 AType:0 GDate:1917.7.1 GTime:16:30:0 MFile:Missions/Alb Test 1917-07-01.mission MID: GType:0 CNTRS:0:0,101:1,102:1,103:1,104:1,105:1,501:2,502:2,600:3 SETTS:01010100100100010000000110 MODS:1 PRESET:0
// T:0 AType:0 GDate:1941.10.1 GTime:11:30:0 MFile:missions/heinkel mann 1941-10-01.mission MID: GType:0 CNTRS:0:0,101:1,201:2 SETTS:111111011110100100000001110 MODS:0 PRESET:1 AQMID:0 ROUNDS: 1 POINTS: 500
public class AType0 extends ATypeBase implements IAType0
{
    private String campaignName = "";
    private String missionFileName = "";


    public AType0(String campaignName, String line)  throws PWCGException
    {
        super();
        this.campaignName = campaignName;
        parse(line);
    }
    
    private void parse (String line)  throws PWCGException
	{
        String startTag = findMissionFileNameStartTag(line);
        if (startTag.isEmpty())
        {
        	return;
        }
        
		missionFileNameByMissionFile(line, startTag);
        if (missionFileName.isEmpty())
        {
            missionFileNameByMsbinFile(line, startTag);
        }
	}

	private String findMissionFileNameStartTag(String line)
	{
		String startTag = campaignName;
        if (!line.contains(startTag))
        {
            startTag = campaignName.toLowerCase();
            if (!line.contains(startTag))
            {
                return "";
            }
        }
		return startTag;
	}

	private void missionFileNameByMsbinFile(String line, String startTag)
	{
		String endTag;
		endTag = ".msnbin";
		if (line.contains(startTag) && line.contains(endTag))
		{
		    Logger.log(LogLevel.DEBUG, "AType0 line is:" + line);
		    missionFileName = campaignName + getString(line, startTag, endTag);
		}
	}

	private void missionFileNameByMissionFile(String line, String startTag)
	{
		String endTag = ".mission";
        if (line.contains(startTag) && line.contains(endTag))
        {
            Logger.log(LogLevel.DEBUG, "AType0 line is:" + line);
            missionFileName = campaignName + getString(line, startTag, endTag);
        }
	}

    @Override
    public String getMissionFileName()
    {
        return missionFileName;
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
    }

    @Override
    protected String getString(String line, String startTag, String endTag)
    {
        try
        {
            return super.getString(line, startTag, endTag);
        }
        catch (Exception e)
        {
            Logger.log(LogLevel.DEBUG, "Exception on start tag " + startTag);
            return "";
        }
    }
}
