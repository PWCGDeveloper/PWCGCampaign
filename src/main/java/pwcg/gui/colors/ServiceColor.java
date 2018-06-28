package pwcg.gui.colors;

import java.awt.Color;
import java.util.Date;

import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public abstract class ServiceColor implements IServiceColorMap
{
    public Color getColorForSquadron(Squadron squadron, Date date) throws PWCGException
    {
        Role primaryRole = squadron.determineSquadronPrimaryRole(date);

        return getColorForRole(primaryRole);
    }

    public abstract Color getColorForRole(Role primaryRole);
}
