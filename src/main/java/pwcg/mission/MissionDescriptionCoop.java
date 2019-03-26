package pwcg.mission;

import java.util.HashMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.options.MapWeather;
import pwcg.mission.options.MissionOptions;

public class MissionDescriptionCoop implements IMissionDescription 
{
    private Mission mission = null;
    private Campaign campaign = null;
    
	private String author = "Brought to you by PWCGCampaign";
	private String title = "";

    private String coopPlayerHtmlTemplate = 
            "<br>Coop Mission" +
            "<br> <DATE>";
    
	private String descCoopTemplate = 
			"Date  <DATE>\n" +
			"\n" +
			"Weather Report \n" +
			"    <CLOUDS>\n" +
			"    <WIND>\n" +
			"\n" +
			"\n";

	private String campaignDateString = "";

    public MissionDescriptionCoop (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
        campaignDateString = DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate());
    }

	public String createDescription() throws PWCGException 
    {
        MapWeather mapWeather = PWCGContextManager.getInstance().getCurrentMap().getMapWeather();
        setClouds(mapWeather.getWeatherDescription());
        setWind(mapWeather.getWindLayers().get(0));

        MissionOptions missionOptions = PWCGContextManager.getInstance().getCurrentMap().getMissionOptions();
        setMissionDateTime(DateUtils.getDateAsMissionFileFormat(campaign.getDate()), missionOptions.getMissionTime().getMissionTime());

        buildTitleDescription(campaign.getCampaignData().getName());

        HashMap<String, Flight> squadronMap = new HashMap<String, Flight>();
        for (Flight flight : mission.getMissionFlightBuilder().getMissionFlights())
        {
            squadronMap.put(flight.getSquadron().determineDisplayName(campaign.getDate()), flight);
        }
        
        return descCoopTemplate;
    }

	private void setMissionDateTime(String missionDate, String missionTime)
	{
		descCoopTemplate = replace(descCoopTemplate, "<DATE>", missionDate);
		coopPlayerHtmlTemplate = replace(coopPlayerHtmlTemplate, "<DATE>", missionDate);
		
		setTime(missionTime);
	}

	private void setTime(String missionTime)
	{
		descCoopTemplate = replace(descCoopTemplate, "<TIME>", missionTime);
	}
	
	public void setClouds(String replacement)
	{
		descCoopTemplate = replace(descCoopTemplate, "<CLOUDS>", replacement);
	}
	
	public void setWind(MapWeather.WindLayer layer) throws PWCGException
	{
		int windFrom = new Double(MathUtils.adjustAngle (layer.direction, 180)).intValue();		
		String windCond = "Wind speed is " + layer.speed + " M/S." + 
				  "\n    Wind direction is " + windFrom + ".";			
		
		descCoopTemplate = replace(descCoopTemplate, "<WIND>", windCond);
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

	private void buildTitleDescription(String campaignName) 
	{
		this.title = campaignName + " " + campaignDateString + " Coop Mission";
	}

	@Override
	public String getHtml()
	{
		return coopPlayerHtmlTemplate;
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
