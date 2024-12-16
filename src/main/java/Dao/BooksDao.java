package Dao;


import Config.HibernateUtil;
import Model.Book;
import Model.Person;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Session;


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


@Getter
@Repository
public class BooksDao {
    private static final Logger logger = LoggerFactory.getLogger(BooksDao.class);

    //получение всех книг
    public List<Book> getAllBooks() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Query<Book> query = (Query<Book>) session.createQuery("from Book");
            List<Book> allBooks = query.list();
            return allBooks;
        } catch (HibernateException e) {
            logger.error("Ошибка при получении списка всех книг", e);
            throw new RuntimeException(e);
        }
    }

    //создание книги
    public void saveBooks (@Valid Book book){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.persist(book); // Сохраняем объект
            session.getTransaction().commit(); // Фиксируем изменения
        } catch (Exception e) {
            logger.error("Ошибка при сохранении объекта book", e);
            throw new RuntimeException("Ошибка при сохранении объекта book", e);
        }
    }

    //получение объекта book по id
    public Book getBookById(UUID id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Book book = session.get(Book.class, id);
            return book;
        } catch (Exception e) {
            logger.error("Ошибка при получении объекта book", e);
            throw new RuntimeException("Ошибка при получении объекта book", e);
        }
    }

    //обновление объекта book
    public void updateBook(@Valid Book bookFromForm) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(bookFromForm);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Ошибка при обновлении объекта book", e);
            throw new RuntimeException("Ошибка при обновлении объекта book", e);
        }
    }

    //удаление объекта book
    public void deleteBook(UUID id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Book book = session.get(Book.class, id);
            session.delete(book);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Ошибка при удалении объекта book", e);
            throw new RuntimeException("Ошибка при удалении объекта book", e);
        }
    }

    // назначение книги читателю
    public void assignBookToPerson(UUID bookId, Person person) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Book book = session.get(Book.class, bookId);
            if (book == null) {
                throw new IllegalArgumentException("Книга с ID " + bookId + " не найдена");
            }
            book.setOwner(person);
            session.update(book);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Ошибка при назначении книги", e);
            throw new RuntimeException("Ошибка при назначении книги", e);
        }
    }
}
