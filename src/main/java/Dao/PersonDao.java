package Dao;

import Config.HibernateUtil;
import Model.Person;
import jakarta.validation.Valid;
import lombok.Getter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Getter
@Repository
public class PersonDao {

    private static final Logger logger = LoggerFactory.getLogger(PersonDao.class);

    //    public PersonDao() {
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//                .configure("hibernate.cfg.xml")
//                .build();
//
//        try {
//            sessionFactory = new MetadataSources(registry)
//                    .addAnnotatedClass(Person.class)
//                    .addAnnotatedClass(Book.class)
//                    .buildMetadata()
//                    .buildSessionFactory();
//        } catch (Exception e) {
//            StandardServiceRegistryBuilder.destroy(registry);
//            e.printStackTrace();
//            throw new RuntimeException("Ошибка инициализации SessionFactory", e);
//        }
//    }



    //получение всех людей
    public List<Person> getAllPeoples() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Query<Person> query = (Query<Person>) session.createQuery("from Person");
            List<Person> allPeople = query.list();
            return allPeople;
        } catch (HibernateException e) {
            logger.error("Ошибка при получении списка всех людей", e);
            throw new RuntimeException(e);
        }
    }

    //создание человека
    public void savePerson (@Valid Person person){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.persist(person); // Сохраняем объект
            session.getTransaction().commit(); // Фиксируем изменения
        } catch (Exception e) {
            logger.error("Ошибка при сохранении объекта Person", e);
            throw new RuntimeException("Ошибка при сохранении объекта Person", e);
        }
    }

    //получение человека по id
    public Person getPersonById(UUID id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Person person = session.get(Person.class, id);
            return person;
        } catch (Exception e) {
            logger.error("Ошибка при получении объекта Person", e);
            throw new RuntimeException("Ошибка при получении объекта Person", e);
        }
    }

    //обновление человека
    public void updatePerson(@Valid Person personFromForm) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(personFromForm);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Ошибка при обновлении объекта Person", e);
            throw new RuntimeException("Ошибка при обновлении объекта Person", e);
        }
    }

    //удаление человека
    public void deletePerson(UUID id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Person person = session.get(Person.class, id);
            session.delete(person);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Ошибка при удалении объекта Person", e);
            throw new RuntimeException("Ошибка при удалении объекта Person", e);
        }
    }
}
