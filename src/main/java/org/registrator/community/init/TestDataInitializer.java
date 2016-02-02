package org.registrator.community.init;

import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.registrator.community.entity.Inquiry;
import org.registrator.community.entity.Resource;
import org.registrator.community.entity.ResourceType;
import org.registrator.community.entity.Role;
import org.registrator.community.entity.TerritorialCommunity;
import org.registrator.community.entity.Tome;
import org.registrator.community.entity.User;
import org.registrator.community.enumeration.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitializer {
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	public void init(){
		
		SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		Session session = sessionFactory.openSession();
		
		Transaction territorialCommunity = session.beginTransaction();
        TerritorialCommunity globalTerritorialCommunity = new TerritorialCommunity();
        globalTerritorialCommunity.setName("Україна");
        session.persist(globalTerritorialCommunity);
        territorialCommunity.commit();
		
		
		
        Transaction roleTransaction = session.beginTransaction();
        
        Role roleUser = new Role(RoleType.USER,"description");
        session.persist(roleUser);
        
        Role roleAdmin = new Role(RoleType.ADMIN,"description");
        session.persist(roleAdmin);
        
        Role roleRegistrator = new Role(RoleType.REGISTRATOR,"description");
        session.persist(roleRegistrator);
        
        Role roleCommissioner = new Role(RoleType.COMMISSIONER,"description");
        session.persist(roleCommissioner);
        
        roleTransaction.commit();
        Transaction userTransaction = session.beginTransaction();
        
        User user = new User("user","$2a$10$Wcuw6mLD18wVT5diGYncJeVyL8J1bTSIly2IbLUX2bJ.UWZPC.qS.",
        		roleUser,"Іван","Головатий","Сергійович","ivan@gmail.com","UNBLOCK");
        user.setDateOfAccession(new Date());
        user.setTerritorialCommunity(globalTerritorialCommunity);
        
        session.persist(user);
        
        User admin = new User("admin","$2a$10$tkROwYPOXyBmKjarHW1rbuOOez2Z5gfkFCbUXUbOv1OY2wgekbZNC",
        		roleAdmin,"Сергій","Головатий","Сергійович","sergey@gmail.com","UNBLOCK");
        user.setDateOfAccession(new Date());
        admin.setTerritorialCommunity(globalTerritorialCommunity);
        session.persist(admin);
        
        User registrator = new User("registrator","$2a$10$KJdq1wmP3MctLh.lEdAuseUCnSRdhJo8S7qwaZHFEUoGhfjOsOnrm",
        		roleRegistrator,"Євген","Михалкевич","Сергійович","evgen@gmail.com","UNBLOCK");
        user.setDateOfAccession(new Date());
        registrator.setTerritorialCommunity(globalTerritorialCommunity);
        session.persist(registrator);
        
        
        userTransaction.commit();
        Transaction resourceTypeTransaction = session.beginTransaction();
        
        ResourceType land = new ResourceType("земельний");
        session.persist(land);
        
        resourceTypeTransaction.commit();
        Transaction tomeTransaction = session.beginTransaction();
        
        Tome tome = new Tome(registrator, "12345");
        session.persist(tome);
        
        tomeTransaction.commit();
        Transaction resourceTransaction = session.beginTransaction();
        
        Resource resource = new Resource(land, "111111", "ліс", registrator, new Date(), "active", tome, "підстава на внесення");
        session.persist(resource);
        
        resourceTransaction.commit();
        Transaction inquiryTransaction = session.beginTransaction();
        
        Inquiry inquiry = new Inquiry("OUTPUT", new Date(), user, registrator, resource);
        session.persist(inquiry);
        
        inquiryTransaction.commit();
	}

}
