package Controllers;

import Dao.PersonDao;
import Model.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDao personDao;

    @Autowired
    public PeopleController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping
    public String getAllPeople(Model model) {
        try {
            List<Person> allPeoples = personDao.getAllPeoples();
            model.addAttribute("keyAllPeoples", allPeoples);
            return "view-with-all-people";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных");
            return "error-view";
        }
    }

    @PostMapping
    public String createPerson(@ModelAttribute("keyOfNewPerson") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "view-to-create-new-person";
        }
        try {
            personDao.savePerson(person);
            return "redirect:/people";
        } catch (Exception e) {
            return "error-view";
        }
    }
}
