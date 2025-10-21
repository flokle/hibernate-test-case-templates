package org.hibernate.bugs.treat.one_to_one.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
