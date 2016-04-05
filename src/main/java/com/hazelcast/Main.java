package com.hazelcast;
import java.util.*;

import com.hazelcast.core.Hazelcast;
import org.hibernate.*;
import org.hibernate.stat.Statistics;
import org.hibernate.metadata.ClassMetadata;

public class Main {

    public static void main(String[] args) {

        Hazelcast.newHazelcastInstance();
        initialize();
        Session session = HibernateUtil.currentSession();
        SessionFactory sf = session.getSessionFactory();       
        Statistics stats = sf.getStatistics();
        stats.setStatisticsEnabled(true);        
        session.clear();
        Supplier supplier = (Supplier) session.load(Supplier.class,
                    new Integer(3));
        System.out.println(supplier.getName());
  
        //clear collection and update entity
        issueRelated(session, supplier);

        Supplier supplier2 = (Supplier) session.load(Supplier.class,
                new Integer(3));
        System.out.println("Updated Version : " + supplier2.getProducts().get(0).getVersion());
            
   //     displayStatistics(sf, stats);
       
//        evict2ndLevelCache(sf);

        displayStatistics(sf, stats);

    }

	private static void issueRelated(Session session, Supplier supplier) {
		//supplier.getProducts().clear();
		session.beginTransaction();
		supplier.setName("Supplier10");
        Product product = supplier.getProducts().get(0);
        product.setPrice(123123);
		session.getTransaction().commit();
		
	}

	private static void initialize() {
		HibernateUtil.droptable("drop table Supplier");
        HibernateUtil.droptable("drop table Product");
        HibernateUtil
                .setup("create table Supplier ( id int, name VARCHAR(20), version BIGINT)");
        HibernateUtil
                .setup("create table Product ( id int, name VARCHAR(20), description VARCHAR(40), price double,supplierId int,version BIGINT)");

        prepareTestData();

      
        HibernateUtil.checkData("select * from Supplier");
        HibernateUtil.checkData("select * from Product");
	}

    public static void evict2ndLevelCache(SessionFactory sessionFactory) {
        System.out.println("\n---Clearing Second level cache");
        try {
            Map<String, ClassMetadata> classesMetadata = sessionFactory.getAllClassMetadata();
            for (String entityName : classesMetadata.keySet()) {
                System.out.println("Evicting Entity from 2nd level cache: " + entityName);
                sessionFactory.evictEntity(entityName);
            }
        } catch (Exception e) {
            System.out.println();
        }

    }

    static public void displayStatistics(SessionFactory sf, Statistics stats) {

        System.out.println("\n---Displaying Statistics.......");

        System.out.println("Second level cache hit count "
                + stats.getSecondLevelCacheHitCount());
        System.out.println("Second level cache miss count "
                + stats.getSecondLevelCacheMissCount());
        System.out.println("Second level cache put count "
                + stats.getSecondLevelCachePutCount());

    }

    private static void prepareTestData() {
        Session session = HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();

        Supplier supplier1 = new Supplier();
        supplier1.setName("Supplier Name 1");
        session.save(supplier1);

        Supplier supplier2 = new Supplier();
        supplier2.setName("Supplier Name 2");
        session.save(supplier2);

        Supplier supplier3 = new Supplier();
        supplier3.setName("Supplier Name 3");
        session.save(supplier3);

        Supplier supplier4 = new Supplier();
        supplier4.setName("Supplier Name 4");
        session.save(supplier4);

        Product product1 = new Product("Product1", "Description1", 20.0);
        product1.setSupplier(supplier1);
        supplier1.getProducts().add(product1);
        session.save(product1);

        Product product2 = new Product("Product2", "Description2", 30.0);
        product2.setSupplier(supplier1);
        supplier1.getProducts().add(product2);
        session.save(product2);

        Product product3 = new Product("Product3", "Description3", 22.0);
        product3.setSupplier(supplier2);
        supplier2.getProducts().add(product3);
        session.save(product3);

        Product product4 = new Product("Product4", "Description4", 70.0);
        product4.setSupplier(supplier3);
        supplier3.getProducts().add(product4);
        session.save(product4);

        tx.commit();
        HibernateUtil.closeSession();
    }
}
