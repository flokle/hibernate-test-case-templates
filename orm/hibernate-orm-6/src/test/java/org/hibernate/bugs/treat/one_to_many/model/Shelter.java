package org.hibernate.bugs.treat.one_to_many.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Shelter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToMany
	@JoinColumn
	private List<AbstractAnimal> animals = new ArrayList<>();

	public long getId() {
		return id;
	}

	public List<AbstractAnimal> getAnimals() {
		return animals;
	}

	public void addToAnimals(AbstractAnimal animal) {
		this.animals.add(animal);
	}

}
