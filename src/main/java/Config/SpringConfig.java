package Config;

import Model.Book;
import Model.Person;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;




@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"Controllers", "Dao"})
public class SpringConfig implements WebMvcConfigurer {



    private final ApplicationContext applicationContext;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    // Бин для SessionFactory
//    @Bean
//    public SessionFactory sessionFactory() {
//        try {
//            // Создание конфигурации Hibernate
//            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration()
//                    .configure("hibernate.cfg.xml")  // Чтение настроек из hibernate.cfg.xml
//                    .addAnnotatedClass(Person.class)  // Добавляем аннотированные классы
//                    .addAnnotatedClass(Book.class);
//
//            // Создание и конфигурирование ServiceRegistry
//            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
//                    .applySettings(configuration.getProperties())  // Получаем свойства конфигурации
//                    .build();
//
//            // Создаем и возвращаем SessionFactory
//            return configuration.buildSessionFactory(serviceRegistry);
//
//        } catch (Throwable ex) {
//            throw new ExceptionInInitializerError("Ошибка при создании SessionFactory: " + ex);
//        }
//    }

    // Бин для TransactionManager
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new HibernateTransactionManager(sessionFactory());
//    }




}
