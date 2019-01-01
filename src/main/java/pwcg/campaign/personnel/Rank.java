package pwcg.campaign.personnel;

public class Rank
{
    private int rankId = 0;
    private String rankName = "";
    private String rankAbbrev = "";
    private int rankCountry = 0;
    private int rankService = 0;

    public int getRankId()
    {
        return rankId;
    }

    public void setRankId(int rankId)
    {
        this.rankId = rankId;
    }

    public String getRankName()
    {
        return rankName;
    }

    public void setRankName(String rankName)
    {
        this.rankName = rankName;
    }

    public String getRankAbbrev()
    {
        return rankAbbrev;
    }

    public void setRankAbbrev(String rankAbbrev)
    {
        this.rankAbbrev = rankAbbrev;
    }

    public int getRankCountry()
    {
        return rankCountry;
    }

    public void setRankCountry(int rankCountry)
    {
        this.rankCountry = rankCountry;
    }

    public int getRankService()
    {
        return rankService;
    }

    public void setRankService(int rankService)
    {
        this.rankService = rankService;
    }

}
