package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.tank.PwcgRoleCategory;

public class RNASColorMap extends ServiceColor
{
    public static final Color BOMBER_COLOR = new Color(168, 48, 79);
    public static final Color RECON_COLOR = new Color(198, 57, 93);
    public static final Color FIGHTER_COLOR = new Color(223, 65, 107);
    
    @Override
    public Color getColorForRole(PwcgRoleCategory roleCategory)
    {
        if (roleCategory == PwcgRoleCategory.BOMBER)
        {
            return BOMBER_COLOR;
        }
        if (roleCategory == PwcgRoleCategory.FIGHTER)
        {
            return FIGHTER_COLOR;            
        }
        
        return RECON_COLOR;
    }
}
