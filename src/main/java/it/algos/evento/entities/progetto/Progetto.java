package it.algos.evento.entities.progetto;

import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.entity.DefaultSort;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
@DefaultSort({"descrizione"})
public class Progetto extends CompanyEntity {

	private static final long serialVersionUID = 7804801140870806369L;

	@NotEmpty
	@Size(min = 4)
	private String descrizione;

	public Progetto() {
		this("");
	}// end of constructor

	public Progetto(String descrizione) {
		super();
		this.setDescrizione(descrizione);
	}// end of constructor

	@Override
	public String toString() {
		return getDescrizione();
	}// end of method

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
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
		Progetto other = (Progetto) obj;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		return true;
	}
	
	

}// end of entity class
