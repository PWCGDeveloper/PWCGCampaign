package pwcg.product.bos.plane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSServiceManager;

public class BoSPlaneMarkingManager implements IPlaneMarkingManager
{
    private Map<Integer, HashSet<String>> allocatedCodesForSquadrons = new HashMap<>();

    @Override
    public void initialize(Campaign campaign) throws PWCGException
    {
        for (Integer squadronId : campaign.getEquipmentManager().getEquipmentAllSquadrons().keySet())
        {
            Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadronId);
            for (EquippedPlane equippedPlane : squadronEquipment.getActiveEquippedPlanes().values())
            {
                recordPlaneIdCode(campaign, squadronId, equippedPlane);
            }
        }
    }

    @Override
    public void recordPlaneIdCode(Campaign campaign, int squadronId, EquippedPlane equippedPlane) throws PWCGException
    {
        HashSet<String> codesForSquadron = getCodesForSquadron(squadronId);
        if (recordExistingCode(equippedPlane, codesForSquadron))
        {
            return;
        }
        else
        {
            allocatePlaneIdCode(campaign, squadronId, equippedPlane, codesForSquadron);
        }
    }

    private HashSet<String> getCodesForSquadron(int squadronId)
    {
        if (!allocatedCodesForSquadrons.containsKey(squadronId))
        {
            HashSet<String> codesForSquadron = new HashSet<>();
            allocatedCodesForSquadrons.put(squadronId, codesForSquadron);
        }
        HashSet<String> codesForSquadron = allocatedCodesForSquadrons.get(squadronId);
        return codesForSquadron;
    }

    private boolean recordExistingCode(EquippedPlane equippedPlane, HashSet<String> codesForSquadron)
    {
        if (equippedPlane.getAircraftIdCode() != null && !equippedPlane.getAircraftIdCode().isEmpty())
        {
            codesForSquadron.add(equippedPlane.getAircraftIdCode());
            return true;
        }
        return false;
    }

    private void allocatePlaneIdCode(Campaign campaign, int squadronId, EquippedPlane equippedPlane, HashSet<String> codesForSquadron) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        int armedServiceId = squadron.getService();
        String code = "";
        if (armedServiceId == BoSServiceManager.LUFTWAFFE)
        {
            if (squadron.determineDisplayName(campaign.getDate()).contains("JG") || squadron.determineDisplayName(campaign.getDate()).contains("SG"))
            {
                code = pickLowNumericCode(codesForSquadron);
            }
            else
            {
                code = pickAlphaCode(codesForSquadron);
            }
        }
        else if (armedServiceId == BoSServiceManager.VVS || armedServiceId == BoSServiceManager.NORMANDIE)
        {
            code = pickHighNumericCode(codesForSquadron);
        }
        else if (armedServiceId == BoSServiceManager.REGIA_AERONAUTICA)
        {
            code = pickLowNumericCode(codesForSquadron);
        }
        else if (armedServiceId == BoSServiceManager.USAAF || armedServiceId == BoSServiceManager.RAF
                || armedServiceId == BoSServiceManager.FREE_FRENCH || armedServiceId == BoSServiceManager.RCAF)
        {
            code = pickAlphaCode(codesForSquadron);
        }
        else
        {
            throw new PWCGException ("No tactical code generation for service " + armedServiceId);
        }

        if (!code.isEmpty())
        {
            equippedPlane.setAircraftIdCode(code);
            codesForSquadron.add(code);
        }
}

    private String pickHighNumericCode(HashSet<String> codesForSquadron)
    {
        String code;
        List<String> possibleCodes = new ArrayList<>();
        for (int i = 10; i < 99; ++i)
        {
            possibleCodes.add("" + i);
        }
        code = pickACode(codesForSquadron, possibleCodes);
        return code;
    }

    private String pickLowNumericCode(HashSet<String> codesForSquadron)
    {
        String code;
        List<String> possibleCodes = new ArrayList<>();
        for (int i = 1; i < 19; ++i)
        {
            possibleCodes.add("" + i);
        }
        code = pickACode(codesForSquadron, possibleCodes);
        return code;
    }

    private String pickAlphaCode(HashSet<String> codesForSquadron)
    {
        String code;
        // Allocate letters randomly
        List<String> possibleCodes = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y", "Z");
        code = pickACode(codesForSquadron, possibleCodes);
        return code;
    }

    private String pickACode(HashSet<String> codesForSquadron, List<String> possibleCodes)
    {
        String code = "";

        Collections.shuffle(possibleCodes);
        for (String possibleCode : possibleCodes)
        {
            if (codesForSquadron.contains(possibleCode))
            {
                continue;
            }

            code = possibleCode;
            break;
        }
        return code;
    }
}
