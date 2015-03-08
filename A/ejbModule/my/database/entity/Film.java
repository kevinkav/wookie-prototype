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
package my.database.entity;

import java.io.Serializable;

import javax.persistence.Entity;
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
        sb.append("\nFilmId: " + id + "\n");
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
        return sb.toString();
        
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cast == null) ? 0 : cast.hashCode());
		result = prime * result
				+ ((countryOfOrigin == null) ? 0 : countryOfOrigin.hashCode());
		result = prime * result
				+ ((director == null) ? 0 : director.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + runningTimeMins;
		result = prime * result + yearOfRelease;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Film other = (Film) obj;
		if (cast == null) {
			if (other.cast != null)
				return false;
		} else if (!cast.equals(other.cast))
			return false;
		if (countryOfOrigin == null) {
			if (other.countryOfOrigin != null)
				return false;
		} else if (!countryOfOrigin.equals(other.countryOfOrigin))
			return false;
		if (director == null) {
			if (other.director != null)
				return false;
		} else if (!director.equals(other.director))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (runningTimeMins != other.runningTimeMins)
			return false;
		if (yearOfRelease != other.yearOfRelease)
			return false;
		return true;
	}
    
    
    
}
