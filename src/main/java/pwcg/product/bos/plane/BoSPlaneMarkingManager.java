package pwcg.product.bos.plane;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.plane.PlaneMcu;
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
        if (squadron.determineUnitIdCode(campaign.getDate()) == null)
        {
            return;
        }

        if (squadron.getService() == BoSServiceManager.LUFTWAFFE)
        {
            if (squadron.determineDisplayName(campaign.getDate()).contains("JG") || squadron.determineDisplayName(campaign.getDate()).contains("SG"))
            {
                // Allocate numbers 1-N
                int code = 1;
                while (codesForSquadron.contains(Integer.toString(code)))
                    code++;

                equippedPlane.setAircraftIdCode(Integer.toString(code));
            }
            else
            {
                // Allocate letters from A
                // Do this randomly rather than in sequence?
                char code = 'A';
                while (codesForSquadron.contains(Character.toString(code)))
                    code++;
                if (code > 'Z')
                    throw new PWCGException("Unable to allocate plane ID code for squadron " + squadron.getSquadronId());

                equippedPlane.setAircraftIdCode(Character.toString(code));
            }
        }
        else if (squadron.getService() == BoSServiceManager.VVS || squadron.getService() == BoSServiceManager.NORMANDIE)
        {
            // Random numbers 1-99
            int code = RandomNumberGenerator.getRandom(99);
            while (codesForSquadron.contains(Integer.toString(code + 1)))
                code = (code + 1) % 99;

            equippedPlane.setAircraftIdCode(Integer.toString(code + 1));
        }
        else if (squadron.getService() == BoSServiceManager.REGIA_AERONAUTICA)
        {
            // Allocate numbers 1-N
            int code = 1;
            while (codesForSquadron.contains(Integer.toString(code)))
                code++;

            equippedPlane.setAircraftIdCode(Integer.toString(code));
        }
        else if (squadron.getService() == BoSServiceManager.USAAF || squadron.getService() == BoSServiceManager.RAF
                || squadron.getService() == BoSServiceManager.FREE_FRENCH)
        {
            // Allocate letters randomly
            char startCode = (char) ('A' + RandomNumberGenerator.getRandom(25));
            char code = startCode;
            while (codesForSquadron.contains(Character.toString(code)))
            {
                if (code == 'Z')
                    code = 'A';
                else
                    code++;
                if (code == startCode)
                    throw new PWCGException("Unable to allocate plane ID code for squadron " + squadron.getSquadronId());
            }

            equippedPlane.setAircraftIdCode(Character.toString(code));
            codesForSquadron.add(Character.toString(code));
        }
    }

    @Override
    public String determineDisplayMarkings(Campaign campaign, EquippedPlane equippedPlane) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(equippedPlane.getSquadronId());

        if (squadron.getService() == BoSServiceManager.LUFTWAFFE)
        {
            if (squadron.determineDisplayName(campaign.getDate()).contains("JG") || squadron.determineDisplayName(campaign.getDate()).contains("Sch.G")
                    || (squadron.determineDisplayName(campaign.getDate()).contains("SG") && squadron.determineUnitIdCode(campaign.getDate()) == null))
            {
                return equippedPlane.getAircraftIdCode() + "+" + squadron.determineSubUnitIdCode(campaign.getDate());
            }
            else
            {
                return squadron.determineUnitIdCode(campaign.getDate()) + "+" + equippedPlane.getAircraftIdCode()
                        + squadron.determineSubUnitIdCode(campaign.getDate());
            }
        }
        else if (squadron.getService() == BoSServiceManager.VVS || squadron.getService() == BoSServiceManager.NORMANDIE)
        {
            return equippedPlane.getAircraftIdCode();
        }
        else if (squadron.getService() == BoSServiceManager.REGIA_AERONAUTICA || squadron.getService() == BoSServiceManager.USAAF
                || squadron.getService() == BoSServiceManager.RAF || squadron.getService() == BoSServiceManager.FREE_FRENCH)
        {
            return squadron.determineUnitIdCode(campaign.getDate()) + "-" + equippedPlane.getAircraftIdCode();
        }

        return Integer.toString(equippedPlane.getSerialNumber());
    }

    @Override
    public void writeTacticalCodes(BufferedWriter writer, Campaign campaign, PlaneMcu planeMcu) throws PWCGException
    {
        BoSPlaneMarkingWriter planeMarkingWriter = new BoSPlaneMarkingWriter();
        planeMarkingWriter.writeTacticalCodes(writer, campaign, planeMcu);
    }
}
