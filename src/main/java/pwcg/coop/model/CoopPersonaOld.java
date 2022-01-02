package pwcg.coop.model;

public class CoopPersonaOld
{
    private String username;
    private String campaignName;
    private String crewMemberName;
    private String crewMemberRank;
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

    public String getCrewMemberName()
    {
        return crewMemberName;
    }

    public void setCrewMemberName(String crewMemberName)
    {
        this.crewMemberName = crewMemberName;
    }
    
    public String getCrewMemberRank()
    {
        return crewMemberRank;
    }

    public void setCrewMemberRank(String crewMemberRank)
    {
        this.crewMemberRank = crewMemberRank;
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
