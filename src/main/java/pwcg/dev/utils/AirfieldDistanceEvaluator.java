package pwcg.dev.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
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
    
    public static void main(String[] args) throws PWCGException
    {
	    UserDir.setUserDir();
	    PWCGContext.setProduct(PWCGProduct.BOS);
	    
        AirfieldDistanceEvaluator airfieldReporter = new AirfieldDistanceEvaluator();
        airfieldReporter.process();
    }

    public void process()
    {

        try
        {
            PWCGContext.getInstance();
                        
            for (int i = 0; i < mapTransitionDates.length - 1; ++i)
            {
                String startDateString = mapTransitionDates[i];
                String endDateString = mapTransitionDates[i+1];
                
                PWCGLogger.log(LogLevel.DEBUG, "\n\n\nDate is " + startDateString + " to " + endDateString);
                
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
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        
        Map <String, Airfield> fighterFields = airfieldSet.getFighterFields();
        Map <String, Airfield> bomberFields = airfieldSet.getBomberFields();

        List<Company> allActiveSquadrons = squadronManager.getActiveCompanies(dateNow);
        for (Company squadron : allActiveSquadrons)
        {
            boolean bad = false;
            
            Airfield squadronField = squadron.determineCurrentAirfieldAnyMap(dateNow);
            
            if (squadron.determineSquadronCountry(dateNow).getSide() != sideSquadrons)
            {
                continue;
            }
            
            if (squadronField == null)
            {
                continue;
            }
            
            String reason = " ";
            
            PwcgRoleCategory squadronRoleCategory = squadron.determineSquadronPrimaryRoleCategory(dateNow);
            if (squadronRoleCategory == PwcgRoleCategory.FIGHTER)
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
                PWCGLogger.log(LogLevel.DEBUG, "\nSquadron " + squadron.getCompanyId() + " at field " + squadronField.getName() + " on date " + dateNow + reason);
                int distance = AirfieldReporter.getDistanceToFront(squadronField, sideSquadrons, dateNow);
                PWCGLogger.log(LogLevel.DEBUG, squadronField.getName() + "   Km to front: " + distance);
                AirfieldBestMMatchFinder.recommendBestMatch(squadron, dateNow);
            }
        }
    }
}
