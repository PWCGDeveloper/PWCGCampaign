package pwcg.gui.colors;

import java.awt.Color;

import pwcg.campaign.tank.PwcgRoleCategory;

public class FrenchNavyColorMap extends ServiceColor implements IServiceColorMap
{
    public static final Color RECON_COLOR = new Color(45, 149, 210);
    public static final Color FIGHTER_COLOR = new Color(45, 182, 210);
    
    public Color getColorForRole(PwcgRoleCategory roleCategory)
    {
        if (roleCategory == PwcgRoleCategory.FIGHTER)
        {
            return FIGHTER_COLOR;            
        }
        
        return RECON_COLOR;
    }
}
