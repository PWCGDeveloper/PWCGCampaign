package pwcg.dev.weaponsmodanalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.aar.inmission.phase1.parse.event.GreatBattlesFileParseUtils;
import pwcg.product.bos.plane.BosPlaneAttributeMapping;

public class PlaneModSetReader
{
    private static String PLANES_DIR = "D:\\PWCG\\GTP\\unzip\\luascripts\\worldobjects\\planes";

    private Map <String, List<PlaneMod>> planeMods =  new TreeMap<>();
    String currentPlane = "";
    String currentMap = "";

    public Map <String, List<PlaneMod>> readPlaneModFiles() throws Exception
    {
        for (BosPlaneAttributeMapping planeMapping: BosPlaneAttributeMapping.values())
        {
            readPlaneFile(planeMapping.getPlaneType());
        }
        return planeMods;
    }

    private void readPlaneFile(String planeType) throws Exception
    {
        currentPlane = planeType;
        String filepath = PLANES_DIR +  "\\" + planeType + ".txt";
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;

        while ((line = reader.readLine()) != null) 
        {
            findMod(line, reader);
        }

        reader.close();
    }

    private void findMod(String line, BufferedReader reader) throws Exception
    {
        if (line.toLowerCase().contains("[WeaponMode=".toLowerCase()))
        {
            readPlaneMod(reader, line);
        }
    }

    private void readPlaneMod(BufferedReader reader, String modIdLine) throws IOException
    {
        if (!planeMods.containsKey(currentPlane))
        {
            planeMods.put(currentPlane, new ArrayList<>());
        }

        PlaneMod planeMod = new PlaneMod();
        planeMod.modIndex = GreatBattlesFileParseUtils.getString(modIdLine , "[WeaponMode=", "]");
        
        String line;
        planeMod.plane = currentPlane;
        while ((line = reader.readLine()) != null) 
        {
            if (line.toLowerCase().contains("[end]"))
            {
                break;
            }
            else if (line.toLowerCase().contains("WMname=".toLowerCase()))
            {
                planeMod.modName = GreatBattlesFileParseUtils.getString(line , "WMname=\"", "\"");
            }             
        }
        
        List<PlaneMod> modsForPlane = planeMods.get(currentPlane);
        modsForPlane.add(planeMod);
    }
}
