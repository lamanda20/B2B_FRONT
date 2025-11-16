package com.example.b2bfront.model;

// 1. AJOUT DE L'IMPORT NÉCESSAIRE
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

// 2. AJOUT DE L'ANNOTATION (ignore les champs inconnus comme "commande")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Livraison {

    private Long idLivraison;
    private String adresse;
    private String ville;
    private String codePostal;
    private String telephone;
    private String transporteur;
    private double fraisLivraison;
    private LocalDate dateEnvoi;
    private LocalDate dateEstimee;

    // Constructeur vide requis pour la désérialisation JSON
    public Livraison() {
    }

    // Getters et Setters (essentiels pour Jackson)
    public Long getIdLivraison() { return idLivraison; }
    public void setIdLivraison(Long idLivraison) { this.idLivraison = idLivraison; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getTransporteur() { return transporteur; }
    public void setTransporteur(String transporteur) { this.transporteur = transporteur; }

    public double getFraisLivraison() { return fraisLivraison; }
    public void setFraisLivraison(double fraisLivraison) { this.fraisLivraison = fraisLivraison; }

    public LocalDate getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDate dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public LocalDate getDateEstimee() { return dateEstimee; }
    public void setDateEstimee(LocalDate dateEstimee) { this.dateEstimee = dateEstimee; }
}