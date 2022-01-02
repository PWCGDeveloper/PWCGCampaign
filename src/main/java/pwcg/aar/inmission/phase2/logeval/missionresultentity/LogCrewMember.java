package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.crewmember.CrewMemberStatus;

public class LogCrewMember
{
    private int serialNumber;
    private String botId = "";
    private double damageLevel = 0.0;
    private int status = CrewMemberStatus.STATUS_ACTIVE;

    public LogCrewMember()
    {
    }

    public void dump(BufferedWriter writer) throws IOException
    {
        writer.write("        Bot   : " + getBotId());
        writer.newLine();
        writer.write("        Status: " + getStatus());
        writer.newLine();
        writer.newLine();
    }

    public String getBotId()
    {
        return botId;
    }

    public void setBotId(String botId)
    {
        this.botId = botId;
    }

    public double getDamageLevel()
    {
        return damageLevel;
    }

    public void setDamageLevel(double damageLevel)
    {
        this.damageLevel = damageLevel;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }
}
