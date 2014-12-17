package Models;

/**
 * Created by root on 17/12/14.
 */
public class Profile {
    private String name, login, avatar_url, url;
    private String followers,  following;
    public Profile(String name, String login, String avatar_url, String url, String followers, String following) {
        this.name=name;
        this.login=login;
        this.avatar_url=avatar_url;
        this.url=url;
        this.followers=followers;
        this.following=following;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }
}
