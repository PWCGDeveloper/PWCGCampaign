package pwcg.coop.model;

public class CoopPersonaOld
{
    private String username;
    private String campaignName;
    private String pilotName;
    private String pilotRank;
    private int serialNumber;
    private int squadronId;
    private boolean approved;
    private String note;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getCampaignName()
    {
        return campaignName;
    }

    public void setCampaignName(String campaignName)
    {
        this.campaignName = campaignName;
    }

    public String getPilotName()
    {
        return pilotName;
    }

    public void setPilotName(String pilotName)
    {
        this.pilotName = pilotName;
    }
    
    public String getPilotRank()
    {
        return pilotRank;
    }

    public void setPilotRank(String pilotRank)
    {
        this.pilotRank = pilotRank;
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public int getSquadronId()
    {
        return squadronId;
    }

    public void setSquadronId(int squadronId)
    {
        this.squadronId = squadronId;
    }

    public boolean isApproved()
    {
        return approved;
    }

    public void setApproved(boolean approved)
    {
        this.approved = approved;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }
}
