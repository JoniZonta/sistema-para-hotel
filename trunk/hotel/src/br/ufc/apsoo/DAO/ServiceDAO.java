package br.ufc.apsoo.DAO;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import br.ufc.apsoo.entidades.Servico;

public class ServiceDAO {
	
	public static boolean SaveService(String name, float value)
	{
		Servico servico = new Servico();
		
		servico.setNome(name);
		servico.setValor(value);

		
		 Session session = null;
		    Transaction tx = null;

		    try {
		      // aqui n�s lemos as configura��es do arquivo hibernate.cfg.xml
		      // e deixamos o Hibernate pronto para trabalhar
		      SessionFactory factory = new
		        Configuration().configure().buildSessionFactory();

		      // abre uma nova sess�o
		      session = factory.openSession();

		      // inicia uma transa��o
		      tx = session.beginTransaction();

		      // vamos criar uma nova inst�ncia da classe Usuario
		      // e definir valores para seus atributos
		      // note que n�o precisamos atribuir valores para
		      // o atributo id
		     

		      session.save(servico); // vamos salvar o usu�rio
		      session.flush();

		      // e salva as altera��es no banco de dados
		      tx.commit();
		    }
		    catch(Exception e) {
		      // houve algum problema? vamos retornar o banco de dados
		      // ao seu estado anterior
		      if(tx != null)
		        tx.rollback();
		      System.out.println("Erro: " + e.getMessage());
		    }
		    finally {
		      session.close();
		    }
		    return true;
		
		
	}
}
