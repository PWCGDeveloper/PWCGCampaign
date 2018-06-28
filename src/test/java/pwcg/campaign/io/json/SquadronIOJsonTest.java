package pwcg.campaign.io.json;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.SquadHistory;
import pwcg.campaign.SquadHistoryEntry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class SquadronIOJsonTest
{
    @Test
    public void readJsonRoFSquadronsTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        List<Squadron> squadrons = SquadronIOJson.readJson();
        assert (squadrons.size() > 0);

        for (Squadron squadron : squadrons)
        {
            assert (squadron.getSquadronRoles().getSquadronRolePeriods().size() > 0);
            assert (squadron.getService() > 0);
            
            verifyLafayetteEsc(squadron);
            verifyRFCToRAF(squadron);
            verifyRNASToRAF(squadron);
        }
    }

    private void verifyLafayetteEsc(Squadron squadron) throws PWCGException
    {
        if (squadron.getSquadronId() == 101124)
        {
            SquadHistory squadronHistory = squadron.getSquadHistory();
            assert (squadronHistory != null);
            
            SquadHistoryEntry  squadHistoryEntry = squadronHistory.getSquadHistoryEntry(DateUtils.getDateYYYYMMDD("19180219"));
            assert (squadHistoryEntry != null);
            assert (squadHistoryEntry.getArmedServiceName().equals("United States Air Service"));
            assert (squadHistoryEntry.getSkill() == SquadHistoryEntry.NO_SQUADRON_SKILL_CHANGE);
        }
    }

    private void verifyRFCToRAF(Squadron squadron) throws PWCGException
    {
        if (squadron.getSquadronId() == 102020)
        {
            SquadHistory squadronHistory = squadron.getSquadHistory();
            assert (squadronHistory != null);
            
            SquadHistoryEntry  squadHistoryEntry = squadronHistory.getSquadHistoryEntry(DateUtils.getDateYYYYMMDD("19180401"));
            assert (squadHistoryEntry != null);
            assert (squadHistoryEntry.getArmedServiceName().equals("Royal Air Force"));
            assert (squadHistoryEntry.getSquadName().equals("No 20 Squadron RAF"));
            assert (squadHistoryEntry.getSkill() == SquadHistoryEntry.NO_SQUADRON_SKILL_CHANGE);
        }
    }

    private void verifyRNASToRAF(Squadron squadron) throws PWCGException
    {
        if (squadron.getSquadronId() == 102209)
        {
            SquadHistory squadronHistory = squadron.getSquadHistory();
            assert (squadronHistory != null);
            
            SquadHistoryEntry  squadHistoryEntry = squadronHistory.getSquadHistoryEntry(DateUtils.getDateYYYYMMDD("19180401"));
            assert (squadHistoryEntry != null);
            assert (squadHistoryEntry.getArmedServiceName().equals("Royal Air Force"));
            assert (squadHistoryEntry.getSquadName().equals("No 209 Squadron RAF"));
            assert (squadHistoryEntry.getSkill() == SquadHistoryEntry.NO_SQUADRON_SKILL_CHANGE);
        }
    }

    @Test
    public void readJsonBoSSquadronsTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        List<Squadron> squadrons = SquadronIOJson.readJson();
        assert (squadrons.size() > 0);
        
        boolean success = true;
        for (Squadron squadron : squadrons)
        {
            assert (squadron.getSquadronRoles().getSquadronRolePeriods().size() > 0);
            assert (squadron.getService() > 0);
        }
        
        assert(success);
    }
}
