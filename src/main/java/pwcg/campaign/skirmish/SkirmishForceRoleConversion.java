package pwcg.campaign.skirmish;

import pwcg.campaign.api.Side;
import pwcg.campaign.tank.PwcgRole;

public class SkirmishForceRoleConversion
{
    Side side;
    PwcgRole fromRole;
    PwcgRole toRole;

    public Side getSide()
    {
        return side;
    }

    public PwcgRole getFromRole()
    {
        return fromRole;
    }

    public PwcgRole getToRole()
    {
        return toRole;
    }

}
