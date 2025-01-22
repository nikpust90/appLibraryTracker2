package Controllers;

import Dao.BooksDao;
import Dao.PersonDao;
import Model.Book;
import Model.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksDao booksDao;
    private final PersonDao personDao;



    @Autowired
    public BooksController(BooksDao booksDao, PersonDao personDao) {
        this.booksDao = booksDao;
        this.personDao = personDao;
    }



    //получить список книг
    @GetMapping
    public String getAllBooks(Model model) {
        try {
            List<Book> allBooks = booksDao.getAllBooks();
            model.addAttribute("keyAllBooks", allBooks);
            return "books/view-with-all-books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных");
            return "books/error-view";
        }
    }

    //создание книги GET
    @GetMapping("/new")
    public String giveToUserPageToCreateNewBook(Model model) {

        model.addAttribute("keyOfNewBook", new Book());

        return "books/view-to-create-new-book";
    }


    //создание книги POST
    @PostMapping
    public String createBook(@ModelAttribute("keyOfNewBook") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/view-to-create-new-book";
        }
        try {
            booksDao.saveBooks(book);
            return "redirect:/books";
        } catch (Exception e) {
            return "books/error-view";
        }
    }

    //получение книги по id GET
    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") long id, Model model) {

        Book bookById = booksDao.getBookById(id);
        model.addAttribute("keyBookById", bookById);
        //получение всех читателей
        model.addAttribute("people", personDao.getAllPeoples());
        return "books/view-with-book-by-id";
    }

    //редактирование книги по id GET
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") long id, Model model) {
        Book bookToBeEdited = booksDao.getBookById(id);
        model.addAttribute("Book", bookToBeEdited);
//        if (bookToBeEdited.getOwner() != null) {
//            model.addAttribute("keyPeoples", bookToBeEdited.getOwner());
//        }
        return "books/view-to-edit-book";
    }

    //редактирование книги по id POST
    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") long id,
                             @ModelAttribute("keyOfBookToBeEdited") @Valid Book bookFromForm,

                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/view-to-edit-book";
        }
        booksDao.updateBook(bookFromForm);
        return "redirect:/books";
    }

    //удаление книги по id DELETE
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        booksDao.deleteBook(id);
        return "redirect:/books";
    }

    // назначение книги читателю
    @PostMapping("/assign/{id}")
    public String assignBook(@PathVariable("id") long bookId, @RequestParam("personId") long personId) {
        Person person = personDao.getPersonById(personId);
        booksDao.assignBookToPerson(bookId, person);
        return "redirect:/books/" + bookId;
    }

    // удаляем книгу у читателя
    @PostMapping("/loose/{id}")
    public String looseBook(@PathVariable("id") long bookId) {
        //Person person = personDao.getPersonById(personId);
        booksDao.deleteBookToPerson(bookId);
        return "redirect:/books/" + bookId;
    }
}
