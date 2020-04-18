package pwcg.gui.rofmap.brief;

import java.util.Map;

import pwcg.core.exception.PWCGException;

public interface IBriefingSquadronSelectedCallback
{
    void squadronsSelectedChanged(Map<Integer, String> selectedSquadrons) throws PWCGException;
}
