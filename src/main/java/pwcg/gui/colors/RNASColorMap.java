package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;

public class RNASColorMap extends ServiceColor
{
    public static final Color BOMBER_COLOR = new Color(168, 48, 79);
    public static final Color RECON_COLOR = new Color(198, 57, 93);
    public static final Color FIGHTER_COLOR = new Color(223, 65, 107);
    
    /**
     * @param role
     * @return
     */
    public Color getColorForRole(Role role)
    {
        if (role == Role.ROLE_STRAT_BOMB)
        {
            return BOMBER_COLOR;
        }
        if (role.isRoleCategory(RoleCategory.FIGHTER))
        {
            return FIGHTER_COLOR;            
        }
        
        return RECON_COLOR;
    }
}
