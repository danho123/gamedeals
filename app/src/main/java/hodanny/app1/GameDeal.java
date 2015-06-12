package hodanny.app1;

import org.json.JSONObject;

/**
 * Created by thho on 6/9/2015.
 */
public class GameDeal
{
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private String thumbnail;

    public GameDeal(JSONObject obj)
    {
        try {

            this.title = obj.get("title").toString();
            this.url = obj.get("url").toString();
            this.thumbnail = obj.get("thumbnail").toString();
        }
        catch (Exception e)
        {

        }
    }
}
