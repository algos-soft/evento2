/**
 * Questo package fornisce le classi necessarie a rappresentare e gestire le preferenze.
 * Le preferenze sono rappresentate dalla classe PrefEventoEntity, che ha una chiave String e memorizza i valori
 * nel database come byte[].
 * Può contenere qualsiasi tipo di dato (anche immagini, o in generale qualsiasi oggetto serializzabile come
 * oggetti Java, pagine web, documenti pdf o altro).
 * <p>
 * Gli elenchi di preferenze vengono rappresentati con delle enum che implementano l'interfaccia PrefIF.
 * Si possono creare quante enum si desiderano (preferenze generali, preferenze dell'azienda,
 * preferenze dell'utente...)
 * Queste enum definiscono le preferenze disponibili e implementano i metodi per leggere e scrivere i valori.
 * Tipicamente definiscono i metodi più comuni (per string, bool, integer, decimal, date, bytes) ma possono
 * implementare metodi specifici per memorizzare qualsiasi tipo di dato serializzabile (per esempio putIndirizzo,
 * getIndirizzo, putPdf, getPdf ecc...).
 * Il supporto per gestire i tipi più comuni è centralizzato nella classe AbsPref.
 */
package it.algos.evento.pref;

