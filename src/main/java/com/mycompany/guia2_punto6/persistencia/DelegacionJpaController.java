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
import com.mycompany.guia2_punto6.logica.Ciudad;
import com.mycompany.guia2_punto6.logica.Delegacion;
import com.mycompany.guia2_punto6.logica.Jaula;
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
public class DelegacionJpaController implements Serializable {

    public DelegacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Delegacion delegacion) throws PreexistingEntityException, Exception {
        if (delegacion.getUnaListaJaula() == null) {
            delegacion.setUnaListaJaula(new ArrayList<Jaula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad unaCiudad = delegacion.getUnaCiudad();
            if (unaCiudad != null) {
                unaCiudad = em.getReference(unaCiudad.getClass(), unaCiudad.getIdCiudad());
                delegacion.setUnaCiudad(unaCiudad);
            }
            List<Jaula> attachedUnaListaJaula = new ArrayList<Jaula>();
            for (Jaula unaListaJaulaJaulaToAttach : delegacion.getUnaListaJaula()) {
                unaListaJaulaJaulaToAttach = em.getReference(unaListaJaulaJaulaToAttach.getClass(), unaListaJaulaJaulaToAttach.getIdJaula());
                attachedUnaListaJaula.add(unaListaJaulaJaulaToAttach);
            }
            delegacion.setUnaListaJaula(attachedUnaListaJaula);
            em.persist(delegacion);
            if (unaCiudad != null) {
                unaCiudad.getUnaListaDelegacion().add(delegacion);
                unaCiudad = em.merge(unaCiudad);
            }
            for (Jaula unaListaJaulaJaula : delegacion.getUnaListaJaula()) {
                Delegacion oldUnaDelegacionOfUnaListaJaulaJaula = unaListaJaulaJaula.getUnaDelegacion();
                unaListaJaulaJaula.setUnaDelegacion(delegacion);
                unaListaJaulaJaula = em.merge(unaListaJaulaJaula);
                if (oldUnaDelegacionOfUnaListaJaulaJaula != null) {
                    oldUnaDelegacionOfUnaListaJaulaJaula.getUnaListaJaula().remove(unaListaJaulaJaula);
                    oldUnaDelegacionOfUnaListaJaulaJaula = em.merge(oldUnaDelegacionOfUnaListaJaulaJaula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDelegacion(delegacion.getIdDelegacion()) != null) {
                throw new PreexistingEntityException("Delegacion " + delegacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Delegacion delegacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Delegacion persistentDelegacion = em.find(Delegacion.class, delegacion.getIdDelegacion());
            Ciudad unaCiudadOld = persistentDelegacion.getUnaCiudad();
            Ciudad unaCiudadNew = delegacion.getUnaCiudad();
            List<Jaula> unaListaJaulaOld = persistentDelegacion.getUnaListaJaula();
            List<Jaula> unaListaJaulaNew = delegacion.getUnaListaJaula();
            if (unaCiudadNew != null) {
                unaCiudadNew = em.getReference(unaCiudadNew.getClass(), unaCiudadNew.getIdCiudad());
                delegacion.setUnaCiudad(unaCiudadNew);
            }
            List<Jaula> attachedUnaListaJaulaNew = new ArrayList<Jaula>();
            for (Jaula unaListaJaulaNewJaulaToAttach : unaListaJaulaNew) {
                unaListaJaulaNewJaulaToAttach = em.getReference(unaListaJaulaNewJaulaToAttach.getClass(), unaListaJaulaNewJaulaToAttach.getIdJaula());
                attachedUnaListaJaulaNew.add(unaListaJaulaNewJaulaToAttach);
            }
            unaListaJaulaNew = attachedUnaListaJaulaNew;
            delegacion.setUnaListaJaula(unaListaJaulaNew);
            delegacion = em.merge(delegacion);
            if (unaCiudadOld != null && !unaCiudadOld.equals(unaCiudadNew)) {
                unaCiudadOld.getUnaListaDelegacion().remove(delegacion);
                unaCiudadOld = em.merge(unaCiudadOld);
            }
            if (unaCiudadNew != null && !unaCiudadNew.equals(unaCiudadOld)) {
                unaCiudadNew.getUnaListaDelegacion().add(delegacion);
                unaCiudadNew = em.merge(unaCiudadNew);
            }
            for (Jaula unaListaJaulaOldJaula : unaListaJaulaOld) {
                if (!unaListaJaulaNew.contains(unaListaJaulaOldJaula)) {
                    unaListaJaulaOldJaula.setUnaDelegacion(null);
                    unaListaJaulaOldJaula = em.merge(unaListaJaulaOldJaula);
                }
            }
            for (Jaula unaListaJaulaNewJaula : unaListaJaulaNew) {
                if (!unaListaJaulaOld.contains(unaListaJaulaNewJaula)) {
                    Delegacion oldUnaDelegacionOfUnaListaJaulaNewJaula = unaListaJaulaNewJaula.getUnaDelegacion();
                    unaListaJaulaNewJaula.setUnaDelegacion(delegacion);
                    unaListaJaulaNewJaula = em.merge(unaListaJaulaNewJaula);
                    if (oldUnaDelegacionOfUnaListaJaulaNewJaula != null && !oldUnaDelegacionOfUnaListaJaulaNewJaula.equals(delegacion)) {
                        oldUnaDelegacionOfUnaListaJaulaNewJaula.getUnaListaJaula().remove(unaListaJaulaNewJaula);
                        oldUnaDelegacionOfUnaListaJaulaNewJaula = em.merge(oldUnaDelegacionOfUnaListaJaulaNewJaula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = delegacion.getIdDelegacion();
                if (findDelegacion(id) == null) {
                    throw new NonexistentEntityException("The delegacion with id " + id + " no longer exists.");
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
            Delegacion delegacion;
            try {
                delegacion = em.getReference(Delegacion.class, id);
                delegacion.getIdDelegacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The delegacion with id " + id + " no longer exists.", enfe);
            }
            Ciudad unaCiudad = delegacion.getUnaCiudad();
            if (unaCiudad != null) {
                unaCiudad.getUnaListaDelegacion().remove(delegacion);
                unaCiudad = em.merge(unaCiudad);
            }
            List<Jaula> unaListaJaula = delegacion.getUnaListaJaula();
            for (Jaula unaListaJaulaJaula : unaListaJaula) {
                unaListaJaulaJaula.setUnaDelegacion(null);
                unaListaJaulaJaula = em.merge(unaListaJaulaJaula);
            }
            em.remove(delegacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Delegacion> findDelegacionEntities() {
        return findDelegacionEntities(true, -1, -1);
    }

    public List<Delegacion> findDelegacionEntities(int maxResults, int firstResult) {
        return findDelegacionEntities(false, maxResults, firstResult);
    }

    private List<Delegacion> findDelegacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Delegacion.class));
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

    public Delegacion findDelegacion(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Delegacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDelegacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Delegacion> rt = cq.from(Delegacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
