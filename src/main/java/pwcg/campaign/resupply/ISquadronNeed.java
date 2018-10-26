package pwcg.campaign.resupply;

import pwcg.core.exception.PWCGException;

public interface ISquadronNeed
{
    public void determineResupplyNeeded() throws PWCGException;
    public int getSquadronId();
    public boolean needsResupply();
    public void noteResupply();
    public int getNumNeeded();
}
