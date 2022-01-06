package pwcg.campaign.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.SquadHistory;
import pwcg.campaign.SquadHistoryEntry;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapForAirfieldFinder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.PwcgRole;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.CompanyTankAssignment;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankType;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

public class Company 
{
    public static final Integer HOME_ASSIGNMENT = -2;
    public static final Integer REPLACEMENT = -1;
    public static final Integer SQUADRON_ID_ANY = -1;
    public static final Integer COMPANY_STAFF_SIZE = 16;
    public static final Integer COMPANY_EQUIPMENT_SIZE = 16;
    public static final Integer MIN_REEQUIPMENT_SIZE = 10;
    public static final Integer REPLACEMENTS_TANKS_PER_COMPANY = 3;
    private Country country = Country.NEUTRAL;
	private int squadronId = 0;
    private String name = "";
    private String fileName = "";
	private int skill = 50;
	private String unitIdCode;
	private String subUnitIdCode;
	private List<CompanyTankAssignment> tankAssignments = new ArrayList<>();
    private Map<Date, String> bases = new TreeMap<>();
	private List<Skin> skins = new ArrayList<Skin>();
	private SquadHistory squadHistory;
	private int serviceId;
    private SquadronRoleSet squadronRoles = new SquadronRoleSet();
    private NightMissionSet nightMissionOdds = new NightMissionSet();
	private List<SquadronConversionPeriod> conversionPeriods = new ArrayList<>();
    private Map<Date, Callsign> callsigns = new TreeMap<>();
	
	public static boolean isPlayerCompany (Campaign campaign, int squadronId)
	{
	    CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);
        if (squadronPersonnel != null && squadronPersonnel.isPlayerSquadron())
        {
            return true;
        }
        
