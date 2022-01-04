package pwcg.dev.utils;

import java.io.File;
import java.util.List;

import pwcg.campaign.context.AceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectorySimulatorManager;
import pwcg.campaign.crewmember.HistoricalAce;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.tank.TankType;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

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
             PWCGLogger.logException(e);;
        }
    }

    public boolean skinExists(String aircraftName, String skinName)
    {
        boolean exists = false;

        if (!skinName.contains(".dds"))
        {
            skinName = skinName + ".dds";
        }

        String filename = System.getProperty("user.dir") + PWCGDirectorySimulatorManager.getInstance().getSkinsDir() + aircraftName + "\\" + skinName;
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
            
            List<TankType> allPlanes = PWCGContext.getInstance().getTankTypeFactory().getAllPlanes();
            
            for (HistoricalAce ace : historicalAces)
            {
                PWCGLogger.log(LogLevel.DEBUG, "Ace is " + ace.getSerialNumber());
                
                for (Skin skin : ace.getSkins())
                {
                    boolean skinFound = false;
                    for (TankType plane : allPlanes)
                    {
                        if (skinExists(plane.getType(), skin.getSkinName()))
                        {
                            PWCGLogger.log(LogLevel.DEBUG, "        Skin");
                            PWCGLogger.log(LogLevel.DEBUG, "        {");
                            PWCGLogger.log(LogLevel.DEBUG, "            Name = \"" + skin.getSkinName() + "\";");
                            PWCGLogger.log(LogLevel.DEBUG, "            StartDate = 01/08/1914;");
                            PWCGLogger.log(LogLevel.DEBUG, "            EndDate = 01/12/1918;");
                            PWCGLogger.log(LogLevel.DEBUG, "            Plane = \"" + plane.getType() + "\";");
                            PWCGLogger.log(LogLevel.DEBUG, "        }");
                            
                            skinFound = true;
                        }
                    }
                    
                    if (!skinFound)
                    {
                        PWCGLogger.log(LogLevel.DEBUG, "        Skin");
                        PWCGLogger.log(LogLevel.DEBUG, "        {");
                        PWCGLogger.log(LogLevel.DEBUG, "            Name = \"" + skin.getSkinName() + "\";");
                        PWCGLogger.log(LogLevel.DEBUG, "            StartDate = 01/08/1914;");
                        PWCGLogger.log(LogLevel.DEBUG, "            EndDate = 01/12/1918;");
                        PWCGLogger.log(LogLevel.DEBUG, "        }");
                    }
                }
            }
            
        }
        catch (Exception e)
        {
             PWCGLogger.logException(e);;
        }
    }
}
