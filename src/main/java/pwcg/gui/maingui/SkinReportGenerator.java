package pwcg.gui.maingui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MissingSkin;

public class SkinReportGenerator
{
    public String writeMissingSkinReport(Map<String, List<MissingSkin>> skinsToWrite) throws IOException, PWCGException
    {
        BufferedWriter skinFileForPlaneWriter = null;
        String missingSkinFileNameReturn = "";
        
        try
        {
            String dateString = DateUtils.getDateStringYYYYMMDDHHMMSS(new Date());
            String missingSkinFileName = "MissingSkinReport_" + dateString + ".txt";
            
            File missingSkinFile = new File(missingSkinFileName);
            missingSkinFile.createNewFile();
            
            skinFileForPlaneWriter = new BufferedWriter(new FileWriter(missingSkinFile));
            
            writeReport(skinsToWrite, skinFileForPlaneWriter);
            
            missingSkinFileNameReturn = missingSkinFile.getAbsolutePath();
        }
        finally
        {
            if (skinFileForPlaneWriter != null)
            {
                skinFileForPlaneWriter.close();
            }
        }
        
        return missingSkinFileNameReturn;
    }

    /**
     * Write a missing skin report by plane/skin
     * 
     * @param skinsToWrite
     * @param skinFileForPlaneWriter
     * @throws PWCGException
     * @throws IOException
     */
    private void writeReport(Map<String, List<MissingSkin>> skinsToWrite, BufferedWriter skinFileForPlaneWriter)
                    throws PWCGException, IOException
    {
        for (String planeTypeDesc : skinsToWrite.keySet())
        {
            TankType plane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(planeTypeDesc);
            List<MissingSkin> missingSkinSet = skinsToWrite.get(planeTypeDesc);
            
            // The plane
            skinFileForPlaneWriter.newLine();
            skinFileForPlaneWriter.write("Plane: " + plane.getDisplayName());
            skinFileForPlaneWriter.newLine();
            
            // The missing skins
            for (MissingSkin missingSkin : missingSkinSet)
            {
                skinFileForPlaneWriter.write("    Skin Name:" + missingSkin.getSkinName());
                skinFileForPlaneWriter.newLine();

                skinFileForPlaneWriter.write("    Skin Category:" + missingSkin.getCategory());
                skinFileForPlaneWriter.newLine();
            }
        }
    }
}
