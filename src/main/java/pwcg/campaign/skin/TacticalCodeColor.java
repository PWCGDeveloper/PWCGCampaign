package pwcg.campaign.skin;

public enum TacticalCodeColor
{
    BLACK("0"),
    WHITE("1"),
    RED("2"),
    BLUE("3"),
    YELLOW("4"),
    GREEN("5"),
    SKY("6");

    private String colorStr;

    TacticalCodeColor(String str)
    {
        colorStr = str;
    }

    public String getColorCode()
    {
        return colorStr;
    }
}
