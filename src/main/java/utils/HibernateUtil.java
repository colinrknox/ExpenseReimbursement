package utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static SessionFactory sf;
	
	static {
		Configuration config = new Configuration();
		config.setProperty("hibernate.connection.url", "jdbc:postgresql://" + System.getenv("AWS_DB_ENDPOINT") + "/project-one");
		config.setProperty("hibernate.connection.username", System.getenv("AWS_USERNAME"));
		config.setProperty("hibernate.connection.password", System.getenv("AWS_PASSWORD"));
		sf = config.buildSessionFactory();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Closing sessionfactory");
				sf.close();
			}
		});
	}
	
	public static Session getSession() {
		return sf.openSession();
	}
}
