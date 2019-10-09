package pwcg.dev.utils;

import java.io.File;
import java.util.List;

import pwcg.campaign.context.AceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

public class AceSkinChecker
{
    public static void main (String[] args)
    {
        UserDir.setUserDir();

        try
        {
            AceSkinChecker checker = new AceSkinChecker();
            checker.checkSkinsForExistence();
        }
        catch (Exception e)
        {
             Logger.logException(e);;
        }
    }

    public boolean skinExists(String aircraftName, String skinName)
    {
        boolean exists = false;

        if (!skinName.contains(".dds"))
        {
            skinName = skinName + ".dds";
        }

        String filename = System.getProperty("user.dir") + PWCGContext.getInstance().getDirectoryManager().getSimulatorDataDir() + "\\graphics\\skins\\" + aircraftName + "\\" + skinName;
        File file = new File(filename);
        if (file.exists())
        {
            exists = true;
        }

        return exists;
    }

    public void checkSkinsForExistence()
    {
        try
        {
            AceManager aceManager = new AceManager();
            aceManager.configure();

            List<HistoricalAce> historicalAces = aceManager.getHistoricalAces();
            
            List<PlaneType> allPlanes = PWCGContext.getInstance().getPlaneTypeFactory().getAllPlanes();
            
            for (HistoricalAce ace : historicalAces)
            {
                Logger.log(LogLevel.DEBUG, "Ace is " + ace.getSerialNumber());
                
                for (Skin skin : ace.getSkins())
                {
                    boolean skinFound = false;
                    for (PlaneType plane : allPlanes)
                    {
                        if (skinExists(plane.getType(), skin.getSkinName()))
                        {
                            Logger.log(LogLevel.DEBUG, "        Skin");
                            Logger.log(LogLevel.DEBUG, "        {");
                            Logger.log(LogLevel.DEBUG, "            Name = \"" + skin.getSkinName() + "\";");
                            Logger.log(LogLevel.DEBUG, "            StartDate = 01/08/1914;");
                            Logger.log(LogLevel.DEBUG, "            EndDate = 01/12/1918;");
                            Logger.log(LogLevel.DEBUG, "            Plane = \"" + plane.getType() + "\";");
                            Logger.log(LogLevel.DEBUG, "        }");
                            
                            skinFound = true;
                        }
                    }
                    
                    if (!skinFound)
                    {
                        Logger.log(LogLevel.DEBUG, "        Skin");
                        Logger.log(LogLevel.DEBUG, "        {");
                        Logger.log(LogLevel.DEBUG, "            Name = \"" + skin.getSkinName() + "\";");
                        Logger.log(LogLevel.DEBUG, "            StartDate = 01/08/1914;");
                        Logger.log(LogLevel.DEBUG, "            EndDate = 01/12/1918;");
                        Logger.log(LogLevel.DEBUG, "        }");
                    }
                }
            }
            
        }
        catch (Exception e)
        {
             Logger.logException(e);;
        }
    }
}
