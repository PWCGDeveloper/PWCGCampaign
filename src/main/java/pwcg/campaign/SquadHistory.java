package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class SquadHistory
{
    private List<SquadHistoryEntry> squadHistoryEntries = new ArrayList<>();

	public SquadHistoryEntry getSquadHistoryEntry(Date date) throws PWCGException
	{
	    SquadHistoryEntry selectedSquadHistoryEntry = null;
	    for (SquadHistoryEntry squadHistoryEntry :squadHistoryEntries)
	    {
	        Date squadHistoryDate = DateUtils.getDateYYYYMMDD(squadHistoryEntry.getDate());
	        if (!date.before(squadHistoryDate))
	        {
	            selectedSquadHistoryEntry = squadHistoryEntry;
	        }
	    }
		return selectedSquadHistoryEntry;
	}

    public List<SquadHistoryEntry> getSquadHistoryEntries()
    {
        return squadHistoryEntries;
    }
}
