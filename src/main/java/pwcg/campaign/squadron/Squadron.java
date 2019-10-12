package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.SquadHistory;
import pwcg.campaign.SquadHistoryEntry;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.MapForAirfieldFinder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.Ace;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.MathUtils;

public class Squadron 
{
    public static final Integer HOME_ASSIGNMENT = -2;
    public static final Integer REPLACEMENT = -1;
    public static final Integer SQUADRON_STAFF_SIZE = 12;
    public static final Integer SQUADRON_EQUIPMENT_SIZE = 14;
    public static final Integer MIN_REEQUIPMENT_SIZE = 10;
    public static final Integer REPLACEMENTS_AIRCRAFT_PER_SQUADRON = 3;
    private Country country = Country.NEUTRAL;
	private int squadronId = 0;
    private String name = "";
    private String fileName = "";
	private int skill = 50;
	private List<SquadronPlaneAssignment> planeAssignments = new ArrayList<>();
    private Map<Date, String> airfields = new TreeMap<>();
	private List<Skin> skins = new ArrayList<Skin>();
	private SquadHistory squadHistory;
	private int serviceId;
	private Date nightDate;
	private SquadronRoleSet squadronRoles = new SquadronRoleSet();
	private List<SquadronConversionPeriod> conversionPeriods = new ArrayList<>();
    private Map<Date, Callsign> callsigns = new TreeMap<>();

	public Squadron ()
	{
	}
	
	public static boolean isPlayerSquadron (Campaign campaign, int squadronId)
	{
        if (campaign.getPersonnelManager().getSquadronPersonnel(squadronId).isPlayerSquadron())
        {
            return true;
        }
        
        return false;
	}

	public List<PlaneType> determineCurrentAircraftList(Date now) throws PWCGException
	{
		TreeMap<Integer, PlaneType> currentAircraftByGoodness = new TreeMap<Integer, PlaneType>();
		
		for (SquadronPlaneAssignment planeAssignment : planeAssignments)
		{
			Date introduction = planeAssignment.getSquadronIntroduction();
			Date withdrawal = planeAssignment.getSquadronWithdrawal();
			
			if (introduction.before(now) || introduction.equals((now)))
			{
				if (withdrawal.after(now) || withdrawal.equals((now)))
				{
				    List<PlaneType> planeTypesForArchType = PWCGContext.getInstance().getPlaneTypeFactory().createActivePlaneTypesForArchType(planeAssignment.getArchType(), now);
				    for (PlaneType planeType : planeTypesForArchType)
				    {
				        PlaneType plane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeType.getType());
				        currentAircraftByGoodness.put(plane.getGoodness(), plane);
				    }
				}
			}			
		}
		
		List<PlaneType> currentAircraftByQuality = new ArrayList<PlaneType>();
		currentAircraftByQuality.addAll(currentAircraftByGoodness.values());
		
