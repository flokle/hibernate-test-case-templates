/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hibernate.bugs.treat.one_to_many;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hibernate.bugs.treat.one_to_many.model.AbstractAnimal;
import org.hibernate.bugs.treat.one_to_many.model.CatAnimal;
import org.hibernate.bugs.treat.one_to_many.model.Shelter;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.ServiceRegistry;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.Setting;
import org.junit.jupiter.api.Test;

import jakarta.persistence.TypedQuery;

@DomainModel(annotatedClasses = { Shelter.class, AbstractAnimal.class, CatAnimal.class })
@ServiceRegistry(settings = { @Setting(name = AvailableSettings.SHOW_SQL, value = "true"),
		@Setting(name = AvailableSettings.FORMAT_SQL, value = "true") })
@SessionFactory
class JoinTreatIgnoredPredicateOnOneToManyORMUnitTestCase {

	@Test
	void oneToManyExpectingOneResultTest(SessionFactoryScope scope) throws Exception {

		scope.inTransaction(session -> {

			// Will contain a cat with one life and a cat with 9 lives:
			long collectionId = createShelter(session);
			session.flush();

			String stmnt = "SELECT cat FROM Shelter s JOIN TREAT(s.animals as CatAnimal) AS cat ON cat.lives = :lives WHERE s.id = :shelterId";

			TypedQuery<CatAnimal> query = session.createQuery(stmnt, CatAnimal.class);
			query.setParameter("shelterId", collectionId);
			query.setParameter("lives", 9);

			int resultCount = query.getResultList().size();
			assertEquals(1, resultCount, "There should be only one cat in the shelter having 9 lives");

		});
	}

	private long createShelter(SessionImplementor session) {

		Shelter shelter = new Shelter();

		CatAnimal cat1 = new CatAnimal();
		cat1.setLives(1);
		shelter.addToAnimals(cat1);

		CatAnimal cat2 = new CatAnimal();
		cat2.setLives(9);
		shelter.addToAnimals(cat2);

		session.persist(shelter);
		session.persist(cat1);
		session.persist(cat2);

		return shelter.getId();

	}

}
