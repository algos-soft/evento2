package it.algos.evento.entities.insegnante;

import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.entity.DefaultSort;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
@DefaultSort({"cognome","nome"})
public class Insegnante extends CompanyEntity {

	private static final long serialVersionUID = -6289325251948554824L;

	@NotEmpty
	private String cognome;

	@NotEmpty
	private String nome;

	private String titolo;

	@ManyToOne
	private OrdineScuola ordineScuola;

	@Email
	private String email;

	private String telefono;
	private String materie;
	private String indirizzo1;
	private String indirizzo2;

	@Lob
	private String note;

	private boolean privato;


	public Insegnante() {
		this("", "", "", "", "");
	}// end of constructor

	public Insegnante(String cognome, String nome, String titolo, String email, String materie) {
		super();
		setCognome(cognome);
		setNome(nome);
		setTitolo(titolo);
		setEmail(email);
		setMaterie(materie);
	}// end of constructor

	@Override
	public String toString() {
		String string = "";
		string = getCognome() + " " + getNome();
		if (!getMaterie().equals("")) {
			string += " (" + getMaterie() + ")";
		}
		return string;
	}// end of method
	
	
	/**
	 * @return un testo di dettaglio con un riepilogo dei dati dell'insegnante
	 */
	public String getDettaglio(){
		String str="";
		String s;
		
		s=getTitolo();
		if (s!=null && !s.equals("")) {
			str+=s;
		}
		
		s=getCognome() + " " + getNome();
		if (getMaterie()!=null && !getMaterie().equals("")) {
			s += " (" + getMaterie() + ")";
		}
		str += " "+s;
		
		s=getIndirizzo1();
		if (s!=null && !s.equals("")) {
			str+="<br>"+s;
		}
		
		s=getIndirizzo2();
		if (s!=null && !s.equals("")) {
			str+="<br>"+s;
		}
		
		s=getTelefono();
		if (s!=null && !s.equals("")) {
			str+="<br>"+s;
		}

		return str;
	}


	/**
	 * @return un testo di dettaglio con un riepilogo dei dati dell'insegnante da vedere nella Prenotazione
	 */
	public String getDettaglioPren(){
		String str="";
		String s;

		s=getTitolo();
		if (s!=null && !s.equals("")) {
			str+=s;
		}

		s=getCognome() + " " + getNome();
		if (getMaterie()!=null && !getMaterie().equals("")) {
			s += " (" + getMaterie() + ")";
		}
		str += " "+s;

		s=getIndirizzo1();
		if (s!=null && !s.equals("")) {
			str+="<br>" + s;
		}

		s=getIndirizzo2();
		if (s!=null && !s.equals("")) {
			str+=" - "+s;
		}

		return str;
	}



	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public OrdineScuola getOrdineScuola() {
		return ordineScuola;
	}

	public void setOrdineScuola(OrdineScuola ordineScuola) {
		this.ordineScuola = ordineScuola;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMaterie() {
		return materie;
	}

	public void setMaterie(String materie) {
		this.materie = materie;
	}

	public String getIndirizzo1() {
		return indirizzo1;
	}

	public void setIndirizzo1(String indirizzo1) {
		this.indirizzo1 = indirizzo1;
	}

	public String getIndirizzo2() {
		return indirizzo2;
	}

	public void setIndirizzo2(String indirizzo2) {
		this.indirizzo2 = indirizzo2;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


	public boolean isPrivato() {
		return privato;
	}

	public void setPrivato(boolean privato) {
		this.privato = privato;
	}




	public String getCognomeNome(){
		return getCognome()+" "+getNome();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((indirizzo1 == null) ? 0 : indirizzo1.hashCode());
		result = prime * result + ((indirizzo2 == null) ? 0 : indirizzo2.hashCode());
		result = prime * result + ((materie == null) ? 0 : materie.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Insegnante other = (Insegnante) obj;
		if (cognome == null) {
			if (other.cognome != null) {
				return false;
			}
		} else if (!cognome.equals(other.cognome)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (indirizzo1 == null) {
			if (other.indirizzo1 != null) {
				return false;
			}
		} else if (!indirizzo1.equals(other.indirizzo1)) {
			return false;
		}
		if (indirizzo2 == null) {
			if (other.indirizzo2 != null) {
				return false;
			}
		} else if (!indirizzo2.equals(other.indirizzo2)) {
			return false;
		}
		if (materie == null) {
			if (other.materie != null) {
				return false;
			}
		} else if (!materie.equals(other.materie)) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		if (note == null) {
			if (other.note != null) {
				return false;
			}
		} else if (!note.equals(other.note)) {
			return false;
		}
		if (telefono == null) {
			if (other.telefono != null) {
				return false;
			}
		} else if (!telefono.equals(other.telefono)) {
			return false;
		}
		if (titolo == null) {
			if (other.titolo != null) {
				return false;
			}
		} else if (!titolo.equals(other.titolo)) {
			return false;
		}

		return true;
	}


}// end of entity class