        return false;
	}

	public List<TankType> determineCurrentAircraftList(Date now) throws PWCGException
	{
		TreeMap<String, TankType> currentAircraftByGoodness = new TreeMap<>();
		
		for (CompanyTankAssignment planeAssignment : tankAssignments)
		{
			Date introduction = planeAssignment.getCompanyIntroduction();
			Date withdrawal = planeAssignment.getCompanyWithdrawal();
			
			if (introduction.before(now) || introduction.equals((now)))
			{
				if (withdrawal.after(now) || withdrawal.equals((now)))
				{
				    List<TankType> planeTypesForArchType = PWCGContext.getInstance().getTankTypeFactory().createActiveTankTypesForArchType(planeAssignment.getArchType(), now);
				    for (TankType planeType : planeTypesForArchType)
				    {
				        TankType plane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(planeType.getType());
				        currentAircraftByGoodness.put("" + plane.getGoodness() + planeType.getType(), plane);
				    }
				}
			}			
		}
		
		List<TankType> currentAircraftByQuality = new ArrayList<TankType>();
		currentAircraftByQuality.addAll(currentAircraftByGoodness.values());
		
		return currentAircraftByQuality;
	}

    public List<TankArchType> determineCurrentAircraftArchTypes(Date now) throws PWCGException
    {
        List<TankArchType> currentTankArchTypes = new ArrayList<>();
        
        for (CompanyTankAssignment planeAssignment : tankAssignments)
        {
            Date introduction = planeAssignment.getCompanyIntroduction();
            Date withdrawal = planeAssignment.getCompanyWithdrawal();
            
            if (introduction.before(now) || introduction.equals((now)))
            {
                if (withdrawal.after(now) || withdrawal.equals((now)))
                {
                    TankArchType planeArchType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(planeAssignment.getArchType());
                    currentTankArchTypes.add(planeArchType);
                }
            }           
        }

        return currentTankArchTypes;
    }


    public boolean isPlaneInActiveSquadronArchTypes(Date date, EquippedTank plane) throws PWCGException
    {
        boolean isActiveArchType = false;
        for (TankArchType archType : determineCurrentAircraftArchTypes(date))
        {
            if (plane.getArchType().equals(archType.getTankArchTypeName()))
            {
                isActiveArchType = true;
                break;
            }
        }
        return isActiveArchType;
    }

	public String determineCurrentAirfieldName(Date campaignDate)
	{
        String currentAirFieldName = null;
        
        for (Date airfieldStartDate : bases.keySet())
        {
            if (!airfieldStartDate.after(campaignDate))
            {
                currentAirFieldName = bases.get(airfieldStartDate);
            }
            else
            {
                break;
            }
        }
        
        return currentAirFieldName;
	}

    public void assignAirfield(Date assignmentDate, String airfield) throws PWCGException 
    {
        bases.put(assignmentDate, airfield);
        TreeMap<Date, String> airfieldModified = new TreeMap<>();
        for (Date fieldDate : bases.keySet())
        {
            airfieldModified.put(fieldDate, (bases.get(fieldDate)));
        }
        bases = airfieldModified;
    }

    public Airfield determineCurrentAirfieldAnyMap(Date campaignDate) throws PWCGException 
    {
        Airfield field = null;
        
        String airfieldName = determineCurrentAirfieldName(campaignDate);
        if (airfieldName != null)
        {
            field =  PWCGContext.getInstance().getAirfieldAllMaps(airfieldName);
        }
        
        return field;
    }

    public Airfield determineCurrentAirfieldCurrentMap(Date campaignDate)
    {
        Airfield field = null;
        
        String airfieldName = determineCurrentAirfieldName(campaignDate);
        if (airfieldName != null)
        {
            field =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
        }
        
        return field;
    }

    public Coordinate determineCurrentPosition(Date campaignDate) throws PWCGException 
    {
        Airfield field = determineCurrentAirfieldAnyMap(campaignDate);
        if (field != null)
        {
            return field.getPosition().copy();
        }
        return null;
    }

    public Date determineActivetDate() throws PWCGException 
    {
        Date firstPlane = determineFirstAircraftDate();
        Date firstAirfield = determineFirstAirfieldDate();

        Date earliest = firstPlane;
        if (firstPlane.before(firstAirfield))
        {
            earliest = firstAirfield;
        }
        
        if (earliest.before(DateUtils.getBeginningOfGame()))
        {
            earliest = DateUtils.getBeginningOfGame();
        }
        
        return earliest;
    }

    public Date determineFirstAircraftDate() throws PWCGException 
    {
        Date firstPlaneDate = DateUtils.getEndOfWar();
        for (CompanyTankAssignment planeAssignment : tankAssignments)
        {
            if (planeAssignment.getCompanyIntroduction().before(firstPlaneDate))
            {
                firstPlaneDate = planeAssignment.getCompanyIntroduction();
            }
        }
        
        return firstPlaneDate;
    }

    public Date determineFirstAirfieldDate() throws PWCGException 
    {
        for (Date airfieldAssignmentDate : bases.keySet())
        {
            return airfieldAssignmentDate;
        }
        
        return null;
    }

	public boolean isCommandedByAce(List<TankAce> aces, Date date) throws PWCGException
	{
		boolean commanded = false;
		for (TankAce ace : aces)
		{
			IRankHelper rankObj = RankFactory.createRankHelper();			
			int rankPos = rankObj.getRankPosByService(ace.getRank(), determineServiceForSquadron(date));
			if (rankPos == 0)
			{
				commanded = true;
				break;
			}
		}
		
		return commanded;
	}
	
	public String determineDisplayName (Date date) throws PWCGException 
	{
		String displayName = name;
        SquadHistoryEntry squadHistoryEntry = getSquadronHistoryEntryForDate(date);
        if (squadHistoryEntry != null)
		{
            displayName = squadHistoryEntry.getSquadName();
		}
		
		return displayName;
	}
	
	private SquadHistoryEntry getSquadronHistoryEntryForDate(Date date) throws PWCGException
	{
        if (squadHistory != null)
        {
            SquadHistoryEntry squadHistoryEntry = squadHistory.getSquadHistoryEntry(date);
            if (squadHistoryEntry != null)
            {
                return squadHistoryEntry;
            }
        }
        
        return null;
	}

	public String determineSquadronDescription(Date date) throws PWCGException 
	{
		String squadronDescription = "";
		
		squadronDescription += "\nSquadron: " + determineDisplayName(date) + "\n\n";
		
        String status = determineSkillDescription();
        if (status != null && status.length() > 0)
        {
            squadronDescription += "Status: " + status + "\n\n";
        }
        
        Callsign callsign = determineCurrentCallsign(date);
        if (callsign != Callsign.NONE)
        {
            squadronDescription += "Callsign: " + callsign + "\n\n";
        }

		squadronDescription += "Stationed at: ";
		String fieldName = determineCurrentAirfieldName(date);
		squadronDescription += fieldName + "\n\n";
		
		List<TankType> planes = determineCurrentAircraftList(date);
		squadronDescription += "Operating the:\n";
		for (TankType plane : planes)
		{
			squadronDescription += "    " + plane.getDisplayName() + "\n";
		}

		Campaign campaign =     PWCGContext.getInstance().getCampaign();
		List<TankAce> aces =  PWCGContext.getInstance().getAceManager().
		                getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), campaign.getDate(), getCompanyId());

		squadronDescription += "\nAces on staff:\n";
		for (TankAce ace : aces)
		{
			squadronDescription += "    " + ace.getNameAndRank() + "\n";
		}

		return squadronDescription;
	}

	public List<Date> determineDatesSquadronAtField(Airfield field)
	{
		List<Date> datesSquadronAtField = new ArrayList<Date>();
		
		for (String airfieldName : bases.values())
		{
			if (airfieldName.equals(field.getName()))
			{
				datesSquadronAtField.add(field.getStartDate());
			}
		}
		
		return datesSquadronAtField;
	}

    public ICountry determineEnemyCountry(Date date) throws PWCGException
    {
        List<Company> squads = null;
        
        ICountry squadronCountry = CountryFactory.makeCountryByCountry(country);
        Side enemySide = squadronCountry.getSideNoNeutral().getOppositeSide();
        Airfield field = determineCurrentAirfieldCurrentMap(date);
        squads =  PWCGContext.getInstance().getCompanyManager().getActiveCompaniesBySideAndProximity(enemySide, date, field.getPosition(), 30000);

        // Use an enemy squadron as a reference country.
        // If no enemy squadron use the enemy map reference nation
        ICountry enemyCountry = CountryFactory.makeNeutralCountry();
        if (squads.size() == 0)
        {
            enemyCountry = CountryFactory.makeMapReferenceCountry(enemySide);
        }
        else
        {
            enemyCountry = squads.get(0).determineSquadronCountry(date);
        }
        
        
        return enemyCountry;
    }

    public Side determineEnemySide() throws PWCGException
    {
        ICountry squadronCountry = CountryFactory.makeCountryByCountry(country);
        Side enemySide = squadronCountry.getSideNoNeutral().getOppositeSide();
        return enemySide;
    }

    public Side determineSide() throws PWCGException
    {
        ICountry squadronCountry = CountryFactory.makeCountryByCountry(country);
        return squadronCountry.getSideNoNeutral();
    }

	public ArmedService determineServiceForSquadron(Date date) throws PWCGException 
	{
	    ArmedService service = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId, date);

		if (date != null)
		{
	        SquadHistoryEntry squadHistoryEntry = getSquadronHistoryEntryForDate(date);
	        if (squadHistoryEntry != null)
	        {
                String serviceName = squadHistoryEntry.getArmedServiceName();
                service = ArmedServiceFactory.createServiceManager().getArmedServiceByName(serviceName, date);
	        }
		}
		
		return service;
	}

	public boolean isHomeDefense(Date date) throws PWCGException
	{
		boolean isHomeDefense = false;
		String name = this.determineDisplayName(date);
		if (name.contains("(HD)") || name.contains("Kest"))
		{
		    isHomeDefense = true;
		}

		return isHomeDefense;
	}

	public boolean isInConversionPeriod(Date date) throws PWCGException
    {
        for (SquadronConversionPeriod conversionPeriod: conversionPeriods)
        {
            if (conversionPeriod.isConversionPeriodActive(date))
            {
                return true;
            }
        }
        
        return false;
    }

    public void setConversionPeriods(List<SquadronConversionPeriod> conversionPeriods)
    {
        this.conversionPeriods = conversionPeriods;
    }

    public TankType determineBestPlane(Date date) throws PWCGException
    {
        TankType bestPlane = null;
        List<TankType> planes = this.determineCurrentAircraftList(date);
        for (TankType plane : planes)
        {
            if (bestPlane == null)
            {
                bestPlane = plane;
            }
            else
            {
                if (plane.getGoodness() > bestPlane.getGoodness())
                {
                    bestPlane = plane;
                }
            }
        }
        
        return bestPlane;
    }
    
    public TankType determineEarliestPlane() throws PWCGException
    {
        TreeMap<Date, TankType> planeTypesTypeByIntroduction = new TreeMap<>();
        for (CompanyTankAssignment planeAssignment : tankAssignments)
        {
            List<TankType> planeTypesForArchType = PWCGContext.getInstance().getTankTypeFactory().createPlanesByIntroduction(planeAssignment.getArchType());
            for (TankType planeType : planeTypesForArchType)
            {
                planeTypesTypeByIntroduction.put(planeType.getIntroduction(), planeType);
            }
        }
 
        List<TankType> planes = new ArrayList<>(planeTypesTypeByIntroduction.values());
        return planes.get(0);
    }

    public String determineSquadronInfo(Date campaignDate) throws PWCGException 
    {
        StringBuffer squadronInfo = new StringBuffer("");
        
        squadronInfo.append(determineDisplayName(campaignDate) + "\n");
        
        String status = determineSkillDescription();
        if (status != null && status.length() > 0)
        {
            squadronInfo.append("Status: " + status + "\n");
        }
        
        squadronInfo.append(DateUtils.getDateString(campaignDate) + "\n");
        List <TankType> planes = determineCurrentAircraftList(campaignDate);
        for (TankType plane : planes)
        {
            squadronInfo.append(plane.getDisplayName() + "   ");
        }
        squadronInfo.append("\n");

        squadronInfo.append("\n");
        squadronInfo.append("\n");

        return squadronInfo.toString();
    }

    public PWCGMap getMapForAirfield(Date campaignDate)
    {
        String airfieldName = determineCurrentAirfieldName(campaignDate);
    	List<FrontMapIdentifier> airfieldMapIdentifiers = MapForAirfieldFinder.getMapForAirfield(airfieldName);
    	PWCGMap map = PWCGContext.getInstance().getMapByMapId(airfieldMapIdentifiers.get(0));
        return map;
    }

    public boolean isStartsCloseToFront(Date date) throws PWCGException
    {
        Side enemySide = determineEnemyCountry(date).getSide();
        Coordinate squadronPosition = determineCurrentPosition(date);
        FrontLinePoint closestFrontPosition = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date).findClosestFrontPositionForSide(squadronPosition, enemySide);
        double distanceToFront = MathUtils.calcDist(squadronPosition, closestFrontPosition.getPosition());
        
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int closeToFrontDistance = productSpecificConfiguration.getCloseToFrontDistance();
        if (distanceToFront <= closeToFrontDistance)
        {
            return true;
        }
        
        return false;
    }

    public String determineSkillDescription()
    {
        if (skill >= 90)
        {
            return "Elite";
        }
        else if (skill >= 70)
        {
            return "Veteran";
        }
        else if (skill >= 50)
        {
            return "Competent";
        }
        else
        {
            return "Novice";
        }
    }

    public ICountry determineSquadronCountry(Date date) throws PWCGException 
    {
        ICountry squadronCountry = CountryFactory.makeCountryByCountry(country);
        
        if (date != null)
        {
            SquadHistoryEntry squadHistoryEntry = getSquadronHistoryEntryForDate(date);
            if (squadHistoryEntry != null)
            {
                String serviceName = squadHistoryEntry.getArmedServiceName();
                ArmedService service = ArmedServiceFactory.createServiceManager().getArmedServiceByName(serviceName, date);
                squadronCountry = CountryFactory.makeCountryByService(service);
            }
        }
        
        return squadronCountry;
    }

	public ICountry getCountry() 
	{
		return CountryFactory.makeCountryByCountry(country);
	}

	public int getCompanyId() 
	{
		return squadronId;
	}

	public void setSquadronId(int id) 
	{
		this.squadronId = id;
	}

	public int determineSquadronSkill(Date date) throws PWCGException 
	{
	    int skillNow = skill;
        SquadHistoryEntry squadHistoryEntry = getSquadronHistoryEntryForDate(date);
        if (date != null)
        {
            if (squadHistoryEntry != null)
            {
                int skillAtDate = squadHistoryEntry.getSkill();
                if (skillAtDate != SquadHistoryEntry.NO_SQUADRON_SKILL_CHANGE && skillAtDate > 20)
                {
                    skillNow = skillAtDate;
                }
            }
        }
        
        return skillNow;
	}

	public void setSkill(int skill) 
	{
		this.skill = skill;
	}

    public List<String> getActiveArchTypes(Date date) throws PWCGException 
    {
        List<String> activeArchTypes = new ArrayList<>();
        for (CompanyTankAssignment planeAssignment : tankAssignments)
        {
            if (DateUtils.isDateInRange(date, planeAssignment.getCompanyIntroduction(), planeAssignment.getCompanyWithdrawal()))
            {
                activeArchTypes.add(planeAssignment.getArchType());
            }
        }
        return activeArchTypes;
    }

    public List<String> getAllArchTypes() throws PWCGException 
    {
        List<String> activeArchTypes = new ArrayList<>();
        for (CompanyTankAssignment planeAssignment : tankAssignments)
        {
            activeArchTypes.add(planeAssignment.getArchType());
        }
        return activeArchTypes;
    }

	public Callsign determineCurrentCallsign(Date campaignDate)
	{
		Callsign currentCallsign = Callsign.NONE;

		for (Date callsignStartDate : callsigns.keySet())
		{
			if (!callsignStartDate.after(campaignDate))
			{
				currentCallsign = callsigns.get(callsignStartDate);
			}
			else
			{
				break;
			}
		}

		return currentCallsign;
	}

	public HashMap<String, String> getNamesInUse(Campaign campaign) throws PWCGException
    {
	    CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(this.getCompanyId());
        HashMap<String, String> namesUsed = new HashMap <String, String>();
	    if (squadronPersonnel != null)
	    {
            CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
            for (CrewMember crewMember : squadronMembers.getCrewMemberList())
            {
                int index = crewMember.getName().indexOf(" ");
                String lastName = crewMember.getName().substring(index + 1);
                namesUsed.put(lastName, lastName);
            }
	    }
        return namesUsed;
    }

	public List<CompanyTankAssignment> getPlaneAssignments() 
	{
		return tankAssignments;
	}

	public Map<Date, String> getAirfields() 
	{
		return bases;
	}

	public void setAirfields(Map<Date, String> airfields) 
	{
		this.bases = airfields;
	}

	public SquadHistory getSquadHistory() 
	{
		return squadHistory;
	}

	public void setSquadHistory(SquadHistory squadHistory) 
	{
		this.squadHistory = squadHistory;
	}

	public int getService() 
	{
		return serviceId;
	}

	public void setService(int serviceId) 
	{
		this.serviceId = serviceId;
	}

    public int getNightOdds(Date date)
    {
        if (nightMissionOdds == null)
        {
            return 0;
        }
        else
        {
            return nightMissionOdds.determineNighMissionOdds(date);
        }
    }

    public List<Skin> getSkins()
    {
        return skins;
    }

    public void setSkins(List<Skin> skins)
    {
        this.skins = skins;
    }

    public SquadronRoleSet getSquadronRoles()
    {
        return squadronRoles;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public void setTankAssignments(List<CompanyTankAssignment> tankAssignments)
    {
        this.tankAssignments = tankAssignments;
    }

    public String determineUnitIdCode(Date date) throws PWCGException
    {
        String code = unitIdCode;

        if (date != null)
        {
            SquadHistoryEntry squadHistoryEntry = getSquadronHistoryEntryForDate(date);
            if (squadHistoryEntry != null)
            {
                code = squadHistoryEntry.getUnitIdCode();
                if (code == null)
                {
                    code = "";
                }
            }
        }

        return code;
    }

    public String determineSubUnitIdCode(Date date) throws PWCGException
    {
        String code = subUnitIdCode;

        if (date != null)
        {
            SquadHistoryEntry squadHistoryEntry = getSquadronHistoryEntryForDate(date);
            if (squadHistoryEntry != null)
            {
                code = squadHistoryEntry.getSubUnitIdCode();
            }
        }

        return code;
    }

    public boolean isSquadronThisRole (Date date, PwcgRole requestedRole) throws PWCGException 
    {
        return squadronRoles.isSquadronThisRole(date, requestedRole);
    }
    
    public PwcgRoleCategory determineSquadronPrimaryRoleCategory(Date date) throws PWCGException
    {
        return squadronRoles.selectSquadronPrimaryRoleCategory(date);
    }
}
