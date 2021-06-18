package pwcg.campaign.newspapers;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.core.utils.DateUtils;

public class Newspaper
{
    private String imageName = "";
    private String headline = "";
    private Side side = Side.NEUTRAL;
    private Date newspaperEventDate;

    public String getImageName()
    {
        return imageName;
    }

    public String getHeadline()
    {
        return headline;
    }

    public Side getSide()
    {
        return side;
    }

    public Date getNewspaperEventDate()
    {
        return newspaperEventDate;
    }
    
    public String formNewspaperPictureName()
    {
        return DateUtils.getDateStringYYYYMMDD(newspaperEventDate) + side + ".jpg";
    }

}
