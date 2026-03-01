package com.roamroute.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "favorites")
public class Favorite {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "selectedpackage_id", nullable = false)
  private SelectedPackage selectedPackage;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public SelectedPackage getSelectedpackage() {
    return selectedPackage;
  }

  public void setSelectedpackage(SelectedPackage selectedPackage) {
    this.selectedPackage = selectedPackage;
  }

}
