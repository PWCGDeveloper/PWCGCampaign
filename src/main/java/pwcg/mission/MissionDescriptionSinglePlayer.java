package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.escort.EscortForPlayerFlight;
import pwcg.mission.options.MapWeather;
import pwcg.mission.options.MissionOptions;

public class MissionDescriptionSinglePlayer implements IMissionDescription 
{
    private Mission mission = null;
    private Campaign campaign = null;
    
	private String author = "Brought to you by PWCGCampaign";
	private String title = "";
    private String singlePlayerHtmlTemplate = 
                    "<br><SQUADRON> stationed at <AIRFIELD>" +
                    "<br> <DATE>" +
                    "<br>Primary Objective <OBJECTIVE>" +
                    "<br> <ESCORTED_BY>";
    
	private String descSinglePlayerTemplate = 
		"Aircraft  <AIRCRAFT>\n" +
		"Squadron  <SQUADRON>\n" +
		"Airbase  <AIRFIELD>\n" +
        "Date  <DATE>\n" +
        "Time  <TIME>\n" +
		"\n" +
		"Weather Report \n" +
		"    <CLOUDS>\n" +
		"    <WIND>\n" +
		"\n" +
		"Primary Objective \n" +
        "    <OBJECTIVE>\n" +
        "\n" +
        "<ESCORTED_BY>\n" +
		"\n";

	private String campaignDateString = "";
		
	private ArrayList<String> enemyIntList = new ArrayList<String>();
	private ArrayList<String> friendlyIntList = new ArrayList<String>();
	private ArrayList<String> enemyIntHtmlList = new ArrayList<String>();
	private ArrayList<String> friendlyIntHtmlList = new ArrayList<String>();
	
    public MissionDescriptionSinglePlayer (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
        campaignDateString = DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate());
    }

	public String createDescription() throws PWCGException 
    {
		Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlights().get(0);

        MapWeather mapWeather = PWCGContext.getInstance().getCurrentMap().getMapWeather();
        setClouds(mapWeather.getWeatherDescription());
        setWind(mapWeather.getWindLayers().get(0));

        MissionOptions missionOptions = PWCGContext.getInstance().getCurrentMap().getMissionOptions();
        setMissionDateTime(DateUtils.getDateAsMissionFileFormat(campaign.getDate()), missionOptions.getMissionTime().getMissionTime());

        setAircraft(playerFlight.getPlanes().get(0).getDisplayName());
        setAirfield(playerFlight.getAirfield().getName());
        setObjective(playerFlight.getMissionObjective());
        setEscortedBy(playerFlight);
        setSquadron(playerFlight.getSquadron().determineDisplayName(campaign.getDate()));
        buildTitleDescription(campaign.getCampaignData().getName(), playerFlight.getFlightType().toString());

        HashMap<String, Flight> squadronMap = new HashMap<String, Flight>();
        for (Flight flight : mission.getMissionFlightBuilder().getAiFlights())
        {
            squadronMap.put(flight.getSquadron().determineDisplayName(campaign.getDate()), flight);
        }

        for (Flight flight : squadronMap.values())
        {
            setFlight(playerFlight.getSquadron().getCountry(), flight);
        }
        
        return descSinglePlayerTemplate;
    }

	public void setAircraft(String replacement)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<AIRCRAFT>", replacement);
	}
	
	public void setSquadron(String replacement)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<SQUADRON>", replacement);
		singlePlayerHtmlTemplate = replace(singlePlayerHtmlTemplate, "<SQUADRON>", replacement);
	}
	
	public void setAirfield(String replacement)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<AIRFIELD>", replacement);
		singlePlayerHtmlTemplate = replace(singlePlayerHtmlTemplate, "<AIRFIELD>", replacement);
	}
	
	private void setMissionDateTime(String missionDate, String missionTime)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<DATE>", missionDate);
		singlePlayerHtmlTemplate = replace(singlePlayerHtmlTemplate, "<DATE>", missionDate);
		
		setTime(missionTime);
	}
	   
    private void setObjective(String replacement)
    {
        descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<OBJECTIVE>", replacement);
        singlePlayerHtmlTemplate = replace(singlePlayerHtmlTemplate, "<OBJECTIVE>", replacement);
    }

	private void setTime(String missionTime)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<TIME>", missionTime);
	}
	
	private void setClouds(String replacement)
	{
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<CLOUDS>", replacement);
	}
	
	private void setWind(MapWeather.WindLayer layer) throws PWCGException
	{
		int windFrom = new Double(MathUtils.adjustAngle (layer.direction, 180)).intValue();		
		String windCond = "Wind speed is " + layer.speed + " M/S." + 
				  "\n    Wind direction is " + windFrom + ".";			
		
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<WIND>", windCond);
	}
	
	private void setEscortedBy(Flight playerFlight) throws PWCGException
	{
        String escortedByText = "";
        EscortForPlayerFlight escortForPlayerFlight = playerFlight.getEscortForPlayer();
        if (escortForPlayerFlight != null)
        {
            escortedByText = "Escorted by " + escortForPlayerFlight.getLeadPlane().getDisplayName() + "s of " + escortForPlayerFlight.getSquadron().determineDisplayName(campaign.getDate());
        }
	    
	    descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<ESCORTED_BY>", escortedByText);
	    singlePlayerHtmlTemplate = replace(singlePlayerHtmlTemplate, "<ESCORTED_BY>", escortedByText);
	}

	
	private void setFlight(ICountry country, Flight flight) throws PWCGException 
	{
		Campaign campaign =     PWCGContext.getInstance().getCampaign();
		
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

	private void buildTitleDescription(String campaignName, String missionType) 
	{
		this.title = campaignName + " " + campaignDateString + " " + missionType;
	}

	@Override
	public String getHtml()
	{
		return singlePlayerHtmlTemplate;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getAuthor() {
		return author;
	}
}
