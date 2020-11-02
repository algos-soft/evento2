package it.algos.evento.entities.sala;

import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.query.AQuery;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.validation.constraints.Min;


@Entity
@DefaultSort({"nome"})
public class Sala extends CompanyEntity {

	private static final long serialVersionUID = 8238775575826490450L;

	@NotEmpty
	private String nome = "";

	@Min(value = 1)
	private Integer capienza;

	public Sala() {
		this("", 0);
	}// end of constructor

	public Sala(String nome, int capienza) {
		super();
		this.setNome(nome);
		this.setCapienza(capienza);
	}// end of constructor

	@Override
	public String toString() {
		return nome;
	}// end of method

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the capienza
	 */
	public Integer getCapienza() {
		return capienza;
	}

	/**
	 * @param capienza
	 *            the capienza to set
	 */
	public void setCapienza(Integer capienza) {
		this.capienza = capienza;
	}

	/**
	 * Recupera la sala usando la query specifica
	 * 
	 * @return la sala, null se non trovata
	 */
	public static Sala read(long id) {
		Sala instance = null;
		BaseEntity entity = AQuery.find(Sala.class, id);

		if (entity != null) {
			if (entity instanceof Sala) {
				instance = (Sala) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method


	/**
	 * Recupera la sala di default eventualmente indicata in preferenze
	 * 
	 * @return la sala di default, null se non specificata
	 */
	public static Sala getDefault() {
		Sala sala = null;
		int idSala = CompanyPrefs.idSalaDefault.getInt();

		if (idSala > 0) {
			sala = Sala.read((long) idSala);
		}// end of if cycle

		return sala;
	}// end of method


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Sala sala = (Sala) o;

		if (nome != null ? !nome.equals(sala.nome) : sala.nome != null) return false;
		return !(capienza != null ? !capienza.equals(sala.capienza) : sala.capienza != null);

	}

	@Override
	public int hashCode() {
		int result = nome != null ? nome.hashCode() : 0;
		result = 31 * result + (capienza != null ? capienza.hashCode() : 0);
		return result;
	}
}// end of entity class
