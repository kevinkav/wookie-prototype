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
@Table(name="Cast")
public class Cast implements Serializable{

    @Id
    private long id;
    
    String leadActor;
    
   
    public String getLeadActor() {
        return leadActor;
    }

    public void setLeadActor(String leadActor) {
        this.leadActor = leadActor;
    }

    public long getId() {
        return id;
    }
    
    public void setId(long id){
        this.id = id;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n---------------------------------\n");
        sb.append("CastId: " + id + "\n");
        sb.append("LeadActor: " + leadActor + "\n");
        sb.append("---------------------------------\n");
        return sb.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((leadActor == null) ? 0 : leadActor.hashCode());
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
		Cast other = (Cast) obj;
		if (id != other.id)
			return false;
		if (leadActor == null) {
			if (other.leadActor != null)
				return false;
		} else if (!leadActor.equals(other.leadActor))
			return false;
		return true;
	}
    
    
}
