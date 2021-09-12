package pwcg.gui.colors;

import javafx.scene.paint.Color;

import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;

public class RAFColorMap extends ServiceColor implements IServiceColorMap
{
    public static final Color BOMBER_COLOR = new Color(100, 26, 9);
    public static final Color RECON_COLOR = new Color(191, 26, 9);
    public static final Color FIGHTER_COLOR = new Color(223, 26, 9);
    
    /* (non-Javadoc)
     * @see pwcg.gui.colors.IColorMap#getColorForRole(pwcg.campaign.plane.PlaneRole.Role)
     */
    @Override
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
