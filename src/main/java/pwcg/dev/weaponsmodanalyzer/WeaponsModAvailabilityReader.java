package pwcg.dev.weaponsmodanalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.logfiles.event.GreatBattlesFileParseUtils;

public class WeaponsModAvailabilityReader
{
    private static String MAP_DIR = "D:\\PWCG\\GTP\\unzip\\scg\\scripts";
    private static String MOSCOW_MAP_FILE = "13planes-to-missions.cfg";
    private static String STALINGRAD_MAP_FILE = "14planes-to-missions.cfg";
    private static String KUBAN_MAP_FILE = "15planes-to-missions.cfg";
    private static String BODENPLATTE_MAP_FILE = "18planes-to-missions.cfg";
    
    private Map <String, Map<String, ModLimitationRaw>> modLimitations =  new TreeMap<>();
    String currentPlane = "";
    String currentMap = "";

    public Map <String, Map<String, ModLimitationRaw>> readMapModificationAvailabilityFiles() throws Exception
    {
        currentMap = "Moscow";
        readModificationsAvailabilityOnMapFile(MOSCOW_MAP_FILE);
        
        currentMap = "Stalingrad";
        readModificationsAvailabilityOnMapFile(STALINGRAD_MAP_FILE);
        
        currentMap = "Kuban";
        readModificationsAvailabilityOnMapFile(KUBAN_MAP_FILE);
        
        currentMap = "Rhein";
        readModificationsAvailabilityOnMapFile(BODENPLATTE_MAP_FILE);
        
        return modLimitations;
    }

    private void readModificationsAvailabilityOnMapFile(String filename) throws Exception
    {
        String filepath = MAP_DIR +  "\\" + filename;
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;

        while ((line = reader.readLine()) != null) 
        {
            changeCurrentPlane(line);
            findMod(line, reader);
        }

        reader.close();
    }

    private void changeCurrentPlane(String line)
    {
        if (line.toLowerCase().contains("plane="))
        {
            String planeDisplayName = GreatBattlesFileParseUtils.getString(line , "[plane=\"", "\"]");
            PlaneType aircraftType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneByDisplayName(planeDisplayName);
            currentPlane = aircraftType.getType();
        }
    }

    private void findMod(String line, BufferedReader reader) throws Exception
    {
        if (line.toLowerCase().contains("[period]"))
        {
            ModLimitationRaw modLimitation = readModLimitation(reader);
            
            if (modLimitation.modificationRequired == null || modLimitation.modificationDenied == null)
            {
                System.out.println("***** WARN mod does not have all parameters");
            }
            else
            {
                addModLimitation(modLimitation);
            }
        }
    }

    private void addModLimitation(ModLimitationRaw modLimitation)
    {
        Map<String, ModLimitationRaw> modLimitationsForPlane = modLimitations.get(currentPlane); 
        if (modLimitationsForPlane.containsKey(modLimitation.period))
        {
            ModLimitationRaw existingModLimitation = modLimitationsForPlane.get(modLimitation.period);
            if (!existingModLimitation.modificationRequired.equals(modLimitation.modificationRequired))
            {
                String mergedAllowed = mergeAllowedMods(existingModLimitation.modificationRequired, modLimitation.modificationRequired);
                System.out.println("***** WARN required mod changed from : " + existingModLimitation.modificationRequired + " to " + mergedAllowed);
                modLimitation.modificationRequired = mergedAllowed;
            }
            if (!existingModLimitation.modificationDenied.equals(modLimitation.modificationDenied))
            {
                String mergedDenied = mergeDeniedMods(existingModLimitation.modificationDenied, modLimitation.modificationDenied);
                System.out.println("***** WARN denied mod changed from : " + existingModLimitation.modificationDenied + " to " + mergedDenied);
                modLimitation.modificationDenied = mergedDenied;
            }
        }
   
        modLimitationsForPlane.put(modLimitation.period, modLimitation);
    }

    private String mergeAllowedMods(String modificationRequired, String newModificationRequired)
    {
        String mergedModifications = "";
        for (int i = 0; i < modificationRequired.length(); ++i)
        {
            if (modificationRequired.charAt(i) ==  '1' || newModificationRequired.charAt(i) == '1')
            {
                mergedModifications  += '1';
            }
            else
            {
                mergedModifications  += '0';
            }
        }
        return mergedModifications;
    }
    
    private String mergeDeniedMods(String existingModLimitation, String newModificationRequired)
    {
        String mergedModifications = "";
        assert(existingModLimitation.length() == newModificationRequired.length());
        for (int i = 0; i < existingModLimitation.length(); ++i)
        {
            if (existingModLimitation.charAt(i) ==  '0' || newModificationRequired.charAt(i) == '0')
            {
                mergedModifications  += '0';
            }
            else
            {
                mergedModifications  += '1';
            }
        }
        return mergedModifications;
    }


    private ModLimitationRaw readModLimitation(BufferedReader reader) throws IOException
    {
        String line;
        ModLimitationRaw modLimitation = new ModLimitationRaw();
        modLimitation.plane = currentPlane;
        modLimitation.map = currentMap;
        while ((line = reader.readLine()) != null) 
        {
            if (line.toLowerCase().contains("[end]"))
            {
                break;
            }
            else if (line.toLowerCase().contains("period="))
            {
                modLimitation.period = GreatBattlesFileParseUtils.getString(line , "period=\"", "");
            }
            else if (line.toLowerCase().contains("modifications_required="))
            {
                modLimitation.modificationRequired = GreatBattlesFileParseUtils.getString(line , "modifications_required=", "");
                modLimitation.modificationRequired = normalizeModmask(modLimitation.modificationRequired);
            }
            else if (line.toLowerCase().contains("modifications_denied="))
            {
                modLimitation.modificationDenied = GreatBattlesFileParseUtils.getString(line , "modifications_denied=", "");
                modLimitation.modificationDenied = normalizeModmask(modLimitation.modificationDenied);
            }
            
            if (!modLimitations.containsKey(currentPlane))
            {
                modLimitations.put(currentPlane, new TreeMap<>());
            }
        }
        
        return modLimitation;
    }
    
    private String normalizeModmask(String modMask) 
    {
        String normalizedMask = "";
        for (int i = 0; i < (12 - modMask.length() + 2); ++i)
        {
            normalizedMask += "0";
        }

        normalizedMask += modMask.substring(2);
        
        return normalizedMask;
    }
}

