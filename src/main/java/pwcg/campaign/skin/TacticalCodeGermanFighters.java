package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class TacticalCodeGermanFighters
{
    private List<String> codes = new ArrayList<>();
    private List<TacticalCodeColor> colors = new ArrayList<>();

    public void buildTacticalCode(Campaign campaign, Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        initializeFighterCode();
        initializeFighterCodeColors();
        
        int rankPos = getRankValue(campaign, squadron, plane);
        if (rankPos == 0 || rankPos == 1)
        {
            buildTacticalCodeForFighterCommander(campaign, squadron, rankPos);
        }
        else
        {
            buildTacticalCodeForFighters(campaign,  squadron, plane);
        }
    }
    
    private int getRankValue(Campaign campaign, Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        ArmedService armedService = squadron.determineServiceForSquadron(campaign.getDate());
        int rankPos = rankObj.getRankPosByService( plane.getPilot().getRank(), armedService);
        return rankPos;
    }

    private void initializeFighterCode()
    {
        codes.add("%20");
        codes.add("%20");
        codes.add("%20");
    }

    private void initializeFighterCodeColors()
    {
        colors.add(TacticalCodeColor.BLACK);
        colors.add(TacticalCodeColor.BLACK);
        colors.add(TacticalCodeColor.BLACK);
    }

    private void buildTacticalCodeForFighterCommander(Campaign campaign, Squadron squadron, int rankPos) throws PWCGException
    {
        if (rankPos == 0)
        {
            codes.set(0, "%22");
        }
        else
        {
            codes.set(0, "%21");
            codes.set(1, "%25");
        }
        
        String gruppeCode = buildGruppeCodeForFighter(campaign, squadron);
        codes.set(2,  gruppeCode);

        if (codes.size() > 0)
        {
            TacticalCodeColor color = TacticalCodeColor.BLACK;
            for (int i = 0; i < codes.size(); ++i)
            {
                colors.set(i, color);
            }
        }
    }

    private void buildTacticalCodeForFighters(Campaign campaign, Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        
        String aircraftCode = plane.getAircraftIdCode();
        if (aircraftCode == null || aircraftCode.isEmpty())
        {
            return;
        }

        if (plane.getAircraftIdCode().length() == 1)
        {
            codes.set(1, plane.getAircraftIdCode());
        }
        else if (plane.getAircraftIdCode().length() > 1) 
        {
            codes.set(0, plane.getAircraftIdCode().substring(0,1));
            codes.set(1, plane.getAircraftIdCode().substring(1,2));
        }
        
        String gruppeCode = buildGruppeCodeForFighter(campaign, squadron);
        codes.set(2,  gruppeCode);
        
        TacticalCodeColor color = determineTacticalCodeColorForFighters(squadron, plane);
        for (int i = 0; i < codes.size(); ++i)
        {
            colors.set(i, color);
        }
    }

    private String buildGruppeCodeForFighter(Campaign campaign, Squadron squadron) throws PWCGException
    {
        if (squadron.determineSubUnitIdCode(campaign.getDate()) == null)
        {
            return "%20";
        }
        
        if (squadron.determineSubUnitIdCode(campaign.getDate()).equals("-"))
        {
            return "%3b";
        }
        else if (squadron.determineSubUnitIdCode(campaign.getDate()).contains("~"))
        {
            return "%3e";
        }
        else if (squadron.determineSubUnitIdCode(campaign.getDate()).contains("+"))
        {
            return "%3d";
        }
        else
        {
            return "%20";
        }
    }

    private TacticalCodeColor determineTacticalCodeColorForFighters(Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        if (squadron.getSquadronTacticalCodeColorOverride() !=  TacticalCodeColor.NONE)
        {
            return squadron.getSquadronTacticalCodeColorOverride();
        }
        
        if (plane.getSkin().getTacticalCodeColor() !=  TacticalCodeColor.NONE)
        {
            return plane.getSkin().getTacticalCodeColor();
        }

        return TacticalCodeColor.BLACK;
    }

    public List<String> getCodes()
    {
        return codes;
    }

    public List<TacticalCodeColor> getColors()
    {
        return colors;
    }
}
