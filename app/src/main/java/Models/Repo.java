package Models;

/**
 * Created by root on 17/12/14.
 */
public class Repo {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommits_url() {
        return commits_url;
    }

    public void setCommits_url(String commits_url) {
        this.commits_url = commits_url;
    }

    private String descripcion;
    private String url;
    private String commits_url;
    public Repo(String name, String descripcion,String url, String commits_url){
        this.name=name;
        this.descripcion=descripcion;
        this.url=url;
        this.commits_url=commits_url;

    }
}
