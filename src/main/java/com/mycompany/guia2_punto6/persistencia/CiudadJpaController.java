/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.guia2_punto6.persistencia;

import com.mycompany.guia2_punto6.logica.Ciudad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.guia2_punto6.logica.Delegacion;
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
public class CiudadJpaController implements Serializable {

    public CiudadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudad ciudad) throws PreexistingEntityException, Exception {
        if (ciudad.getUnaListaDelegacion() == null) {
            ciudad.setUnaListaDelegacion(new ArrayList<Delegacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Delegacion> attachedUnaListaDelegacion = new ArrayList<Delegacion>();
            for (Delegacion unaListaDelegacionDelegacionToAttach : ciudad.getUnaListaDelegacion()) {
                unaListaDelegacionDelegacionToAttach = em.getReference(unaListaDelegacionDelegacionToAttach.getClass(), unaListaDelegacionDelegacionToAttach.getIdDelegacion());
                attachedUnaListaDelegacion.add(unaListaDelegacionDelegacionToAttach);
            }
            ciudad.setUnaListaDelegacion(attachedUnaListaDelegacion);
            em.persist(ciudad);
            for (Delegacion unaListaDelegacionDelegacion : ciudad.getUnaListaDelegacion()) {
                Ciudad oldUnaCiudadOfUnaListaDelegacionDelegacion = unaListaDelegacionDelegacion.getUnaCiudad();
                unaListaDelegacionDelegacion.setUnaCiudad(ciudad);
                unaListaDelegacionDelegacion = em.merge(unaListaDelegacionDelegacion);
                if (oldUnaCiudadOfUnaListaDelegacionDelegacion != null) {
                    oldUnaCiudadOfUnaListaDelegacionDelegacion.getUnaListaDelegacion().remove(unaListaDelegacionDelegacion);
                    oldUnaCiudadOfUnaListaDelegacionDelegacion = em.merge(oldUnaCiudadOfUnaListaDelegacionDelegacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCiudad(ciudad.getIdCiudad()) != null) {
                throw new PreexistingEntityException("Ciudad " + ciudad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudad ciudad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad persistentCiudad = em.find(Ciudad.class, ciudad.getIdCiudad());
            List<Delegacion> unaListaDelegacionOld = persistentCiudad.getUnaListaDelegacion();
            List<Delegacion> unaListaDelegacionNew = ciudad.getUnaListaDelegacion();
            List<Delegacion> attachedUnaListaDelegacionNew = new ArrayList<Delegacion>();
            for (Delegacion unaListaDelegacionNewDelegacionToAttach : unaListaDelegacionNew) {
                unaListaDelegacionNewDelegacionToAttach = em.getReference(unaListaDelegacionNewDelegacionToAttach.getClass(), unaListaDelegacionNewDelegacionToAttach.getIdDelegacion());
                attachedUnaListaDelegacionNew.add(unaListaDelegacionNewDelegacionToAttach);
            }
            unaListaDelegacionNew = attachedUnaListaDelegacionNew;
            ciudad.setUnaListaDelegacion(unaListaDelegacionNew);
            ciudad = em.merge(ciudad);
            for (Delegacion unaListaDelegacionOldDelegacion : unaListaDelegacionOld) {
                if (!unaListaDelegacionNew.contains(unaListaDelegacionOldDelegacion)) {
                    unaListaDelegacionOldDelegacion.setUnaCiudad(null);
                    unaListaDelegacionOldDelegacion = em.merge(unaListaDelegacionOldDelegacion);
                }
            }
            for (Delegacion unaListaDelegacionNewDelegacion : unaListaDelegacionNew) {
                if (!unaListaDelegacionOld.contains(unaListaDelegacionNewDelegacion)) {
                    Ciudad oldUnaCiudadOfUnaListaDelegacionNewDelegacion = unaListaDelegacionNewDelegacion.getUnaCiudad();
                    unaListaDelegacionNewDelegacion.setUnaCiudad(ciudad);
                    unaListaDelegacionNewDelegacion = em.merge(unaListaDelegacionNewDelegacion);
                    if (oldUnaCiudadOfUnaListaDelegacionNewDelegacion != null && !oldUnaCiudadOfUnaListaDelegacionNewDelegacion.equals(ciudad)) {
                        oldUnaCiudadOfUnaListaDelegacionNewDelegacion.getUnaListaDelegacion().remove(unaListaDelegacionNewDelegacion);
                        oldUnaCiudadOfUnaListaDelegacionNewDelegacion = em.merge(oldUnaCiudadOfUnaListaDelegacionNewDelegacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = ciudad.getIdCiudad();
                if (findCiudad(id) == null) {
                    throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.");
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
            Ciudad ciudad;
            try {
                ciudad = em.getReference(Ciudad.class, id);
                ciudad.getIdCiudad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.", enfe);
            }
            List<Delegacion> unaListaDelegacion = ciudad.getUnaListaDelegacion();
            for (Delegacion unaListaDelegacionDelegacion : unaListaDelegacion) {
                unaListaDelegacionDelegacion.setUnaCiudad(null);
                unaListaDelegacionDelegacion = em.merge(unaListaDelegacionDelegacion);
            }
            em.remove(ciudad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ciudad> findCiudadEntities() {
        return findCiudadEntities(true, -1, -1);
    }

    public List<Ciudad> findCiudadEntities(int maxResults, int firstResult) {
        return findCiudadEntities(false, maxResults, firstResult);
    }

    private List<Ciudad> findCiudadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudad.class));
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

    public Ciudad findCiudad(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudad.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudad> rt = cq.from(Ciudad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
