package pwcg.campaign.company;

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
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CompanyViability
{
    public static boolean isCompanyActive(Company company, Date date) throws PWCGException 
    {       
        if (date.after(DateUtils.getEndOfWar()))
        {
            return false;
        }
        
        if (company.determineCurrentAircraftList(date).size() == 0)
        {
            return false;
        }
        
        if (company.determineCurrentAirfieldAnyMap(date) == null)
        {
            return false;
        }
        
        return true; 
    }
    
    public static boolean isCompanyPlayable(Company company, Date date) throws PWCGException 
    {       
        if (!isCompanyActive(company, date))
        {
            return false;
        }
        
        return true; 
    }

    public static boolean isCompanyViable(Company company, Campaign campaign) throws PWCGException
    {
        if (!isCompanyActive(company, campaign.getDate()))
        {
            return false;
        }
        
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(company.getCompanyId());
        if (companyPersonnel == null)
        {
            return false;
        }
        
        if (!companyPersonnel.isSquadronPersonnelViable())
        {
            return false;
        }
        
        Equipment companyEquipment = campaign.getEquipmentManager().getEquipmentForCompany(company.getCompanyId());
        if (companyEquipment == null)
        {
            return false;
        }
        
        if (!companyEquipment.isCompanyEquipmentViable())
        {
            return false;
        }
        
        if (company.isInConversionPeriod(campaign.getDate()))
        {
            return false;
        }
        
        return true;
    }

    public static List<Company> reduceToAIOnly(Campaign campaign, List<Company> selectedSquadrons) throws PWCGException
    {
        List<Company> selectedSquadronsNoPlayer = new ArrayList<>();
        for (Company company : selectedSquadrons)
        {
            if (!campaign.getPersonnelManager().companyHasActivePlayers(company.getCompanyId()))
            {
                selectedSquadronsNoPlayer.add(company);
            }
        }
        return selectedSquadronsNoPlayer;
    }
    
    public static List<Company> reduceToSide(Side side, List<Company> companies) throws PWCGException
    {       
        List<Company> companiesForSide = new ArrayList<Company>();
        for (Company company : companies)
        {
            if (side == company.determineSide())
            {
                companiesForSide.add(company);
            }
        }
        return companiesForSide;
    }
    

    public static List<Company> reduceToCurrentMap(List<Company> companies, Date date) throws PWCGException 
    {
        List<Company> listForMap = new ArrayList<Company>();
        for (Company company : companies)
        {
            Airfield field = company.determineCurrentAirfieldCurrentMap(date);
            if (field != null)
            {
                listForMap.add(company);
            }
        }

        return listForMap;
    }
    
    public static List<Company> reduceToService(List<Company> companies, Date date, ArmedService service) throws PWCGException 
    {
        List<Company> companiesForService = new ArrayList<>();
        for (Company company : companies)
        {
            if (company.determineServiceForSquadron(date).getServiceId() == service.getServiceId())
            {
                companiesForService.add(company);
            }
        }
        return companiesForService;
    }
    

    public static List<Company> reduceToCountry(List<Company> companies, Date date, ICountry country) throws PWCGException
    {
        List<Company> companiesForCountry = new ArrayList<>();
        for (Company company : companies)
        {
            ICountry squadCountry = company.determineSquadronCountry(date);
            if (squadCountry.equals(country))
            {
                companiesForCountry.add(company);
            }
        }
        return companiesForCountry;
    }

    public static List<Company> reduceToRole(List<Company> companies, List<PwcgRole> acceptableRoles, Date date) throws PWCGException 
    {       
        Map<Integer, Company> companiesWithRole = new HashMap<>();
        for(Company company : companies)
        {
            for (PwcgRole acceptableRole : acceptableRoles)
            {
                if (company.isSquadronThisRole(date, acceptableRole))
                {
                    companiesWithRole.put(company.getCompanyId(), company);
                }
            }
        }

        return new ArrayList<>(companiesWithRole.values());
    }
}
