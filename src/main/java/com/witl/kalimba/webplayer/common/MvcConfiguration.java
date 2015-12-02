package com.witl.kalimba.webplayer.common;

/*@auther : Ambarish Kumar
 * 
 */

import java.util.Properties;

import javax.sql.DataSource;

import com.witl.kalimba.webplayer.common.UserJDBCTemplate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import com.witl.kalimba.webplayer.ADUserJDBCTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

 
@Configuration
@ComponentScan(basePackages="com.witl.kalimba.webplayer")
@EnableTransactionManagement
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter{
 
	//Conf conf = new Conf();
    @Bean
    public ViewResolver getViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".html");
        return resolver;
    }
     
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
 
    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://108.59.85.14:3306/MELODY");
        dataSource.setUsername("kalimbauser");
        dataSource.setPassword("Mel0di@K@limb@");
         
        return dataSource;
    }
   
    @Bean
    public UserJDBCTemplate getUserJDBCTemplate() {
        return new UserJDBCTemplate(getDataSource());
    }
    
   /* @Bean
    public SonicUserJDBCTemplate getSonicUserJDBCTemplate() {
        return new SonicUserJDBCTemplate(getDataSource());
    }
    */
    /*@Bean
    public ADUserJDBCTemplate getAdUserJDBCTemplate() {
        return new ADUserJDBCTemplate(getDataSource());
    }*/
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
       LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
       sessionFactory.setDataSource(getDataSource());
       sessionFactory.setPackagesToScan(new String[] { "com.witl.kalimba.webplayer" });
       sessionFactory.setHibernateProperties(hibernateProperties());
  
       return sessionFactory;
    }
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(sessionFactory);
  
       return txManager;
    }
  
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
       return new PersistenceExceptionTranslationPostProcessor();
    }
  
    Properties hibernateProperties() {
       return new Properties() {
          {
             setProperty("hibernate.hbm2ddl.auto", "create");
             setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
             setProperty("hibernate.globally_quoted_identifiers", "true");
             setProperty("hibernate.show_sql", "true");
          }
       };
    }
    
    
    
}