		return currentAircraftByQuality;
	}

    public List<PlaneArchType> determineCurrentAircraftArchTypes(Date now) throws PWCGException
    {
        List<PlaneArchType> currentPlaneArchTypes = new ArrayList<>();
        
        for (SquadronPlaneAssignment planeAssignment : planeAssignments)
        {
            Date introduction = planeAssignment.getSquadronIntroduction();
            Date withdrawal = planeAssignment.getSquadronWithdrawal();
            
            if (introduction.before(now) || introduction.equals((now)))
            {
                if (withdrawal.after(now) || withdrawal.equals((now)))
                {
                    PlaneArchType planeArchType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneArchType(planeAssignment.getArchType());
                    currentPlaneArchTypes.add(planeArchType);
                }
            }           
        }

        return currentPlaneArchTypes;
    }


    public boolean isPlaneInActiveSquadronArchTypes(Date date, EquippedPlane plane) throws PWCGException
    {
        boolean isActiveArchType = false;
        for (PlaneArchType archType : determineCurrentAircraftArchTypes(date))
        {
            if (plane.getArchType().equals(archType.getPlaneArchTypeName()))
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
		
		for (Date airfieldStartDate : airfields.keySet())
		{
			if (!airfieldStartDate.after(campaignDate))
			{
				currentAirFieldName = airfields.get(airfieldStartDate);
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
        airfields.put(assignmentDate, airfield);
    }

    public IAirfield determineCurrentAirfieldAnyMap(Date campaignDate) throws PWCGException 
    {
        IAirfield field = null;
        
        String airfieldName = determineCurrentAirfieldName(campaignDate);
        if (airfieldName != null)
        {
            field =  PWCGContext.getInstance().getAirfieldAllMaps(airfieldName);
        }
        
        return field;
    }

    public IAirfield determineCurrentAirfieldCurrentMap(Date campaignDate)
    {
        IAirfield field = null;
        
        String airfieldName = determineCurrentAirfieldName(campaignDate);
        if (airfieldName != null)
        {
            field =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
        }
        
        return field;
    }

    public Coordinate determineCurrentPosition(Date campaignDate) throws PWCGException 
    {
        IAirfield field = determineCurrentAirfieldAnyMap(campaignDate);
        if (field != null)
        {
            return field.getPosition().copy();
        }
        return null;
    }

	public boolean isCanFly(Date date) throws PWCGException 
	{		
		String currentAirfield = determineCurrentAirfieldName(date);
		if (currentAirfield == null)
		{
		    Logger.log(LogLevel.DEBUG, determineDisplayName(date) + ": Cannot fly airfield is null");
		    return false;
		}
		
		if (date.after(DateUtils.getEndOfWWIIRussia()))
		{
		    Date lastDate = null;
		    for (Date airfieldStartDate : airfields.keySet())
		    {
		        lastDate = airfieldStartDate;
		    }
		    if (lastDate.before(DateUtils.getEndOfWWIIRussia()))
		    {
		        return false;
		    }
		}
        
        if (!hasFlyablePlane(date))
        {
            Logger.log(LogLevel.DEBUG, determineDisplayName(date) + ": Cannot fly aircraft is null");
            return false;
        }
        else
        {
            Logger.log(LogLevel.DEBUG, determineDisplayName(date) + ": Can fly fom airfield " + currentAirfield);
            return true; 
        }
	}
	
	public boolean hasFlyablePlane(Date date) throws PWCGException
	{
        List<PlaneType> currentAircraftSet = this.determineCurrentAircraftList(date);
        for (PlaneType currentAircraft : currentAircraftSet)
        {
            if (currentAircraft.isFlyable())
            {
                return true;
            }
        }
        return false;
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
        for (SquadronPlaneAssignment planeAssignment : planeAssignments)
        {
            if (planeAssignment.getSquadronIntroduction().before(firstPlaneDate))
            {
                firstPlaneDate = planeAssignment.getSquadronIntroduction();
            }
        }
        
        return firstPlaneDate;
    }

    public Date determineFirstAirfieldDate() throws PWCGException 
    {
        for (Date airfieldAssignmentDate : airfields.keySet())
        {
            return airfieldAssignmentDate;
        }
        
        return null;
    }

	public boolean isCommandedByAce(List<Ace> aces, Date date) throws PWCGException
	{
		boolean commanded = false;
		for (Ace ace : aces)
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
		
		squadronDescription += "Squadron " + determineDisplayName(date) + "\n\n";
		
        if (determineSquadronPrimaryRole(date) == Role.ROLE_FIGHTER)
        {
            String status = determineSkillDescription();
            if (status != null && status.length() > 0)
            {
                squadronDescription += "Status: " + status + "\n\n";
            }
        }
        
        Callsign callsign = determineCurrentCallsign(date);
        if (callsign != Callsign.NONE)
        {
            squadronDescription += "Callsign: " + callsign + "\n\n";
        }

		squadronDescription += "Stationed at\n";
		String fieldName = determineCurrentAirfieldName(date);
		squadronDescription += "    " + fieldName + "\n\n";
		
		List<PlaneType> planes = determineCurrentAircraftList(date);
		squadronDescription += "Flying the \n";
		for (PlaneType plane : planes)
		{
			squadronDescription += "    " + plane.getDisplayName() + "\n";
		}

		
		Campaign campaign =     PWCGContext.getInstance().getCampaign();
		List<Ace> aces =  PWCGContext.getInstance().getAceManager().
		                getActiveAcesForSquadron(campaign.getPersonnelManager().getCampaignAces(), campaign.getDate(), getSquadronId());

		squadronDescription += "\nAces on staff \n";
		for (Ace ace : aces)
		{
			squadronDescription += "    " + ace.getRank() + " " + ace.getSerialNumber() + "\n";
		}

		return squadronDescription;
	}

	public List<Date> determineDatesSquadronAtField(IAirfield field)
	{
		List<Date> datesSquadronAtField = new ArrayList<Date>();
		
		for (String airfieldName : airfields.values())
		{
			if (airfieldName.equals(field.getName()))
			{
				datesSquadronAtField.add(field.getStartDate());
			}
		}
		
		return datesSquadronAtField;
	}

    public ICountry determineEnemyCountry(Campaign campaign, Date date) throws PWCGException
    {
        List<Squadron> squads = null;
        Coordinate squadronPosition = this.determineCurrentPosition(date);
        
        ICountry squadronCountry = CountryFactory.makeCountryByCountry(country);
        Side enemySide = squadronCountry.getSideNoNeutral().getOppositeSide();
        squads =  PWCGContext.getInstance().getSquadronManager().getNearestSquadronsBySide(campaign, squadronPosition, 1, 10000.0, enemySide, date);

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

	public boolean isSquadronViable(Campaign campaign) throws PWCGException
	{
	    SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);
	    
	    if (squadronPersonnel == null)
	    {
	        return false;
	    }
	    
        if (!squadronPersonnel.isSquadronPersonnelViable())
        {
            return false;
        }
        
        if (campaign.getEquipmentManager().getEquipmentForSquadron(squadronId) == null)
        {
            return false;
        }
        
        if (!campaign.getEquipmentManager().getEquipmentForSquadron(squadronId).isSquadronEquipmentViable())
        {
            return false;
        }
        
        if (isInConversionPeriod(campaign.getDate()))
        {
            return false;
        }
        
	    return true;
	}
	
    private boolean isInConversionPeriod(Date date) throws PWCGException
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

    public boolean isSquadronThisRole (Date date, Role requestedRole) throws PWCGException 
    {
        return squadronRoles.isSquadronThisRole(date, requestedRole);
    }

    public boolean isSquadronThisPrimaryRole (Date date, Role requestedRole) throws PWCGException 
    {
        return squadronRoles.isSquadronThisPrimaryRole(date, requestedRole);
    }
    
    public Role determineSquadronPrimaryRole(Date date) throws PWCGException
    {
        return squadronRoles.selectSquadronPrimaryRole(date);
    }

    public PlaneType determineBestPlane(Date date) throws PWCGException
    {
        PlaneType bestPlane = null;
        List<PlaneType> planes = this.determineCurrentAircraftList(date);
        for (PlaneType plane : planes)
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
    
    public PlaneType determineEarliestPlane() throws PWCGException
    {
        TreeMap<Date, PlaneType> planeTypesTypeByIntroduction = new TreeMap<>();
        for (SquadronPlaneAssignment planeAssignment : planeAssignments)
        {
            List<PlaneType> planeTypesForArchType = PWCGContext.getInstance().getPlaneTypeFactory().createPlanesByIntroduction(planeAssignment.getArchType());
            for (PlaneType planeType : planeTypesForArchType)
            {
                planeTypesTypeByIntroduction.put(planeType.getIntroduction(), planeType);
            }
        }
 
        List<PlaneType> planes = new ArrayList<>(planeTypesTypeByIntroduction.values());
        return planes.get(0);
    }

    public String determineSquadronInfo(Date campaignDate) throws PWCGException 
    {
        StringBuffer squadronInfo = new StringBuffer("");
        
        squadronInfo.append(determineDisplayName(campaignDate) + "\n");
        
        if (this.determineSquadronPrimaryRole(campaignDate) == Role.ROLE_FIGHTER)
        {
            String status = determineSkillDescription();
            if (status != null && status.length() > 0)
            {
                squadronInfo.append("Status: " + status + "\n");
            }
        }
        
        squadronInfo.append(DateUtils.getDateString(campaignDate) + "\n");
        List <PlaneType> planes = determineCurrentAircraftList(campaignDate);
        for (PlaneType plane : planes)
        {
            squadronInfo.append(plane.getDisplayName() + "   ");
        }
        squadronInfo.append("\n");

        squadronInfo.append("Airfield: " + determineCurrentAirfieldName(campaignDate) + "\n");
        
    	String airfieldName = determineCurrentAirfieldName(campaignDate);
    	List<FrontMapIdentifier> airfieldMapIdentifiers = MapForAirfieldFinder.getMapForAirfield(airfieldName);
    	PWCGMap map = PWCGContext.getInstance().getMapByMapId(airfieldMapIdentifiers.get(0));
        squadronInfo.append("Map: " + map.getMapName() + "\n");


        squadronInfo.append("\n");
        squadronInfo.append("\n");

        return squadronInfo.toString();
    }

    public boolean isStartsCloseToFront(Campaign campaign, Date date) throws PWCGException
    {
        Side enemySide = determineEnemyCountry(campaign, date).getSide();
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

    public boolean determineIsNightSquadron()
    {
        if (nightDate != null)
        {
            Date campaignDate = PWCGContext.getInstance().getCampaign().getDate();
            if (nightDate.before(campaignDate))
            {
                return true;
            }
        }
        
        return false;
    }

	public ICountry getCountry() 
	{
		return CountryFactory.makeCountryByCountry(country);
	}

	public int getSquadronId() 
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

    public double determineSquadronRange(Date date) throws PWCGException 
    {
        double squadronRange = this.determineBestPlane(date).getRange() * 1000;
        squadronRange *= 0.45;
        return squadronRange;
    }

	public void setSkill(int skill) 
	{
		this.skill = skill;
	}

    public List<String> getActiveArchTypes(Date date) throws PWCGException 
    {
        List<String> activeArchTypes = new ArrayList<>();
        for (SquadronPlaneAssignment planeAssignment : planeAssignments)
        {
            if (DateUtils.isDateInRange(date, planeAssignment.getSquadronIntroduction(), planeAssignment.getSquadronWithdrawal()))
            {
                activeArchTypes.add(planeAssignment.getArchType());
            }
        }
        return activeArchTypes;
    }

    public List<String> getAllArchTypes() throws PWCGException 
    {
        List<String> activeArchTypes = new ArrayList<>();
        for (SquadronPlaneAssignment planeAssignment : planeAssignments)
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

	public List<SquadronPlaneAssignment> getPlaneAssignments() 
	{
		return planeAssignments;
	}

	public Map<Date, String> getAirfields() 
	{
		return airfields;
	}

	public void setAirfields(Map<Date, String> airfields) 
	{
		this.airfields = airfields;
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

	public Date getNightDate()
	{
		return nightDate;
	}

	public void setNightDate(Date nightDate) 
	{
		this.nightDate = nightDate;
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
}
