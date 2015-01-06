package Models;

/**
 * Created by root on 17/12/14.
 */
public class Commit {
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String author,date,message,url;
    public Commit(String author,String date,String message,String url){
        this.author=author;
        this.date=date;
        this.message=message;
        this.url=url;
    }

}
