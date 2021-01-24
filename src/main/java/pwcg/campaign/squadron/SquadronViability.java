package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class SquadronViability
{
    public static boolean isSquadronActive(Squadron squadron, Date date) throws PWCGException 
    {       
        String currentAirfield = squadron.determineCurrentAirfieldName(date);
        if (currentAirfield == null)
        {
            return false;
        }
        
        if (date.after(DateUtils.getEndOfWar()))
        {
            return false;
        }
        
        if (squadron.determineCurrentAircraftList(date).size() == 0)
        {
            return false;
        }
        
        return true; 
    }
    
    public static boolean isSquadronPlayerFlyable(Squadron squadron, Date date) throws PWCGException 
    {       
        if (!isSquadronActive(squadron, date))
        {
            return false;
        }
        
        if (!squadron.hasFlyablePlane(date))
        {
            return false;
        }
        
        return true; 
    }

    public static boolean isSquadronViable(Squadron squadron, Campaign campaign) throws PWCGException
    {
        if (!isSquadronActive(squadron, campaign.getDate()))
        {
            return false;
        }
        
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId());
        if (squadronPersonnel == null)
        {
            return false;
        }
        
        if (!squadronPersonnel.isSquadronPersonnelViable())
        {
            return false;
        }
        
        Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
        if (squadronEquipment == null)
        {
            return false;
        }
        
        if (!squadronEquipment.isSquadronEquipmentViable())
        {
            return false;
        }
        
        if (squadron.isInConversionPeriod(campaign.getDate()))
        {
            return false;
        }
        
        return true;
    }

    public static List<Squadron> reduceToAIOnly(Campaign campaign, List<Squadron> selectedSquadrons) throws PWCGException
    {
        List<Squadron> selectedSquadronsNoPlayer = new ArrayList<>();
        for (Squadron squadron : selectedSquadrons)
        {
            if (!campaign.getPersonnelManager().squadronHasActivePlayers(squadron.getSquadronId()))
            {
                selectedSquadronsNoPlayer.add(squadron);
            }
        }
        return selectedSquadronsNoPlayer;
    }
    
    public static List<Squadron> reduceToSide(Side side, List<Squadron> squadrons) throws PWCGException
    {       
        List<Squadron> squadronsForSide = new ArrayList<Squadron>();
        for (Squadron squadron : squadrons)
        {
            if (side == squadron.determineSide())
            {
                squadronsForSide.add(squadron);
            }
        }
        return squadronsForSide;
    }
    

    public static List<Squadron> reduceToCurrentMap(List<Squadron> squadrons, Date date) throws PWCGException 
    {
        List<Squadron> listForMap = new ArrayList<Squadron>();
        for (Squadron squadron : squadrons)
        {
            Airfield field = squadron.determineCurrentAirfieldCurrentMap(date);
            if (field != null)
            {
                listForMap.add(squadron);
            }
        }

        return listForMap;
    }
    
    public static List<Squadron> reduceToService(List<Squadron> squadrons, Date date, ArmedService service) throws PWCGException 
    {
        List<Squadron> squadronsForService = new ArrayList<>();
        for (Squadron squadron : squadrons)
        {
            if (squadron.determineServiceForSquadron(date).getServiceId() == service.getServiceId())
            {
                squadronsForService.add(squadron);
            }
        }
        return squadronsForService;
    }
    

    public static List<Squadron> reduceToCountry(List<Squadron> squadrons, Date date, ICountry country) throws PWCGException
    {
        List<Squadron> squadronsForCountry = new ArrayList<>();
        for (Squadron squadron : squadrons)
        {
            ICountry squadCountry = squadron.determineSquadronCountry(date);
            if (squadCountry.equals(country))
            {
                squadronsForCountry.add(squadron);
            }
        }
        return squadronsForCountry;
    }

    public static List<Squadron> reduceToRole(List<Squadron> squadrons, List<Role> acceptableRoles, Date date) throws PWCGException 
    {       
        Map<Integer, Squadron> squadronsWithRole = new HashMap<>();
        for(Squadron squadron : squadrons)
        {
            for (Role acceptableRole : acceptableRoles)
            {
                if (squadron.isSquadronThisRole(date, acceptableRole))
                {
                    squadronsWithRole.put(squadron.getSquadronId(), squadron);
                }
            }
        }

        return new ArrayList<>(squadronsWithRole.values());
    }
}
