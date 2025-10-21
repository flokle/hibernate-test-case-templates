package org.hibernate.bugs.treat.one_to_many.model;

import javax.persistence.Entity;

@Entity
public class CatAnimal extends AbstractAnimal {

	private int lives;

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

}
