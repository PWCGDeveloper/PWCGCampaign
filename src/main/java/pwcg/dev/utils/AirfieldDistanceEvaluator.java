package pwcg.dev.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.dev.utils.AirfieldDistanceOrganizer.AirfieldSet;

public class AirfieldDistanceEvaluator
{

    private static String[] mapTransitionDates = 
    {
            "19430301",
            "19430918",
            "19430927",
            "19431004",
            "19431008",
    };

    AirfieldDistanceOrganizer airfieldDistanceOrganizer = new AirfieldDistanceOrganizer();

    String mapName = PWCGMap.KUBAN_MAP_NAME;
    
    public static void main(String[] args) throws PWCGException
    {
	    UserDir.setUserDir();
	    PWCGContextManager.setRoF(false);
	    
        AirfieldDistanceEvaluator airfieldReporter = new AirfieldDistanceEvaluator();
        airfieldReporter.process();
    }

    public void process()
    {

        try
        {
            PWCGContextManager.getInstance();
                        
            for (int i = 0; i < mapTransitionDates.length - 1; ++i)
            {
                String startDateString = mapTransitionDates[i];
                String endDateString = mapTransitionDates[i+1];
                
                System.out.println("\n\n\nDate is " + startDateString + " to " + endDateString);
                
                Date startDate = DateUtils.getDateWithValidityCheck(startDateString);
                             
                airfieldDistanceOrganizer.process(startDate, FrontMapIdentifier.MOSCOW_MAP);
                
                analyzeSquadrons(airfieldDistanceOrganizer.alliedAirfieldSet, startDate, Side.ALLIED);
                
                analyzeSquadrons(airfieldDistanceOrganizer.axisAirfieldSet, startDate, Side.AXIS);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void analyzeSquadrons(AirfieldSet airfieldSet, Date dateNow, Side sideSquadrons) throws PWCGException
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        
        Map <String, IAirfield> fighterFields = airfieldSet.getFighterFields();
        Map <String, IAirfield> bomberFields = airfieldSet.getBomberFields();

        List<Squadron> allActiveSquadrons = squadronManager.getActiveSquadrons(dateNow);
        for (Squadron squadron : allActiveSquadrons)
        {
            boolean bad = false;
            
            IAirfield squadronField = squadron.determineCurrentAirfieldAnyMap(dateNow);
            
            if (squadron.determineSquadronCountry(dateNow).getSide() != sideSquadrons)
            {
                continue;
            }
            
            if (squadronField == null)
            {
                continue;
            }
            
            String reason = " ";
            
            if (squadron.determineSquadronPrimaryRole(dateNow) == Role.ROLE_FIGHTER)
            {
                if (!fighterFields.containsKey(squadronField.getName()))
                {
                    if (bomberFields.containsKey(squadronField.getName()))
                    {
                        reason = " ... is at a bomber field";
                        bad = true;
                    }
                    else if (airfieldSet.getFieldsTooClose().containsKey(squadronField.getName()))
                    {
                        reason = " ... is too close";
                        bad = true;
                    }
                    else if (airfieldSet.getFieldsTooFar().containsKey(squadronField.getName()))
                    {
                        reason = " ... is too far";
                        bad = true;
                    }
                }
            }
            else
            {
                
                if (!bomberFields.containsKey(squadronField.getName()))
                {
                    if (fighterFields.containsKey(squadronField.getName()))
                    {
                        reason = " ... is at a fighter field";
                        bad = true;
                    }
                    else if (airfieldSet.getFieldsTooClose().containsKey(squadronField.getName()))
                    {
                        reason = " ... is too close";
                        bad = true;
                    }
                    else if (airfieldSet.getFieldsTooFar().containsKey(squadronField.getName()))
                    {
                        reason = " ... is too far";
                        bad = true;
                    }
                }
            }
            

            if (bad)
            {
                System.out.println("\nSquadron " + squadron.getSquadronId() + " at field " + squadronField.getName() + " on date " + dateNow + reason);
                int distance = AirfieldReporter.getDistanceToFront(squadronField, sideSquadrons, dateNow);
                System.out.println(squadronField.getName() + "   Km to front: " + distance);
                AirfieldFinder.recommendBestMatch(squadron, dateNow);
            }
        }
    }
}
