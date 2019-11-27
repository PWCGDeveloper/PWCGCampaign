package pwcg.coop.model;

import pwcg.campaign.squadmember.SquadronMemberStatus;

public class CoopDisplayRecord
{
    private String username = "unknown";
    private String pilorNameAndRank = "unknown";
    private String campaignName = "unknown";
    private String squadronName = "unknown";
    private int pilotStatus = SquadronMemberStatus.STATUS_ACTIVE;
    private int pilotSerialNumber = 0;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPilotNameAndRank()
    {
        return pilorNameAndRank;
    }

    public void setPilorNameAndRank(String pilorNameAndRank)
    {
        this.pilorNameAndRank = pilorNameAndRank;
    }

    public String getCampaignName()
    {
        return campaignName;
    }

    public void setCampaignName(String campaignName)
    {
        this.campaignName = campaignName;
    }

    public String getSquadronName()
    {
        return squadronName;
    }

    public void setSquadronName(String squadronName)
    {
        this.squadronName = squadronName;
    }

    public int getPilotStatus()
    {
        return pilotStatus;
    }

    public void setPilotStatus(int pilotStatus)
    {
        this.pilotStatus = pilotStatus;
    }

    public int getPilotSerialNumber()
    {
        return pilotSerialNumber;
    }

    public void setPilotSerialNumber(int pilotSerialNumber)
    {
        this.pilotSerialNumber = pilotSerialNumber;
    }
}
