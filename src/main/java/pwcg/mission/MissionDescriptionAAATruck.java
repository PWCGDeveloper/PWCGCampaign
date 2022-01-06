package pwcg.mission;

import java.util.HashMap;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionWeather;
import pwcg.mission.options.WindLayer;

public class MissionDescriptionAAATruck implements IMissionDescription 
{
    private Mission mission;
    private Campaign campaign;
    
	private String author = "Brought to you by PWCGCampaign";
	private String title = "";
    private String singlePlayerHtmlTemplate = 
                    "<br> <DATE>" +
                    "<br>Primary Objective <OBJECTIVE>";
    
	private String descSinglePlayerTemplate = 
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
	
    public MissionDescriptionAAATruck (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
        campaignDateString = DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate());
    }

	public String createDescription() throws PWCGException 
    {
        MissionWeather weather = mission.getWeather();
        setClouds(weather.getWeatherDescription());
        setWind(weather.getWindLayers().get(0));

        MissionOptions missionOptions = mission.getMissionOptions();
        setMissionDateTime(DateUtils.getDateAsMissionFileFormat(campaign.getDate()), missionOptions.getMissionTime().getMissionTime());

        setObjective("Shoot down enemy aircraft");
        buildTitleDescription(campaign.getCampaignData().getName(), "AAA");

        return descSinglePlayerTemplate;
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
	
	private void setWind(WindLayer layer) throws PWCGException
	{
		int windFrom = Double.valueOf(MathUtils.adjustAngle (layer.getDirection(), 180)).intValue();		
		String windCond = "Wind speed is " + layer.getSpeed() + " M/S." + 
				  "\n    Wind direction is " + windFrom + ".";			
		
		descSinglePlayerTemplate = replace(descSinglePlayerTemplate, "<WIND>", windCond);
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
