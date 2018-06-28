package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.plane.Role;

public class FrenchNavyColorMap extends ServiceColor implements IServiceColorMap
{
    public static final Color RECON_COLOR = new Color(45, 149, 210);
    public static final Color FIGHTER_COLOR = new Color(45, 182, 210);
    
    /* (non-Javadoc)
     * @see pwcg.gui.colors.IColorMap#getColorForRole(pwcg.campaign.plane.PlaneRole.Role)
     */
    @Override
    public Color getColorForRole(Role role)
    {
        if (role == Role.ROLE_FIGHTER)
        {
            return FIGHTER_COLOR;            
        }
        
        return RECON_COLOR;
    }
}
