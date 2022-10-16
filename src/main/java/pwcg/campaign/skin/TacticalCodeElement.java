package pwcg.campaign.skin;

public class TacticalCodeElement
{
    private String code;
    private String color;

    TacticalCodeElement(String code, String color)
    {
        this.code = code;
        this.color = color;
    }
    
    public String getCode()
    {
        return code;
    }

    public String getColor()
    {
        return color;
    }
}
