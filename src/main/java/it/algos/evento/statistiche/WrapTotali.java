package it.algos.evento.statistiche;

import it.algos.evento.entities.prenotazione.Prenotazione;

import java.math.BigDecimal;

public class WrapTotali {

	private int totInteri;
	private int totRidotti;
	private int totDisabili;
	private int totAccomp;
	private int totCapienza;
	private int totScuole;
	private int totRappresentazioni;
	private BigDecimal totPagare;
	private BigDecimal totPagato;

	public WrapTotali() {
	}// end of constructor

	public WrapTotali(int totInteri, int totRidotti, int totDisabili, int totAccomp, int totCapienza, int totScuole, BigDecimal totPagare,
			BigDecimal totPagato) {
		super();
		this.totInteri = totInteri;
		this.totRidotti = totRidotti;
		this.totDisabili = totDisabili;
		this.totAccomp = totAccomp;
		this.totCapienza = totCapienza;
		this.totScuole = totScuole;
		this.totPagare = totPagare;
		this.totPagato = totPagato;
	}// end of constructor

	public WrapTotali(Prenotazione pren) {
		totInteri = pren.getNumInteri();
		totRidotti = pren.getNumRidotti();
		totDisabili = pren.getNumDisabili();
		totAccomp = pren.getNumAccomp();
		totPagare = pren.getImportoDaPagare();
		totPagato = pren.getImportoPagato();
	}// end of constructor

	public void addPren(Prenotazione pren) {
		BigDecimal importo;

		totInteri += pren.getNumInteri();
		totRidotti += pren.getNumRidotti();
		totDisabili += pren.getNumDisabili();
		totAccomp += pren.getNumAccomp();

		importo = pren.getImportoDaPagare();
		if (importo != null) {
			if (totPagare == null) {
				totPagare = importo;
			} else {
				totPagare = totPagare.add(importo);
			}// end of if/else cycle
		}// end of if cycle
		importo = pren.getImportoPagato();
		if (importo != null) {
			if (totPagato == null) {
				totPagato = importo;
			} else {
				totPagato = totPagato.add(importo);
			}// end of if/else cycle
		}// end of if cycle

	}// end of method

	public void addWrap(WrapTotali wrap) {
		BigDecimal importo;

		totInteri += wrap.totInteri;
		totRidotti += wrap.totRidotti;
		totDisabili += wrap.totDisabili;
		totAccomp += wrap.totAccomp;
		totCapienza += wrap.totCapienza;

		importo = wrap.totPagare;
		if (importo != null) {
			if (totPagare == null) {
				totPagare = importo;
			} else {
				totPagare = totPagare.add(importo);
			}// end of if/else cycle
		}// end of if cycle
		importo = wrap.totPagato;
		if (importo != null) {
			if (totPagato == null) {
				totPagato = importo;
			} else {
				totPagato = totPagato.add(importo);
			}// end of if/else cycle
		}// end of if cycle

	}// end of method

	public int getTotInteri() {
		return totInteri;
	}

	public int getTotRidotti() {
		return totRidotti;
	}

	public int getTotDisabili() {
		return totDisabili;
	}
	
	public int getTotAccomp() {
		return totAccomp;
	}

	public int getTotSpettatori(){
		return getTotInteri()+getTotRidotti()+getTotDisabili()+getTotAccomp();
	}
	
	public int getTotCapienza(){
		return totCapienza;
	}

	public int getTotScuole() {
		return totScuole;
	}
	
	public int getTotRappresentazioni() {
		return totRappresentazioni;
	}

	public BigDecimal getTotPagare() {
		return totPagare;
	}

	public BigDecimal getTotPagato() {
		return totPagato;
	}

	public double getTotPagareDouble() {
		if (totPagare != null) {
			return totPagare.doubleValue();
		} else {
			return 0;
		}// end of if/else cycle
	}// end of method

	public double getTotPagatoDoble() {
		if (totPagato != null) {
			return totPagato.doubleValue();
		} else {
			return 0;
		}// end of if/else cycle
	}// end of method

	public void setTotInteri(int num) {
		this.totInteri = num;
	}

	public void setTotRidotti(int num) {
		this.totRidotti = num;
	}

	public void setTotDisabili(int num) {
		this.totDisabili = num;
	}

	public void setTotAccomp(int totAccomp) {
		this.totAccomp = totAccomp;
	}

	public void setTotCapienza(int totCapienza) {
		this.totCapienza = totCapienza;
	}

	public void setTotScuole(int totScuole) {
		this.totScuole = totScuole;
	}

	public void setTotRappresentazioni(int totRappresentazioni) {
		this.totRappresentazioni = totRappresentazioni;
	}

	public void setTotPagare(BigDecimal totPagare) {
		this.totPagare = totPagare;
	}

	public void setTotPagato(BigDecimal totPagato) {
		this.totPagato = totPagato;
	}
	
	

}// end of class
