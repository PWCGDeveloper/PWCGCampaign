package pwcg.gui.colors;

import java.awt.Color;
import java.util.Date;

import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public interface IServiceColorMap
{
    Color getColorForRole(PwcgRoleCategory roleCategory);

    Color getColorForSquadron(Squadron squadron, Date date) throws PWCGException;
}