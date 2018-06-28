package pwcg.dev.utils;

import java.util.Set;

import pwcg.campaign.context.AceManager;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class AceSquadronCommandChecker
{
    public static void main (String[] args)
    {
        UserDir.setUserDir();

        try
        {
            AceSquadronCommandChecker checker = new AceSquadronCommandChecker();
            checker.checkForSquadronCommand();
        }
        catch (Exception e)
        {
             Logger.logException(e);;
        }
    }

    public void checkForSquadronCommand()
    {
        try
        {
            AceManager aceManager = new AceManager();
            aceManager.configure();

            Set<Integer> aceCommandedSquadrons = aceManager.getAceCommandedSquadrons();
             
            for (Integer aceSquadronId : aceCommandedSquadrons)
            {
                Logger.log(LogLevel.DEBUG, "" + aceSquadronId);
            }
            
        }
        catch (Exception e)
        {
             Logger.logException(e);
        }
    }
}
