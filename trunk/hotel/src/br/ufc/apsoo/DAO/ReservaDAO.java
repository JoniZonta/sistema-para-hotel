package br.ufc.apsoo.DAO;

import java.util.Date;
import java.util.Calendar;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import br.ufc.apsoo.entidades.Apartamento;
import br.ufc.apsoo.entidades.Hospede;
import br.ufc.apsoo.entidades.Reserva;

public class ReservaDAO {
	public static void reservar(Hospede hospede, Apartamento apartamento){		

		Session session = null;
		Transaction tx = null;

		try {
			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			session = factory.openSession();
			
			tx=session.beginTransaction();
			Query query = session.createQuery("from Hospede where cpf="+hospede.getCpf());
			Hospede h = (Hospede) query.list().get(0);
			Reserva reserva = new Reserva();
			reserva.setHospede(hospede);
			reserva.setApartamento(apartamento);
			session.save(reserva);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
	}
	
	public static void reservar(Hospede hospede, Apartamento apartamento, Date dt_inicio, Date dt_fim)
	{
		Session session = null;
		Transaction tx = null;

		try {
			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			session = factory.openSession();
			
			tx=session.beginTransaction();
			Reserva reserva = new Reserva();
			reserva.setHospede(hospede);
			reserva.setApartamento(apartamento);
			reserva.setDataInicio(dt_inicio);
			reserva.setDataFim(dt_fim);
			session.save(reserva);
			
			/*Depois de salvar a RESERVA tem que mudar a situa��o do apartamento para n�o dispon�vel*/
			//Apartamento apBd = ApartamentoDAO.buscarApartamento(apartamento.getId());
			//apBd.setDisponivel(false);
			session.update(apartamento);
			tx.commit();
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.out.println("Erro: " + e.getMessage());
		} finally {
			session.close();
		}
		
	}
}
