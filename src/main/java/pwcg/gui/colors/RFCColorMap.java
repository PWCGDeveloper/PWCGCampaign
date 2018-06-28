package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.plane.Role;

public class RFCColorMap extends ServiceColor implements IServiceColorMap
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
        else if (role == Role.ROLE_FIGHTER)
        {
            return FIGHTER_COLOR;            
        }
        
        return RECON_COLOR;
    }
}
