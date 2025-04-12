/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.guia2_punto6.persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.guia2_punto6.logica.Camion;
import com.mycompany.guia2_punto6.logica.Delegacion;
import com.mycompany.guia2_punto6.logica.Jaula;
import com.mycompany.guia2_punto6.logica.Paquete;
import com.mycompany.guia2_punto6.persistencia.exceptions.NonexistentEntityException;
import com.mycompany.guia2_punto6.persistencia.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author elnah
 */
public class JaulaJpaController implements Serializable {

    public JaulaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jaula jaula) throws PreexistingEntityException, Exception {
        if (jaula.getUnaListaPaquetes() == null) {
            jaula.setUnaListaPaquetes(new ArrayList<Paquete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Camion unCamion = jaula.getUnCamion();
            if (unCamion != null) {
                unCamion = em.getReference(unCamion.getClass(), unCamion.getMatricula());
                jaula.setUnCamion(unCamion);
            }
            Delegacion unaDelegacion = jaula.getUnaDelegacion();
            if (unaDelegacion != null) {
                unaDelegacion = em.getReference(unaDelegacion.getClass(), unaDelegacion.getIdDelegacion());
                jaula.setUnaDelegacion(unaDelegacion);
            }
            List<Paquete> attachedUnaListaPaquetes = new ArrayList<Paquete>();
            for (Paquete unaListaPaquetesPaqueteToAttach : jaula.getUnaListaPaquetes()) {
                unaListaPaquetesPaqueteToAttach = em.getReference(unaListaPaquetesPaqueteToAttach.getClass(), unaListaPaquetesPaqueteToAttach.getIdPaquete());
                attachedUnaListaPaquetes.add(unaListaPaquetesPaqueteToAttach);
            }
            jaula.setUnaListaPaquetes(attachedUnaListaPaquetes);
            em.persist(jaula);
            if (unCamion != null) {
                unCamion.getUnaListaJaula().add(jaula);
                unCamion = em.merge(unCamion);
            }
            if (unaDelegacion != null) {
                unaDelegacion.getUnaListaJaula().add(jaula);
                unaDelegacion = em.merge(unaDelegacion);
            }
            for (Paquete unaListaPaquetesPaquete : jaula.getUnaListaPaquetes()) {
                Jaula oldUnaJaulaOfUnaListaPaquetesPaquete = unaListaPaquetesPaquete.getUnaJaula();
                unaListaPaquetesPaquete.setUnaJaula(jaula);
                unaListaPaquetesPaquete = em.merge(unaListaPaquetesPaquete);
                if (oldUnaJaulaOfUnaListaPaquetesPaquete != null) {
                    oldUnaJaulaOfUnaListaPaquetesPaquete.getUnaListaPaquetes().remove(unaListaPaquetesPaquete);
                    oldUnaJaulaOfUnaListaPaquetesPaquete = em.merge(oldUnaJaulaOfUnaListaPaquetesPaquete);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJaula(jaula.getIdJaula()) != null) {
                throw new PreexistingEntityException("Jaula " + jaula + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jaula jaula) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jaula persistentJaula = em.find(Jaula.class, jaula.getIdJaula());
            Camion unCamionOld = persistentJaula.getUnCamion();
            Camion unCamionNew = jaula.getUnCamion();
            Delegacion unaDelegacionOld = persistentJaula.getUnaDelegacion();
            Delegacion unaDelegacionNew = jaula.getUnaDelegacion();
            List<Paquete> unaListaPaquetesOld = persistentJaula.getUnaListaPaquetes();
            List<Paquete> unaListaPaquetesNew = jaula.getUnaListaPaquetes();
            if (unCamionNew != null) {
                unCamionNew = em.getReference(unCamionNew.getClass(), unCamionNew.getMatricula());
                jaula.setUnCamion(unCamionNew);
            }
            if (unaDelegacionNew != null) {
                unaDelegacionNew = em.getReference(unaDelegacionNew.getClass(), unaDelegacionNew.getIdDelegacion());
                jaula.setUnaDelegacion(unaDelegacionNew);
            }
            List<Paquete> attachedUnaListaPaquetesNew = new ArrayList<Paquete>();
            for (Paquete unaListaPaquetesNewPaqueteToAttach : unaListaPaquetesNew) {
                unaListaPaquetesNewPaqueteToAttach = em.getReference(unaListaPaquetesNewPaqueteToAttach.getClass(), unaListaPaquetesNewPaqueteToAttach.getIdPaquete());
                attachedUnaListaPaquetesNew.add(unaListaPaquetesNewPaqueteToAttach);
            }
            unaListaPaquetesNew = attachedUnaListaPaquetesNew;
            jaula.setUnaListaPaquetes(unaListaPaquetesNew);
            jaula = em.merge(jaula);
            if (unCamionOld != null && !unCamionOld.equals(unCamionNew)) {
                unCamionOld.getUnaListaJaula().remove(jaula);
                unCamionOld = em.merge(unCamionOld);
            }
            if (unCamionNew != null && !unCamionNew.equals(unCamionOld)) {
                unCamionNew.getUnaListaJaula().add(jaula);
                unCamionNew = em.merge(unCamionNew);
            }
            if (unaDelegacionOld != null && !unaDelegacionOld.equals(unaDelegacionNew)) {
                unaDelegacionOld.getUnaListaJaula().remove(jaula);
                unaDelegacionOld = em.merge(unaDelegacionOld);
            }
            if (unaDelegacionNew != null && !unaDelegacionNew.equals(unaDelegacionOld)) {
                unaDelegacionNew.getUnaListaJaula().add(jaula);
                unaDelegacionNew = em.merge(unaDelegacionNew);
            }
            for (Paquete unaListaPaquetesOldPaquete : unaListaPaquetesOld) {
                if (!unaListaPaquetesNew.contains(unaListaPaquetesOldPaquete)) {
                    unaListaPaquetesOldPaquete.setUnaJaula(null);
                    unaListaPaquetesOldPaquete = em.merge(unaListaPaquetesOldPaquete);
                }
            }
            for (Paquete unaListaPaquetesNewPaquete : unaListaPaquetesNew) {
                if (!unaListaPaquetesOld.contains(unaListaPaquetesNewPaquete)) {
                    Jaula oldUnaJaulaOfUnaListaPaquetesNewPaquete = unaListaPaquetesNewPaquete.getUnaJaula();
                    unaListaPaquetesNewPaquete.setUnaJaula(jaula);
                    unaListaPaquetesNewPaquete = em.merge(unaListaPaquetesNewPaquete);
                    if (oldUnaJaulaOfUnaListaPaquetesNewPaquete != null && !oldUnaJaulaOfUnaListaPaquetesNewPaquete.equals(jaula)) {
                        oldUnaJaulaOfUnaListaPaquetesNewPaquete.getUnaListaPaquetes().remove(unaListaPaquetesNewPaquete);
                        oldUnaJaulaOfUnaListaPaquetesNewPaquete = em.merge(oldUnaJaulaOfUnaListaPaquetesNewPaquete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = jaula.getIdJaula();
                if (findJaula(id) == null) {
                    throw new NonexistentEntityException("The jaula with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jaula jaula;
            try {
                jaula = em.getReference(Jaula.class, id);
                jaula.getIdJaula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jaula with id " + id + " no longer exists.", enfe);
            }
            Camion unCamion = jaula.getUnCamion();
            if (unCamion != null) {
                unCamion.getUnaListaJaula().remove(jaula);
                unCamion = em.merge(unCamion);
            }
            Delegacion unaDelegacion = jaula.getUnaDelegacion();
            if (unaDelegacion != null) {
                unaDelegacion.getUnaListaJaula().remove(jaula);
                unaDelegacion = em.merge(unaDelegacion);
            }
            List<Paquete> unaListaPaquetes = jaula.getUnaListaPaquetes();
            for (Paquete unaListaPaquetesPaquete : unaListaPaquetes) {
                unaListaPaquetesPaquete.setUnaJaula(null);
                unaListaPaquetesPaquete = em.merge(unaListaPaquetesPaquete);
            }
            em.remove(jaula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jaula> findJaulaEntities() {
        return findJaulaEntities(true, -1, -1);
    }

    public List<Jaula> findJaulaEntities(int maxResults, int firstResult) {
        return findJaulaEntities(false, maxResults, firstResult);
    }

    private List<Jaula> findJaulaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jaula.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Jaula findJaula(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jaula.class, id);
        } finally {
            em.close();
        }
    }

    public int getJaulaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jaula> rt = cq.from(Jaula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
