package pwcg.dev.weaponsmodanalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModAvailabilityConsolidator
{

    public ModLimitations consolidate(
            Map<String, List<PlaneMod>> planeMods, 
            Map<String, Map<String, ModLimitationRaw>> rawModLimitations)
    {
        ModLimitations modLimitations = new ModLimitations();
        for (String plane : planeMods.keySet())
        {
            ModLimitation modLimitation = new ModLimitation();
            List<PlaneMod> planeModsForPlane = planeMods.get(plane);
            Map<String, ModLimitationRaw> rawModLimitationsForPlane = rawModLimitations.get(plane);
            
            if (rawModLimitationsForPlane != null)
            {
                modLimitation.plane = plane;
                modLimitation.modLimitationPeriods = mapModsToBitMap(rawModLimitationsForPlane, planeModsForPlane);
                modLimitations.modLimitations.add(modLimitation);
            }
        }
        return modLimitations;
    }

    private List<ModLimitationPeriod> mapModsToBitMap(Map<String, ModLimitationRaw> rawModLimitationsForPlane, List<PlaneMod> planeModsForPlane)
    {
        List<ModLimitationPeriod> modPeriods = new ArrayList<>();

        for (String period : rawModLimitationsForPlane.keySet())
        {
            ModLimitationPeriod modLimitationPeriod = new ModLimitationPeriod();
            modLimitationPeriod.period = period;
            
            ModLimitationRaw modLimitationsForPeriod = rawModLimitationsForPlane.get(period);
            modLimitationPeriod.modLimitationElements = getModAvailabilityForPeriod(planeModsForPlane, modLimitationsForPeriod);
            
            modPeriods.add(modLimitationPeriod);
        }
        return modPeriods;
    }

    private List<ModLimitationElement> getModAvailabilityForPeriod(List<PlaneMod> planeModsForPlane, ModLimitationRaw modLimitationsForPeriod)
    {
        List<ModLimitationElement> modsForPeriod = new ArrayList<>();
        for (int i = 0; i < planeModsForPlane.size(); ++i)
        {
            ModLimitationElement element = new ModLimitationElement();
            PlaneMod planeMod = planeModsForPlane.get(i);
            element.modificationName = planeMod.modName;
            boolean isAvailable = true;
            
            int modMaskIndex = modLimitationsForPeriod.modificationRequired.length() -i -1;
            if (modLimitationsForPeriod.modificationRequired.charAt(modMaskIndex) ==  '1')
            {
                isAvailable = true;
            }
            else if (modLimitationsForPeriod.modificationDenied.charAt(modMaskIndex) ==  '1')
            {
                isAvailable = false;
            }
            element.isAvailable = isAvailable;
            modsForPeriod.add(element);
        }
        return modsForPeriod;
    }
}
