/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package my.prototype.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Film")
public class Film implements Serializable {
    
    @Id
    private long id;
    
    private int yearOfRelease;
    
    private int runningTimeMins;
    
    private String director;
    
    private String countryOfOrigin;
    
    private String name;
    
    private Cast cast;
    
 
    public void setCast(Cast cast){
        this.cast = cast;
    }
    
    public Cast getCast(){
        return this.cast;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //@GeneratedValue(strategy=GenerationType.AUTO)
    public long getId() {
        return id;
    }
    
    public void setId(long id){
        this.id = id;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public int getRunningTimeMins() {
        return runningTimeMins;
    }

    public void setRunningTimeMins(int runningTimeMins) {
        this.runningTimeMins = runningTimeMins;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n---------------------------------\n");
        sb.append("FilmId: " + id + "\n");
        sb.append("Name: " + name + "\n");
        sb.append("Director: " + director + "\n");
        sb.append("RunningTime: " + runningTimeMins + "\n");
        sb.append("Year: " + yearOfRelease + "\n");
        sb.append("Country: " + countryOfOrigin + "\n");
        if (cast != null){
            sb.append("CastID: " + cast.getId() + "\n"); 
            sb.append("LeadActor: " + cast.getLeadActor() + "\n");
        }
        else{
            sb.append("CastID: \n"); 
            sb.append("LeadActor: <>" + "\n");
        }
        sb.append("---------------------------------\n");
        return sb.toString();
        
    }
}
