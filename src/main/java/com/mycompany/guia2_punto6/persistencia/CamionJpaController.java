/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.guia2_punto6.persistencia;

import com.mycompany.guia2_punto6.logica.Camion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class CamionJpaController implements Serializable {

    public CamionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Camion camion) throws PreexistingEntityException, Exception {
        if (camion.getUnaListaJaula() == null) {
            camion.setUnaListaJaula(new ArrayList<Jaula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Jaula> attachedUnaListaJaula = new ArrayList<Jaula>();
            for (Jaula unaListaJaulaJaulaToAttach : camion.getUnaListaJaula()) {
                unaListaJaulaJaulaToAttach = em.getReference(unaListaJaulaJaulaToAttach.getClass(), unaListaJaulaJaulaToAttach.getIdJaula());
                attachedUnaListaJaula.add(unaListaJaulaJaulaToAttach);
            }
            camion.setUnaListaJaula(attachedUnaListaJaula);
            em.persist(camion);
            for (Jaula unaListaJaulaJaula : camion.getUnaListaJaula()) {
                Camion oldUnCamionOfUnaListaJaulaJaula = unaListaJaulaJaula.getUnCamion();
                unaListaJaulaJaula.setUnCamion(camion);
                unaListaJaulaJaula = em.merge(unaListaJaulaJaula);
                if (oldUnCamionOfUnaListaJaulaJaula != null) {
                    oldUnCamionOfUnaListaJaulaJaula.getUnaListaJaula().remove(unaListaJaulaJaula);
                    oldUnCamionOfUnaListaJaulaJaula = em.merge(oldUnCamionOfUnaListaJaulaJaula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCamion(camion.getMatricula()) != null) {
                throw new PreexistingEntityException("Camion " + camion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Camion camion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Camion persistentCamion = em.find(Camion.class, camion.getMatricula());
            List<Jaula> unaListaJaulaOld = persistentCamion.getUnaListaJaula();
            List<Jaula> unaListaJaulaNew = camion.getUnaListaJaula();
            List<Jaula> attachedUnaListaJaulaNew = new ArrayList<Jaula>();
            for (Jaula unaListaJaulaNewJaulaToAttach : unaListaJaulaNew) {
                unaListaJaulaNewJaulaToAttach = em.getReference(unaListaJaulaNewJaulaToAttach.getClass(), unaListaJaulaNewJaulaToAttach.getIdJaula());
                attachedUnaListaJaulaNew.add(unaListaJaulaNewJaulaToAttach);
            }
            unaListaJaulaNew = attachedUnaListaJaulaNew;
            camion.setUnaListaJaula(unaListaJaulaNew);
            camion = em.merge(camion);
            for (Jaula unaListaJaulaOldJaula : unaListaJaulaOld) {
                if (!unaListaJaulaNew.contains(unaListaJaulaOldJaula)) {
                    unaListaJaulaOldJaula.setUnCamion(null);
                    unaListaJaulaOldJaula = em.merge(unaListaJaulaOldJaula);
                }
            }
            for (Jaula unaListaJaulaNewJaula : unaListaJaulaNew) {
                if (!unaListaJaulaOld.contains(unaListaJaulaNewJaula)) {
                    Camion oldUnCamionOfUnaListaJaulaNewJaula = unaListaJaulaNewJaula.getUnCamion();
                    unaListaJaulaNewJaula.setUnCamion(camion);
                    unaListaJaulaNewJaula = em.merge(unaListaJaulaNewJaula);
                    if (oldUnCamionOfUnaListaJaulaNewJaula != null && !oldUnCamionOfUnaListaJaulaNewJaula.equals(camion)) {
                        oldUnCamionOfUnaListaJaulaNewJaula.getUnaListaJaula().remove(unaListaJaulaNewJaula);
                        oldUnCamionOfUnaListaJaulaNewJaula = em.merge(oldUnCamionOfUnaListaJaulaNewJaula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = camion.getMatricula();
                if (findCamion(id) == null) {
                    throw new NonexistentEntityException("The camion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Camion camion;
            try {
                camion = em.getReference(Camion.class, id);
                camion.getMatricula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The camion with id " + id + " no longer exists.", enfe);
            }
            List<Jaula> unaListaJaula = camion.getUnaListaJaula();
            for (Jaula unaListaJaulaJaula : unaListaJaula) {
                unaListaJaulaJaula.setUnCamion(null);
                unaListaJaulaJaula = em.merge(unaListaJaulaJaula);
            }
            em.remove(camion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Camion> findCamionEntities() {
        return findCamionEntities(true, -1, -1);
    }

    public List<Camion> findCamionEntities(int maxResults, int firstResult) {
        return findCamionEntities(false, maxResults, firstResult);
    }

    private List<Camion> findCamionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Camion.class));
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

    public Camion findCamion(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Camion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCamionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Camion> rt = cq.from(Camion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
