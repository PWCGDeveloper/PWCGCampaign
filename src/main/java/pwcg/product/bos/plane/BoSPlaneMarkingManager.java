package pwcg.product.bos.plane;

import java.util.HashSet;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.product.bos.country.BoSServiceManager;

public class BoSPlaneMarkingManager implements IPlaneMarkingManager {

    @Override
    public void allocatePlaneIdCode(Campaign campaign, int squadronId, Equipment equipment, EquippedPlane equippedPlane) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);

        Set<String> allocatedCodes = new HashSet<>();
        for (EquippedPlane plane : equipment.getActiveEquippedPlanes().values())
        {
            allocatedCodes.add(plane.getAircraftIdCode());
        }

        if (squadron.getService() == BoSServiceManager.LUFTWAFFE)
        {
            if (squadron.determineDisplayName(campaign.getDate()).contains("JG") ||
                (squadron.determineDisplayName(campaign.getDate()).contains("SG") &&
                 squadron.determineUnitIdCode(campaign.getDate()) == null))
            {
                // Allocate numbers 1-N
                int code = 1;
                while (allocatedCodes.contains(Integer.toString(code)))
                    code++;

                equippedPlane.setAircraftIdCode(Integer.toString(code));
            } else {
                // Allocate letters from A
                // Do this randomly rather than in sequence?
                char code = 'A';
                while (allocatedCodes.contains(Character.toString(code)))
                    code++;
                if (code > 'Z')
                    throw new PWCGException("Unable to allocate plane ID code for squadron " + squadron.getSquadronId());

                equippedPlane.setAircraftIdCode(Character.toString(code));
            }
        }
        else if (squadron.getService() == BoSServiceManager.VVS ||
                 squadron.getService() == BoSServiceManager.NORMANDIE)
        {
            // Random numbers 1-99
            int code = RandomNumberGenerator.getRandom(99);
            while (allocatedCodes.contains(Integer.toString(code + 1)))
                code = (code + 1) % 99;

            equippedPlane.setAircraftIdCode(Integer.toString(code + 1));
        }
        else if (squadron.getService() == BoSServiceManager.REGIA_AERONAUTICA)
        {
            // Allocate numbers 1-N
            int code = 1;
            while (allocatedCodes.contains(Integer.toString(code)))
                code++;

            equippedPlane.setAircraftIdCode(Integer.toString(code));
        }
        else if (squadron.getService() == BoSServiceManager.USAAF ||
                 squadron.getService() == BoSServiceManager.RAF ||
                 squadron.getService() == BoSServiceManager.FREE_FRENCH)
        {
            // Allocate letters from A
            // Do this randomly rather than in sequence?
            char code = 'A';
            while (allocatedCodes.contains(Character.toString(code)))
                code++;
            if (code > 'Z')
                throw new PWCGException("Unable to allocate plane ID code for squadron " + squadron.getSquadronId());

            equippedPlane.setAircraftIdCode(Character.toString(code));
        }
    }
}
