package pwcg.dev.jsonconvert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.SquadHistory;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.io.json.SquadronIOJson;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronConversionPeriod;
import pwcg.campaign.squadron.SquadronRolePeriod;
import pwcg.campaign.squadron.SquadronRoleSet;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.country.FCServiceManager;

public class FCSquadronConverter extends GroundObjectIOJsonConverter{

    public static void main(String[] args) throws PWCGException
    {
    	PWCGContext.setProduct(PWCGProduct.FC);

    	FCSquadronConverter jsonConverter = new FCSquadronConverter();
    	jsonConverter.initializeFCSquadrons();
    }
    
    public void initializeFCSquadrons() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        for (Squadron squadron : squadronManager.getAllSquadrons())
        {
            System.out.println(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19180601")));
            
            List<Skin> skins = new ArrayList<Skin>();
            squadron.setSkins(skins);
            
            SquadHistory squadHistory = new SquadHistory();
            squadron.setSquadHistory(squadHistory);
            
            List<SquadronConversionPeriod> conversionPeriods = new ArrayList<>();
            squadron.setConversionPeriods(conversionPeriods);
            
            Map<Date, String> airfields = new TreeMap<>();
            if (squadron.determineSide() == Side.ALLIED)
            {
                airfields.put(DateUtils.getDateYYYYMMDD("19170801"), "Bruay");
            }
            else
            {
                airfields.put(DateUtils.getDateYYYYMMDD("19170801"), "Cambrai East");
            }
            squadron.setAirfields(airfields);
            
            List<SquadronPlaneAssignment> planeAssignments = new ArrayList<>();
            SquadronPlaneAssignment planeAssignment = new SquadronPlaneAssignment();
            planeAssignment.setSquadronIntroduction(DateUtils.getDateYYYYMMDD("19170801"));
            planeAssignment.setSquadronWithdrawal(DateUtils.getDateYYYYMMDD("19181201"));
            if (squadron.determineSide() == Side.ALLIED)
            {
                planeAssignment.setArchType("sopcamel");
            }
            else
            {
                planeAssignment.setArchType("albatros");
            }
            planeAssignments.add(planeAssignment);
            squadron.setPlaneAssignments(planeAssignments);

            SquadronRoleSet squadronRoles = squadron.getSquadronRoles();
            for (SquadronRolePeriod rolePeriod : squadronRoles.getSquadronRolePeriods())
            {
                rolePeriod.setStartDate(DateUtils.getDateYYYYMMDD("19170801"));
                rolePeriod.setEndDate(DateUtils.getDateYYYYMMDD("19181201"));
            }

            if (squadron.determineSide() == Side.ALLIED)
            {
                squadron.setService(FCServiceManager.RFC);
                
                int squadronId = squadron.getSquadronId();
                int squadronIdPart = squadronId % 1000;
                int newSquadronId = 302000 + squadronIdPart;
                squadron.setSquadronId(newSquadronId);
            }
            else
            {
                squadron.setService(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE);
                
                int squadronId = squadron.getSquadronId();
                int squadronIdPart = squadronId % 1000;
                int newSquadronId = 401000 + squadronIdPart;
                squadron.setSquadronId(newSquadronId);
            }

            SquadronIOJson.writeJson(squadron);
        }
    }
 }
