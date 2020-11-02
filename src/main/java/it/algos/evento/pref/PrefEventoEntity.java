package it.algos.evento.pref;

import it.algos.webbase.multiazienda.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity per rappresentare una preferenza.
 * Ha una chiave String e memorizza i valori nel database come byte[].
 * Può contenere qualsiasi tipo di dato (anche immagini, o in generale qualsiasi oggetto serializzabile).
 * I valori vengono letti e scritti tramite i metodi delle enum.
 * Si possono creare quante enum si desidera enum (preferenze generali, preferenze dell'azienda,
 * preferenze dell'utente...)
 * La enum contengono i metodi per inserire e recuperare i tipi più comuni
 * (getString, getInt, getDecimal, getResource, getImage...). Il supporto per i tipi comuni è disponibile nella
 * classe AbsPref, ma è anche possibile creare metodi specifici nella enum per memorizzare qualsiasi
 * tipo serializzabile (putIndirizzo, getIndirizzo, putCertificato, getCertificato...)
 */
@Table(name = "COMPANYPREFS")
@Entity
public class PrefEventoEntity extends CompanyEntity {

    private static final long serialVersionUID = -325887743609301921L;

    private String code;
    private byte[] value;

    public PrefEventoEntity() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

}
