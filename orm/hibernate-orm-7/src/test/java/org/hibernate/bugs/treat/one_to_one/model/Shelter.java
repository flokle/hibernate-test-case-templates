package org.hibernate.bugs.treat.one_to_one.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Shelter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	@JoinColumn
	private AbstractAnimal animal;

	public long getId() {
		return id;
	}

	public AbstractAnimal getAnimal() {
		return animal;
	}

	public void setAnimal(AbstractAnimal animal) {
		this.animal = animal;
	}

}
