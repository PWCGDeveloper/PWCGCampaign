package pwcg.dev.utils;

import java.util.Set;

import pwcg.campaign.context.AceManager;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

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
             PWCGLogger.logException(e);;
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
                PWCGLogger.log(LogLevel.DEBUG, "" + aceSquadronId);
            }
            
        }
        catch (Exception e)
        {
             PWCGLogger.logException(e);
        }
    }
}
