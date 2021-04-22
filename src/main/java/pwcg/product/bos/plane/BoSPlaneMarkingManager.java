package pwcg.product.bos.plane;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.campaign.skin.TacticalCodeColor;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.product.bos.country.BoSServiceManager;

public class BoSPlaneMarkingManager implements IPlaneMarkingManager
{

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
            // Allocate letters randomly
            char startCode = (char) ('A' + RandomNumberGenerator.getRandom(25));
            char code = startCode;
            while (allocatedCodes.contains(Character.toString(code)))
            {
                if (code == 'Z')
                    code = 'A';
                else
                    code++;
                if (code == startCode)
                    throw new PWCGException("Unable to allocate plane ID code for squadron " + squadron.getSquadronId());
            }

            equippedPlane.setAircraftIdCode(Character.toString(code));
        }
    }

    @Override
    public String determineDisplayMarkings(Campaign campaign, EquippedPlane equippedPlane) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(equippedPlane.getSquadronId());

        if (squadron.getService() == BoSServiceManager.LUFTWAFFE)
        {
            if (squadron.determineDisplayName(campaign.getDate()).contains("JG") ||
                squadron.determineDisplayName(campaign.getDate()).contains("Sch.G") ||
                (squadron.determineDisplayName(campaign.getDate()).contains("SG") &&
                 squadron.determineUnitIdCode(campaign.getDate()) == null))
            {
                return equippedPlane.getAircraftIdCode() + "+" + squadron.determineSubUnitIdCode(campaign.getDate());
            } else {
                return squadron.determineUnitIdCode(campaign.getDate()) +
                        "+" +
                        equippedPlane.getAircraftIdCode() +
                        squadron.determineSubUnitIdCode(campaign.getDate());
            }
        }
        else if (squadron.getService() == BoSServiceManager.VVS ||
                 squadron.getService() == BoSServiceManager.NORMANDIE)
        {
            return equippedPlane.getAircraftIdCode();
        }
        else if (squadron.getService() == BoSServiceManager.REGIA_AERONAUTICA ||
                 squadron.getService() == BoSServiceManager.USAAF ||
                 squadron.getService() == BoSServiceManager.RAF ||
                 squadron.getService() == BoSServiceManager.FREE_FRENCH)
        {
            return squadron.determineUnitIdCode(campaign.getDate()) +
                    "-" +
                    equippedPlane.getAircraftIdCode();
        }

        return Integer.toString(equippedPlane.getSerialNumber());
    }

    private String convertGerman(String in)
    {
        String str = in;
        str = str.replace("<<-", "\"\u0027");
        str = str.replace("<<", "\"");
        str = str.replace("<-", "\u0021\u0027");
        str = str.replace("<", "\u0021");
        str = str.replace("\u25cb|", "\u0024"); // Open circle followed by bar
        str = str.replace("\u25cb", "\u0023");  // Open circle
        str = str.replace("||", "\u0026");
        str = str.replace("|", "\u0025");
        str = str.replace("\u25b2", "\u0028");  // Solid triangle
        str = str.replace("\u25cb", "\u003a");  // Solid circle
        str = str.replace("-", "\u003b");
        str = str.replace("+", "\u003c");
        str = str.replace("~~", "\u003e");
        str = str.replace("~", "\u003d");
        return str;
    }

    @Override
    public void writeTacticalCodes(BufferedWriter writer, Campaign campaign, PlaneMcu planeMcu) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(planeMcu.getSquadronId());
        String tCode = null;
        String tCodeColor = null;

        TacticalCodeColor tacticalCodeColor = planeMcu.getTacticalCodeColor();
        if (planeMcu.getSkin() != null)
        {
            tacticalCodeColor = planeMcu.getSkin().getTacticalCodeColor();
        }

        if (squadron.getService() == BoSServiceManager.LUFTWAFFE)
        {
            if (squadron.determineDisplayName(campaign.getDate()).contains("JG") ||
                squadron.determineDisplayName(campaign.getDate()).contains("Sch.G") ||
                (squadron.determineDisplayName(campaign.getDate()).contains("SG") &&
                 squadron.determineUnitIdCode(campaign.getDate()) == null))
            {
                tCode = String.format("%2s%-2s",
                                      convertGerman(planeMcu.getAircraftIdCode()),
                                      convertGerman(squadron.determineSubUnitIdCode(campaign.getDate())));
                tCodeColor = String.format("%1$s%1$s%1$s%1$s", tacticalCodeColor.getColorCode());
            } else {
                tCode = String.format("%2s%s%s",
                                      convertGerman(squadron.determineUnitIdCode(campaign.getDate())),
                                      convertGerman(planeMcu.getAircraftIdCode()),
                                      convertGerman(squadron.determineSubUnitIdCode(campaign.getDate())));
                tCodeColor = String.format("%1$s%1$s%1$s%1$s", tacticalCodeColor.getColorCode());
            }
        }
        else if (squadron.getService() == BoSServiceManager.VVS ||
                 squadron.getService() == BoSServiceManager.NORMANDIE)
        {
            tCode = String.format("%-4s", planeMcu.getAircraftIdCode());
            tCodeColor = String.format("%1$s%1$s%1$s%1$s", tacticalCodeColor.getColorCode());
        }
        else if (squadron.getService() == BoSServiceManager.USAAF ||
                 squadron.getService() == BoSServiceManager.RAF ||
                 squadron.getService() == BoSServiceManager.FREE_FRENCH)
        {
            tCode = String.format("%2s%s",
                                  squadron.determineUnitIdCode(campaign.getDate()),
                                  planeMcu.getAircraftIdCode());
            tCodeColor = String.format("%1$s%1$s%1$s", tacticalCodeColor.getColorCode());
        }
        else if (squadron.getService() == BoSServiceManager.REGIA_AERONAUTICA)
        {
            tCode = String.format("%3s%-2s",
                                  squadron.determineUnitIdCode(campaign.getDate()),
                                  planeMcu.getAircraftIdCode());
            tCodeColor = String.format("%1$s%1$s%1$s%1$s%1$s", tacticalCodeColor.getColorCode());
        }

        if (tCode != null)
        {
            try {
                writer.write("  TCode = \"" + URLEncoder.encode(tCode, "UTF-8").replace("+",  "%20") + "\";");
                writer.newLine();
                writer.write("  TCodeColor = \"" + tCodeColor + "\";");
                writer.newLine();
            } catch (IOException e) {
                PWCGLogger.logException(e);
                throw new PWCGIOException(e.getMessage());
            }
        }
    }
}
