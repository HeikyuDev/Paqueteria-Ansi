/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.guia2_punto6.persistencia;

import com.mycompany.guia2_punto6.logica.MovimientoPaquete;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.guia2_punto6.logica.Paquete;
import com.mycompany.guia2_punto6.persistencia.exceptions.NonexistentEntityException;
import com.mycompany.guia2_punto6.persistencia.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author elnah
 */
public class MovimientoPaqueteJpaController implements Serializable {

    public MovimientoPaqueteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MovimientoPaquete movimientoPaquete) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete unPaquete = movimientoPaquete.getUnPaquete();
            if (unPaquete != null) {
                unPaquete = em.getReference(unPaquete.getClass(), unPaquete.getIdPaquete());
                movimientoPaquete.setUnPaquete(unPaquete);
            }
            em.persist(movimientoPaquete);
            if (unPaquete != null) {
                unPaquete.getUnaListaMovimientos().add(movimientoPaquete);
                unPaquete = em.merge(unPaquete);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimientoPaquete(movimientoPaquete.getIdMovimiento()) != null) {
                throw new PreexistingEntityException("MovimientoPaquete " + movimientoPaquete + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MovimientoPaquete movimientoPaquete) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimientoPaquete persistentMovimientoPaquete = em.find(MovimientoPaquete.class, movimientoPaquete.getIdMovimiento());
            Paquete unPaqueteOld = persistentMovimientoPaquete.getUnPaquete();
            Paquete unPaqueteNew = movimientoPaquete.getUnPaquete();
            if (unPaqueteNew != null) {
                unPaqueteNew = em.getReference(unPaqueteNew.getClass(), unPaqueteNew.getIdPaquete());
                movimientoPaquete.setUnPaquete(unPaqueteNew);
            }
            movimientoPaquete = em.merge(movimientoPaquete);
            if (unPaqueteOld != null && !unPaqueteOld.equals(unPaqueteNew)) {
                unPaqueteOld.getUnaListaMovimientos().remove(movimientoPaquete);
                unPaqueteOld = em.merge(unPaqueteOld);
            }
            if (unPaqueteNew != null && !unPaqueteNew.equals(unPaqueteOld)) {
                unPaqueteNew.getUnaListaMovimientos().add(movimientoPaquete);
                unPaqueteNew = em.merge(unPaqueteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = movimientoPaquete.getIdMovimiento();
                if (findMovimientoPaquete(id) == null) {
                    throw new NonexistentEntityException("The movimientoPaquete with id " + id + " no longer exists.");
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
            MovimientoPaquete movimientoPaquete;
            try {
                movimientoPaquete = em.getReference(MovimientoPaquete.class, id);
                movimientoPaquete.getIdMovimiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimientoPaquete with id " + id + " no longer exists.", enfe);
            }
            Paquete unPaquete = movimientoPaquete.getUnPaquete();
            if (unPaquete != null) {
                unPaquete.getUnaListaMovimientos().remove(movimientoPaquete);
                unPaquete = em.merge(unPaquete);
            }
            em.remove(movimientoPaquete);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MovimientoPaquete> findMovimientoPaqueteEntities() {
        return findMovimientoPaqueteEntities(true, -1, -1);
    }

    public List<MovimientoPaquete> findMovimientoPaqueteEntities(int maxResults, int firstResult) {
        return findMovimientoPaqueteEntities(false, maxResults, firstResult);
    }

    private List<MovimientoPaquete> findMovimientoPaqueteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MovimientoPaquete.class));
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

    public MovimientoPaquete findMovimientoPaquete(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MovimientoPaquete.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimientoPaqueteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MovimientoPaquete> rt = cq.from(MovimientoPaquete.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
