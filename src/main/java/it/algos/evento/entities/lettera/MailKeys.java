package it.algos.evento.entities.lettera;

public enum MailKeys {

    modello("modelloLettera"),
    from("from"),
    destinatario("destinatario"),
    oggetto("oggetto");

    private String key;

    private MailKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
