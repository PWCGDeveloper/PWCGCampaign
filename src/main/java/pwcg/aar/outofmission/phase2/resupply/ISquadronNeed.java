package pwcg.aar.outofmission.phase2.resupply;

import pwcg.core.exception.PWCGException;

public interface ISquadronNeed
{
    public void determineResupplyNeeded() throws PWCGException;
    public int getSquadronId();
    public boolean needsResupply();
    public void noteResupply();
}
