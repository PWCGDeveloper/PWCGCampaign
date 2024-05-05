package pwcg.campaign;

public class CampaignPilotGeneratorModel
{
    private ArmedService service;
    private String playerName = "";
    private String playerRank = "";
    private String playerRegion = "";

    public ArmedService getService()
    {
        return service;
    }

    public void setService(ArmedService service)
    {
        this.service = service;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public String getPlayerRank()
    {
        return playerRank;
    }

    public void setPlayerRank(String playerRank)
    {
        this.playerRank = playerRank;
    }

    public String getPlayerRegion()
    {
        return playerRegion;
    }

    public void setPlayerRegion(String playerRegion)
    {
        this.playerRegion = playerRegion;
    }
}
