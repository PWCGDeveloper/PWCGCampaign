package pwcg.product.bos.plane;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLEncoder;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skin.TacticalCodeColor;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.product.bos.country.BoSServiceManager;

public class BoSPlaneMarkingWriter
{
    public void writeTacticalCodes(BufferedWriter writer, Campaign campaign, PlaneMcu planeMcu) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(planeMcu.getSquadronId());
        if (squadron == null)
        {
            return;
        }
        
        if (!shouldUseTacticalCodes(squadron, planeMcu))
        {
            return;
        }

        TacticalCodeColor tacticalCodeColor = planeMcu.getTacticalCodeColor();
        if (planeMcu.getSkin() != null)
        {
            tacticalCodeColor = planeMcu.getSkin().getTacticalCodeColor();
        }

        TacticalCodeMarkings tacticalCodeMarkings = getTacticalMarkings(campaign, planeMcu, squadron, tacticalCodeColor);
        if (tacticalCodeMarkings != null && tacticalCodeMarkings.isValid())
        {
            writeTacticalMarkingCode(writer, tacticalCodeMarkings);
        }
    }

    private boolean shouldUseTacticalCodes(Squadron squadron, PlaneMcu planeMcu) throws PWCGException
    {
        if (planeMcu.getAircraftIdCode() == null)
        {
            return false;
        }

        if (planeMcu.getSkin() == null)
        {
            return false;
        }

        if (!planeMcu.getSkin().isUseTacticalCodes())
        {
            return false;
        }

        return true;
    }

    private TacticalCodeMarkings getTacticalMarkings(Campaign campaign, PlaneMcu planeMcu, Squadron squadron, TacticalCodeColor tacticalCodeColor)
            throws PWCGException
    {
        TacticalCodeMarkings tacticalCodeMarkings = null;
        if (squadron.getService() == BoSServiceManager.LUFTWAFFE)
        {
            tacticalCodeMarkings = getTacticalMarkingsLuftwaffe(campaign, planeMcu, squadron, tacticalCodeColor);
        }
        else if (squadron.getService() == BoSServiceManager.VVS || squadron.getService() == BoSServiceManager.NORMANDIE)
        {
            tacticalCodeMarkings = getTacticalMarkingsVVS(planeMcu, tacticalCodeColor);
        }
        else if (squadron.getService() == BoSServiceManager.USAAF || squadron.getService() == BoSServiceManager.RAF
                || squadron.getService() == BoSServiceManager.FREE_FRENCH)
        {
            tacticalCodeMarkings = getTacticalMarkingsWesternAllies(campaign, planeMcu, squadron, tacticalCodeColor);
        }
        else if (squadron.getService() == BoSServiceManager.REGIA_AERONAUTICA)
        {
            tacticalCodeMarkings = getTacticalMarkingsItaly(campaign, planeMcu, squadron, tacticalCodeColor);
        }
        return tacticalCodeMarkings;
    }

    private TacticalCodeMarkings getTacticalMarkingsLuftwaffe(Campaign campaign, PlaneMcu planeMcu, Squadron squadron, TacticalCodeColor tacticalCodeColor)
            throws PWCGException
    {
        String tCode;
        String tCodeColor;
        if (squadron.determineDisplayName(campaign.getDate()).contains("JG") || squadron.determineDisplayName(campaign.getDate()).contains("Sch.G")
                || (squadron.determineDisplayName(campaign.getDate()).contains("SG") && squadron.determineUnitIdCode(campaign.getDate()) == null))
        {
            tCode = String.format("%2s%-2s", convertGerman(planeMcu.getAircraftIdCode()), convertGerman(squadron.determineSubUnitIdCode(campaign.getDate())));
            tCodeColor = String.format("%1$s%1$s%1$s%1$s", tacticalCodeColor.getColorCode());
        }
        else
        {
            tCode = String.format("%2s%s%s", convertGerman(squadron.determineUnitIdCode(campaign.getDate())), convertGerman(planeMcu.getAircraftIdCode()),
                    convertGerman(squadron.determineSubUnitIdCode(campaign.getDate())));
            tCodeColor = String.format("%1$s%1$s%1$s%1$s", tacticalCodeColor.getColorCode());
        }

        return new TacticalCodeMarkings(tCode, tCodeColor);
    }

    private TacticalCodeMarkings getTacticalMarkingsVVS(PlaneMcu planeMcu, TacticalCodeColor tacticalCodeColor)
    {
        String tCode;
        String tCodeColor;
        tCode = String.format("%-4s", planeMcu.getAircraftIdCode());
        tCodeColor = String.format("%1$s%1$s%1$s%1$s", tacticalCodeColor.getColorCode());

        return new TacticalCodeMarkings(tCode, tCodeColor);
    }

    private TacticalCodeMarkings getTacticalMarkingsWesternAllies(Campaign campaign, PlaneMcu planeMcu, Squadron squadron, TacticalCodeColor tacticalCodeColor)
            throws PWCGException
    {
        String tCode;
        String tCodeColor;
        tCode = String.format("%2s%s", squadron.determineUnitIdCode(campaign.getDate()), planeMcu.getAircraftIdCode());
        tCodeColor = String.format("%1$s%1$s%1$s", tacticalCodeColor.getColorCode());

        return new TacticalCodeMarkings(tCode, tCodeColor);
    }

    private TacticalCodeMarkings getTacticalMarkingsItaly(Campaign campaign, PlaneMcu planeMcu, Squadron squadron, TacticalCodeColor tacticalCodeColor)
            throws PWCGException
    {
        String tCode;
        String tCodeColor;
        tCode = String.format("%3s%-2s", squadron.determineUnitIdCode(campaign.getDate()), planeMcu.getAircraftIdCode());
        tCodeColor = String.format("%1$s%1$s%1$s%1$s%1$s", tacticalCodeColor.getColorCode());

        return new TacticalCodeMarkings(tCode, tCodeColor);
    }

    private void writeTacticalMarkingCode(BufferedWriter writer, TacticalCodeMarkings markings) throws PWCGException
    {
        try
        {
            writer.write("  TCode = \"" + URLEncoder.encode(markings.tCode, "UTF-8").replace("+", "%20") + "\";");
            writer.newLine();
            writer.write("  TCodeColor = \"" + markings.tCodeColor + "\";");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    private class TacticalCodeMarkings
    {
        TacticalCodeMarkings(String tCode, String tCodeColor)
        {
            this.tCode = tCode;
            this.tCodeColor = tCodeColor;
        }

        public boolean isValid()
        {
            if (tCode != null && tCodeColor != null)
            {
                return true;
            }

            return false;
        }

        String tCode;
        String tCodeColor;
    }

    private String convertGerman(String in)
    {
        String str = in;
        str = str.replace("<<-", "\"\u0027");
        str = str.replace("<<", "\"");
        str = str.replace("<-", "\u0021\u0027");
        str = str.replace("<", "\u0021");
        str = str.replace("\u25cb|", "\u0024"); // Open circle followed by bar
        str = str.replace("\u25cb", "\u0023"); // Open circle
        str = str.replace("||", "\u0026");
        str = str.replace("|", "\u0025");
        str = str.replace("\u25b2", "\u0028"); // Solid triangle
        str = str.replace("\u25cb", "\u003a"); // Solid circle
        str = str.replace("-", "\u003b");
        str = str.replace("+", "\u003c");
        str = str.replace("~~", "\u003e");
        str = str.replace("~", "\u003d");
        return str;
    }
}
