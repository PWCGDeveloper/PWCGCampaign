package pwcg.gui.colors;

import javafx.scene.paint.Color;

import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;

public class AmericanColorMap extends ServiceColor implements IServiceColorMap
{
    public static final Color RECON_COLOR = new Color(50, 60, 80);
    public static final Color FIGHTER_COLOR = new Color(80, 95, 130);
    
    /* (non-Javadoc)
     * @see pwcg.gui.colors.IColorMap#getColorForRole(pwcg.campaign.plane.PlaneRole.Role)
     */
    @Override
    public Color getColorForRole(Role role)
    {
        if (role.isRoleCategory(RoleCategory.FIGHTER))
        {
            return FIGHTER_COLOR;            
        }
        
        return RECON_COLOR;
    }
}
