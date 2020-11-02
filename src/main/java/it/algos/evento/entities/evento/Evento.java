package it.algos.evento.entities.evento;

import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.entity.DefaultSort;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Entity
@DefaultSort({"sigla"})
public class Evento extends CompanyEntity {

	private static final long serialVersionUID = 4640617537726384074L;

	@NotEmpty
	@Size(min = 2, max = 30)
	private String sigla;

	private String titolo;

	@ManyToOne
	private Progetto progetto;

	@ManyToOne
	@NotNull
	private Stagione stagione;

	private boolean prezzoPerGruppi;// false se prezzi a persona, true se prezzi a gruppo

	@Column(precision = 6, scale = 2)
	private BigDecimal importoIntero=new BigDecimal(0);

	@Column(precision = 6, scale = 2)
	private BigDecimal importoRidotto=new BigDecimal(0);
	
	@Column(precision = 6, scale = 2)
	private BigDecimal importoDisabili=new BigDecimal(0);

	@Column(precision = 6, scale = 2)
	private BigDecimal importoAccomp=new BigDecimal(0);

	@Column(precision = 8, scale = 2)
	private BigDecimal importoGruppo =new BigDecimal(0); // importo fisso (gruppi)

	@OneToMany(mappedBy = "evento")
    @CascadeOnDelete
    private List<Rappresentazione> rappresentazioni;

	public Evento(){
		super();
	}

	public Evento(String sigla, String titolo, int impIntero, int impRidotto, int impDisabili, int impAccomp){
		this(sigla, titolo, new BigDecimal(impIntero), new BigDecimal(impRidotto), new BigDecimal(impDisabili), new BigDecimal(impAccomp));
	}

	public Evento(String sigla, String titolo, BigDecimal importoIntero, BigDecimal importoRidotto, BigDecimal importoDisabili, BigDecimal importoAccomp){
		super();
		this.setSigla(sigla);
		this.setTitolo(titolo);
		this.setImportoIntero(importoIntero);
		this.setImportoRidotto(importoRidotto);
	}



	@Override
	public String toString() {
		String testo="";
		testo=getSigla();
		Stagione stagione=getStagione();
		if (stagione!=null){
			testo+=" ("+stagione.getSigla()+")";
		}
		return testo;
	}

	public Progetto getProgetto() {
		return progetto;
	}

	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}

	public Stagione getStagione() {
		return stagione;
	}

	public void setStagione(Stagione stagione) {
		this.stagione = stagione;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public boolean isPrezzoPerGruppi() {
		return prezzoPerGruppi;
	}

	public void setPrezzoPerGruppi(boolean prezzoPerGruppi) {
		this.prezzoPerGruppi = prezzoPerGruppi;
	}

	public BigDecimal getImportoGruppo() {
		return importoGruppo;
	}

	public void setImportoGruppo(BigDecimal importoGruppo) {
		this.importoGruppo = importoGruppo;
	}

	public BigDecimal getImportoIntero() {
		return importoIntero;
	}

	public void setImportoIntero(BigDecimal importo) {
		if(importo==null){  // evitiamo i nulli nei numeri sul database
			importo=new BigDecimal(0);
		}
		this.importoIntero = importo;
	}

	public BigDecimal getImportoRidotto() {
		return importoRidotto;
	}

	public void setImportoRidotto(BigDecimal importo) {
		if(importo==null){  // evitiamo i nulli nei numeri sul database
			importo=new BigDecimal(0);
		}
		this.importoRidotto = importo;
	}
	
	/**
	 * @return the importoDisabili
	 */
	public BigDecimal getImportoDisabili() {
		return importoDisabili;
	}

	/**
	 * @param importo the importoDisabili to set
	 */
	public void setImportoDisabili(BigDecimal importo) {
		if(importo==null){  // evitiamo i nulli nei numeri sul database
			importo=new BigDecimal(0);
		}
		this.importoDisabili = importo;
	}

	/**
	 * @return the importoAccomp
	 */
	public BigDecimal getImportoAccomp() {
		return importoAccomp;
	}

	/**
	 * @param importo the importoAccomp to set
	 */
	public void setImportoAccomp(BigDecimal importo) {
		if(importo==null){  // evitiamo i nulli nei numeri sul database
			importo=new BigDecimal(0);
		}
		this.importoAccomp = importo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((importoRidotto == null) ? 0 : importoRidotto.hashCode());
		result = prime * result + ((importoIntero == null) ? 0 : importoIntero.hashCode());
		result = prime * result + ((importoDisabili == null) ? 0 : importoDisabili.hashCode());
		result = prime * result + ((importoAccomp == null) ? 0 : importoAccomp.hashCode());
		result = prime * result + ((progetto == null) ? 0 : progetto.hashCode());
		result = prime * result + ((sigla == null) ? 0 : sigla.hashCode());
		result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
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
		Evento other = (Evento) obj;
		if (importoRidotto == null) {
			if (other.importoRidotto != null)
				return false;
		} else if (!importoRidotto.equals(other.importoRidotto))
			return false;
		if (importoIntero == null) {
			if (other.importoIntero != null)
				return false;
		} else if (!importoIntero.equals(other.importoIntero))
			return false;
		if (importoDisabili == null) {
			if (other.importoDisabili != null)
				return false;
		} else if (!importoDisabili.equals(other.importoDisabili))
			return false;
		if (importoAccomp == null) {
			if (other.importoAccomp != null)
				return false;
		} else if (!importoAccomp.equals(other.importoAccomp))
			return false;
		if (progetto == null) {
			if (other.progetto != null)
				return false;
		} else if (!progetto.equals(other.progetto))
			return false;
		if (sigla == null) {
			if (other.sigla != null)
				return false;
		} else if (!sigla.equals(other.sigla))
			return false;
		if (titolo == null) {
			if (other.titolo != null)
				return false;
		} else if (!titolo.equals(other.titolo))
			return false;
		return true;
	}
	
	
	

}// end of entity class
