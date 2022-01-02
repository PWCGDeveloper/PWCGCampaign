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
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class SquadronViability
{
    public static boolean isSquadronActive(Company squadron, Date date) throws PWCGException 
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
    
    public static boolean isSquadronPlayerFlyable(Company squadron, Date date) throws PWCGException 
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

    public static boolean isSquadronViable(Company squadron, Campaign campaign) throws PWCGException
    {
        if (!isSquadronActive(squadron, campaign.getDate()))
        {
            return false;
        }
        
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadron.getSquadronId());
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

    public static List<Company> reduceToAIOnly(Campaign campaign, List<Company> selectedSquadrons) throws PWCGException
    {
        List<Company> selectedSquadronsNoPlayer = new ArrayList<>();
        for (Company squadron : selectedSquadrons)
        {
            if (!campaign.getPersonnelManager().squadronHasActivePlayers(squadron.getSquadronId()))
            {
                selectedSquadronsNoPlayer.add(squadron);
            }
        }
        return selectedSquadronsNoPlayer;
    }
    
    public static List<Company> reduceToSide(Side side, List<Company> squadrons) throws PWCGException
    {       
        List<Company> squadronsForSide = new ArrayList<Company>();
        for (Company squadron : squadrons)
        {
            if (side == squadron.determineSide())
            {
                squadronsForSide.add(squadron);
            }
        }
        return squadronsForSide;
    }
    

    public static List<Company> reduceToCurrentMap(List<Company> squadrons, Date date) throws PWCGException 
    {
        List<Company> listForMap = new ArrayList<Company>();
        for (Company squadron : squadrons)
        {
            Airfield field = squadron.determineCurrentAirfieldCurrentMap(date);
            if (field != null)
            {
                listForMap.add(squadron);
            }
        }

        return listForMap;
    }
    
    public static List<Company> reduceToService(List<Company> squadrons, Date date, ArmedService service) throws PWCGException 
    {
        List<Company> squadronsForService = new ArrayList<>();
        for (Company squadron : squadrons)
        {
            if (squadron.determineServiceForSquadron(date).getServiceId() == service.getServiceId())
            {
                squadronsForService.add(squadron);
            }
        }
        return squadronsForService;
    }
    

    public static List<Company> reduceToCountry(List<Company> squadrons, Date date, ICountry country) throws PWCGException
    {
        List<Company> squadronsForCountry = new ArrayList<>();
        for (Company squadron : squadrons)
        {
            ICountry squadCountry = squadron.determineSquadronCountry(date);
            if (squadCountry.equals(country))
            {
                squadronsForCountry.add(squadron);
            }
        }
        return squadronsForCountry;
    }

    public static List<Company> reduceToRole(List<Company> squadrons, List<PwcgRole> acceptableRoles, Date date) throws PWCGException 
    {       
        Map<Integer, Company> squadronsWithRole = new HashMap<>();
        for(Company squadron : squadrons)
        {
            for (PwcgRole acceptableRole : acceptableRoles)
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
