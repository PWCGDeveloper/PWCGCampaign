package pwcg.campaign.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.exception.PWCGMissionDebriefException;
import pwcg.core.utils.Logger;

public class AutoStart
{
    private String campaignName = "";
    private String missionFileName = "";

    public String getCampaignName()
    {
        return campaignName;
    }

    public void write() throws PWCGIOException
    {
        BufferedWriter tempMissionFileWriter;
        try
        {
            String dir = System.getProperty("user.dir");
            String tempMissionFile = dir + "\\autostart.tmp";
            tempMissionFileWriter = new BufferedWriter(new FileWriter(tempMissionFile));
            tempMissionFileWriter.write("Missions\\" + missionFileName);
            tempMissionFileWriter.newLine();
            tempMissionFileWriter.write(campaignName);
            tempMissionFileWriter.close();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public void read() throws PWCGIOException
    {
        try
        {
            String dir = System.getProperty("user.dir");
            String tempMissionFilePath = dir + "\\autostart.tmp";

            File tempMissionFile = new File(tempMissionFilePath);
            if (tempMissionFile.exists())
            {
                BufferedReader tempMissionFileReader = new BufferedReader(new FileReader(tempMissionFilePath));
                missionFileName = tempMissionFileReader.readLine();
                campaignName = tempMissionFileReader.readLine();
                tempMissionFileReader.close();
            }
            else
            {
                throw new PWCGMissionDebriefException("Started with -debfief but no autostart file was found");
            }
        }
        catch (FileNotFoundException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
        catch (PWCGMissionDebriefException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public void setCampaignName(String campaignName)
    {
        this.campaignName = campaignName;
    }

    public String getMissionFileName()
    {
        return missionFileName;
    }

    public void setMissionFileName(String missionFileName)
    {
        this.missionFileName = missionFileName;
    }

}
