package pwcg.gui.sound;

import java.util.Date;

import javax.swing.JOptionPane;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogFileMostRecentDateFinder;
import pwcg.core.logfiles.LogSetFinder;
import pwcg.core.utils.DirectoryReader;
import pwcg.gui.dialogs.ConfirmDialog;

public class ProceedWithMission
{

    public static boolean shouldProceedWithMission(Campaign campaign, String text) throws PWCGException
    {
        DirectoryReader directoryReader = new DirectoryReader();
        LogSetFinder logSetFinder = new LogSetFinder(directoryReader);
        LogFileMostRecentDateFinder logFileMostRecentDateFinder = new LogFileMostRecentDateFinder(campaign, logSetFinder);
        Date lastFlownMissionDate = logFileMostRecentDateFinder.determineMostRecentAARLogFileMissionDataSetForCampaign();
        
        if (lastFlownMissionDate == null)
        {
            return true;
        }
        
        if (lastFlownMissionDate.equals(campaign.getDate()))
        {
            int result = ConfirmDialog.areYouSure(text);
            if (result == JOptionPane.YES_OPTION)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return true;
    }

}
