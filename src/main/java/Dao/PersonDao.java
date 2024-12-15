package Dao;

import Model.Book;
import Model.Person;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Repository
public class PersonDao {

    private static final Logger logger = LoggerFactory.getLogger(PersonDao.class);
    //private static final SessionFactory sessionFactory;
    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    //private static SessionFactory sessionFactory;

//    @Autowired
//    public void setSessionFactory(SessionFactory sessionFactory) {
//        PersonDao.sessionFactory = sessionFactory;
//    }
//    static {
//        try {Configuration configuration = new Configuration()
//                    .configure("hibernate.cfg.xml")
//                    .addAnnotatedClass(Person.class)
//                    .addAnnotatedClass(Book.class);
//
//            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
//                    .applySettings(configuration.getProperties())
//                    .build();
//
//            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//        } catch (Throwable ex) {
//            logger.error("Ошибка при создании sessionFactory object.", ex);
//            throw new ExceptionInInitializerError("Ошибка при создании sessionFactory object." + ex);
//        }
//    }

    //получение всех людей
    public List<Person> getAllPeoples() {


        try {
            Session session = sessionFactory.openSession();

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
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(person); // Сохраняем объект
            session.getTransaction().commit(); // Фиксируем изменения


        } catch (Exception e) {
            logger.error("Ошибка при сохранении объекта Person", e);
            throw new RuntimeException("Ошибка при сохранении объекта Person", e);

        }
    }
}
