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
package org.hibernate.bugs.treat.one_to_one;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hibernate.bugs.treat.one_to_one.model.AbstractAnimal;
import org.hibernate.bugs.treat.one_to_one.model.CatAnimal;
import org.hibernate.bugs.treat.one_to_one.model.Shelter;
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
class JoinTreatIgnoredPredicateOnOneToOneORMUnitTestCase {

	@Test
	void oneToOneExpectingNoResultTest(SessionFactoryScope scope) throws Exception {

		scope.inTransaction(session -> {
			
			// Will contain a cat with one life:
			long collectionId = createShelter(session);
			session.flush();

			String stmnt = "SELECT cat FROM Shelter s JOIN TREAT(s.animal as CatAnimal) AS cat ON cat.lives = :lives WHERE s.id = :shelterId";

			TypedQuery<CatAnimal> query = session.createQuery(stmnt, CatAnimal.class);
			query.setParameter("shelterId", collectionId);
			query.setParameter("lives", 9);

			int resultCount = query.getResultList().size();
			assertEquals(0, resultCount, "There shouldn't be a cat in the shelter having 9 lives");

		});
	}

	@Test
	void oneToOneExpectingOneResultTest(SessionFactoryScope scope) throws Exception {

		scope.inTransaction(session -> {
			
			// Will contain a cat with one life:
			long collectionId = createShelter(session);
			session.flush();

			String stmnt = "SELECT cat FROM Shelter s JOIN TREAT(s.animal as CatAnimal) AS cat ON cat.lives = :lives WHERE s.id = :shelterId";

			TypedQuery<CatAnimal> query = session.createQuery(stmnt, CatAnimal.class);
			query.setParameter("shelterId", collectionId);
			query.setParameter("lives", 1);

			int resultCount = query.getResultList().size();
			assertEquals(1, resultCount, "There should be a cat in the shelter having one life");

		});
	}

	private long createShelter(SessionImplementor session) {

		Shelter shelter = new Shelter();

		CatAnimal cat = new CatAnimal();
		cat.setLives(1);
		shelter.setAnimal(cat);

		session.persist(shelter);
		session.persist(cat);

		return shelter.getId();

	}

}
