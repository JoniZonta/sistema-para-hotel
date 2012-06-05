package br.ufc.apsoo.DAO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;

import br.ufc.apsoo.DTO.EvolucaoDTO;
import br.ufc.apsoo.DTO.ServicoDTO;
import br.ufc.apsoo.entidades.Servico;

public class ServiceDAO {

	public static boolean SaveService(String name, float value) {
		Servico servico = new Servico();

		servico.setNome(name);
		servico.setValor(value);

		Session session = null;
		Transaction tx = null;

		try {
			// aqui n�s lemos as configura��es do arquivo hibernate.cfg.xml
			// e deixamos o Hibernate pronto para trabalhar
			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();

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
		} catch (Exception e) {
			// houve algum problema? vamos retornar o banco de dados
			// ao seu estado anterior
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
		return true;

	}

	public static ArrayList<Servico> ListServices() {
		ArrayList<Servico> listServicos = new ArrayList();

		Session session = null;
		Transaction tx = null;

		try {

			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			session = factory.openSession();

			Query query = session.createQuery("from Servico order by id");
			listServicos = (ArrayList<Servico>) query.list();

		} catch (Exception e) {
			// houve algum problema? vamos retornar o banco de dados
			// ao seu estado anterior
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
		return listServicos;
	}
	
	public static boolean EditService(long id, String newName, float newValue)
	{
		Servico servico = new Servico();
		servico.setId(id);
	
		 Session session = null;
		    Transaction tx = null;

		    try {
		      SessionFactory factory = new
		        Configuration().configure().buildSessionFactory();
		      session = factory.openSession();
		      tx = session.beginTransaction();

		      Query query = session.createQuery("from Servico where id="+ id);
				servico = (Servico) query.list().get(0);
		      
				servico.setNome(newName);
				servico.setValor(newValue);
				
				session.update(servico);
				
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
	
	
	public static Servico getServiceByNameAndValue(String name, float value) {
		Servico servico = new Servico();

		Session session = null;
		Transaction tx = null;

		try {

			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			session = factory.openSession();
			tx = session.beginTransaction();

			Query query = session.createQuery("from Servico where nome='" + name + "' and valor=" + value);
			servico = (Servico) query.list().get(0);

		} catch (Exception e) {
			// houve algum problema? vamos retornar o banco de dados
			// ao seu estado anterior
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
		return servico;
	}
	
	public static Servico getServiceById(long id) {
		Servico servico = new Servico();

		Session session = null;
		Transaction tx = null;

		try {

			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			session = factory.openSession();
			tx = session.beginTransaction();

			Query query = session.createQuery("from Servico where id=" + id);
			servico = (Servico) query.list().get(0);

		} catch (Exception e) {
			// houve algum problema? vamos retornar o banco de dados
			// ao seu estado anterior
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
		return servico;
	}
	
	
	public static boolean deleteService(long id) {
		Servico servico = new Servico();
		
		servico = getServiceById(id);
		
		Session session = null;
		Transaction tx = null;

		try {
				SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			session = factory.openSession();
			tx = session.beginTransaction();

			session.delete(servico);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
		return true;

	}
	
	@SuppressWarnings("unchecked")
	public static List<BigInteger> getServicesByIdConta(long id) {
		List<BigInteger> servicos = null;
		Session session = null;
		Transaction tx = null;

		try {

			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			session = factory.openSession();
			tx = session.beginTransaction();

			Query query = session.createSQLQuery("select servicos_id from conta_servico where conta_id=" + id);
			servicos = query.list();

		} catch (Exception e) {
			// houve algum problema? vamos retornar o banco de dados
			// ao seu estado anterior
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
		return servicos;
	}
	
	@SuppressWarnings("unchecked")
	public static List<ServicoDTO> getServicosUltimoMes() {
		List<ServicoDTO> servicoDTO = null;
		Session session = null;
		Transaction tx = null;

		try {

			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			session = factory.openSession();
			tx = session.beginTransaction();

			Query query = session.createSQLQuery("SELECT servico.nome as nome, sum(servico.valor) as valor " +
					"FROM conta, conta_servico, servico " +
					"WHERE EXTRACT(MONTH FROM CURRENT_DATE - INTERVAL '1 MONTH') = EXTRACT (MONTH FROM conta.datafim) " +
					"AND conta.id = conta_servico.conta_id AND servico.id = conta_servico.servicos_id group by servico.nome " +
					"order by valor desc");
			servicoDTO = query.setResultTransformer(Transformers.aliasToBean(ServicoDTO.class)).list();

		} catch (Exception e) {
			// houve algum problema? vamos retornar o banco de dados
			// ao seu estado anterior
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
		return servicoDTO;
	}
	
	@SuppressWarnings("unchecked")
	public static List<EvolucaoDTO> getEvolucao() {
		List<EvolucaoDTO> servicoDTO = null;
		Session session = null;
		Transaction tx = null;

		try {

			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			session = factory.openSession();
			tx = session.beginTransaction();

			Query query = session.createSQLQuery(
					" SELECT EXTRACT (MONTH FROM conta.datafim) mes, (td.total + ts.total ) total " +
					" FROM " +
					"		(SELECT EXTRACT (MONTH FROM conta.datafim) mes, sum(extract(day from (datafim - datainicio)) * tipo.valor) as total" +
					"		 FROM conta, conta_apartamento, apartamento, tipo " +
					"		 WHERE conta.id = conta_apartamento.conta_id and apartamentos_id = apartamento.id and apartamento.tipo_id = tipo.id " +
					"			   and EXTRACT (MONTH FROM conta.datafim) between EXTRACT(MONTH FROM CURRENT_DATE - INTERVAL '4 MONTH') and " +
					"			   EXTRACT(MONTH FROM CURRENT_DATE - INTERVAL '1 MONTH') group by  EXTRACT (MONTH FROM conta.datafim) " +
					"		       order by EXTRACT (MONTH FROM conta.datafim)) td, " +
					"		(SELECT EXTRACT (MONTH FROM conta.datafim) mes ,sum(servico.valor) as total " +
					"		 FROM conta, conta_servico, servico " +
					"		 WHERE EXTRACT (MONTH FROM conta.datafim) between EXTRACT(MONTH FROM CURRENT_DATE - INTERVAL '4 MONTH') and " +
					"			   EXTRACT(MONTH FROM CURRENT_DATE - INTERVAL '1 MONTH') AND conta.id = conta_servico.conta_id AND servico.id = conta_servico.servicos_id" +
					"		       group by  EXTRACT (MONTH FROM conta.datafim)	order by EXTRACT (MONTH FROM conta.datafim)) ts, conta " +
					" WHERE EXTRACT (MONTH FROM conta.datafim) = td.mes AND EXTRACT (MONTH FROM conta.datafim) = ts.mes " +
					" ORDER BY EXTRACT (YEAR FROM conta.datafim) , EXTRACT (MONTH FROM conta.datafim)asc");
			servicoDTO = query.setResultTransformer(Transformers.aliasToBean(EvolucaoDTO.class)).list();

		} catch (Exception e) {
			// houve algum problema? vamos retornar o banco de dados
			// ao seu estado anterior
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
		return servicoDTO;
	}
}
