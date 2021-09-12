package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.plane.RoleCategory;

public class GermanNavyColorMap extends ServiceColor implements IServiceColorMap
{
    public static final Color RECON_COLOR = new Color(120, 105, 90);
    public static final Color FIGHTER_COLOR = new Color(150, 140, 120);
    
    /* (non-Javadoc)
     * @see pwcg.gui.colors.IColorMap#getColorForRole(pwcg.campaign.plane.PlaneRole.Role)
     */
    @Override
    public Color getColorForRole(PwcgRole role)
    {
        if (role.isRoleCategory(RoleCategory.FIGHTER))
        {
            return FIGHTER_COLOR;            
        }
        
        return RECON_COLOR;
    }
}
