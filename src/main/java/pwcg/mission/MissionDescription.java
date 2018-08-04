package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.options.MapWeather;
import pwcg.mission.options.MissionOptions;

public class MissionDescription 
{
    private Mission mission = null;
    private Campaign campaign = null;
    
	private String author = "Brought to you by PWCGCampaign";
	private String title = "";
    private String html = 
                    "<br><SQUADRON> stationed at <AIRFIELD>" +
                    "<br> <DATE>" +
                    "<br>Primary Objective <OBJECTIVE>";

	private String desc = 
		"Aircraft  <AIRCRAFT>\n" +
		"Squadron  <SQUADRON>\n" +
		"Airbase  <AIRFIELD>\n" +
		"Date  <DATE>\n" +
		"\n" +
		"Weather Report \n" +
		"    <CLOUDS>\n" +
		"    <WIND>\n" +
		"\n" +
		"Primary Objective \n" +
		"    <OBJECTIVE>\n" +
		"\n";

	private String balloons = "";
	private String campaignDateString = "";
		
	private ArrayList<String> enemyIntList = new ArrayList<String>();
	private ArrayList<String> friendlyIntList = new ArrayList<String>();
	private ArrayList<String> enemyIntHtmlList = new ArrayList<String>();
	private ArrayList<String> friendlyIntHtmlList = new ArrayList<String>();
	
    public MissionDescription (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;

        campaignDateString = DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate());
    }

    public void createDescription() throws PWCGException 
    {
        MapWeather mapWeather = PWCGContextManager.getInstance().getCurrentMap().getMapWeather();
        setClouds(mapWeather.getWeatherDescription());
        setWind(mapWeather.getWindLayers().get(0));

        MissionOptions missionOptions = PWCGContextManager.getInstance().getCurrentMap().getMissionOptions();
        setMissionDateTime(DateUtils.getDateAsMissionFileFormat(campaign.getDate()), missionOptions.getMissionTime().getMissionTime());

        Flight myFlight = mission.getMissionFlightBuilder().getPlayerFlight();
        setAircraft(myFlight.getPlanes().get(0).getDisplayName());
        setAirfield(myFlight.getAirfield().getName());
        setObjective(myFlight.getMissionObjective());
        setSquadron(myFlight.getSquadron().determineDisplayName(campaign.getDate()));
        setTitle(campaign.getCampaignData().getName(), myFlight.getFlightType().toString());

        HashMap<String, Flight> squadronMap = new HashMap<String, Flight>();
        for (Flight flight : mission.getMissionFlightBuilder().getMissionFlights())
        {
            squadronMap.put(flight.getSquadron().determineDisplayName(campaign.getDate()), flight);
        }

        // Add to intelligence
        for (Flight flight : squadronMap.values())
        {
            setFlight(campaign.determineCountry(), flight);
        }

        // Add balloons to description
        boolean enemyBalloons = false;
        if (campaign.determineCountry().getSide() == Side.ALLIED)
        {
            enemyBalloons = mission.getMissionBalloons().hasAxisBalloon();
        }
        else
        {
            enemyBalloons = mission.getMissionBalloons().hasAlliedBalloon();
        }
        
        if (enemyBalloons)
        {
            setBalloons();
        }
    }

	public void setAircraft(String replacement)
	{
		desc = replace(desc, "<AIRCRAFT>", replacement);
	}
	
	public void setSquadron(String replacement)
	{
		desc = replace(desc, "<SQUADRON>", replacement);
		html = replace(html, "<SQUADRON>", replacement);
	}
	
	public void setAirfield(String replacement)
	{
		desc = replace(desc, "<AIRFIELD>", replacement);
		html = replace(html, "<AIRFIELD>", replacement);
	}
	
	private void setMissionDateTime(String missionDate, String missionTime)
	{
		desc = replace(desc, "<DATE>", missionDate);
		html = replace(html, "<DATE>", missionDate);
		
		setTime(missionTime);
	}
	   
    public void setObjective(String replacement)
    {
        desc = replace(desc, "<OBJECTIVE>", replacement);
        html = replace(html, "<OBJECTIVE>", replacement);
    }

	private void setTime(String missionTime)
	{
		desc = replace(desc, "<TIME>", missionTime);
	}
	
	public void setClouds(String replacement)
	{
		desc = replace(desc, "<CLOUDS>", replacement);
	}
	
	public void setWind(MapWeather.WindLayer layer) throws PWCGException
	{
        boolean isBritish = false;
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        if (campaign.determineCountry().isCountry(Country.BRITAIN) ||
            campaign.determineCountry().isCountry(Country.USA))
        {
            isBritish = true;
        }
        
		int windFrom = new Double(MathUtils.adjustAngle (layer.direction, 180)).intValue();		
		
		String windCond = "";
		if (isBritish)
		{
			Double  windSpeed = layer.speed * 2.2369362920544;
			windCond = "Wind speed is " + windSpeed.intValue() + " MPH." + 
			  "\nWind direction is " + windFrom + ".";
		}
		else
		{
			windCond = "Wind speed is " + layer.speed + " M/S." + 
			  "\n    Wind direction is " + windFrom + ".";			
		}
		
		desc = replace(desc, "<WIND>", windCond);
	}
	
	public void setFlight(ICountry country, Flight flight) throws PWCGException 
	{
		Campaign campaign =     PWCGContextManager.getInstance().getCampaign();
		
		String squadron = flight.getSquadron().determineDisplayName(campaign.getDate());
		String aircraft = flight.getPlanes().get(0).getDisplayName();
		ICountry flightCountry = flight.getAirfield().createCountry(campaign.getDate());
		
		if (country.isSameSide(flightCountry))
		{
			String friendlyInt = "    " + squadron + " flying " + aircraft;
			friendlyIntList.add(friendlyInt + "\n");
			
			String friendlyInthtml = "<br>    " + friendlyInt;
			friendlyIntHtmlList.add(friendlyInthtml);			
		}
		else
		{
			String enemyInt = "    " + squadron + " flying " + aircraft;
			enemyIntList.add(enemyInt + "\n");
			
			String enemyInthtml = "<br>    " + enemyInt;
			enemyIntHtmlList.add(enemyInthtml);
		}

	}

    /**
     * @param str
     * @param pattern
     * @param replacement
     * @return
     */
    private String replace(String str, String pattern, String replacement) 
    {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) 
        {
            result.append(str.substring(s, e));
            result.append(replacement);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        
        return result.toString();
    }

	/**
	 * 
	 */
	public void setBalloons()
	{
		balloons = "    Enemy balloons are in your area\n";
	}

	/**
	 * @return
	 */
	public String getHtml()
	{
		return html;
	}
	
	/**
	 * @return
	 */
	public String getDesc()
	{
		return desc;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String campaignName, String missionType) 
	{
		this.title = campaignName + " " + campaignDateString + " " + missionType;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBalloons() {
		return balloons;
	}

	public void setBalloons(String balloons) {
		this.balloons = balloons;
	}

	public String getDateStr() {
		return campaignDateString;
	}

	public void setDateStr(String dateStr) {
		this.campaignDateString = dateStr;
	}

	public ArrayList<String> getEnemyIntList() {
		return enemyIntList;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
