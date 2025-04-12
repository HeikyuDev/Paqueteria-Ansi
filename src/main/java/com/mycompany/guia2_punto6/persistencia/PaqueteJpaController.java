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
import com.mycompany.guia2_punto6.logica.Envio;
import com.mycompany.guia2_punto6.logica.Jaula;
import com.mycompany.guia2_punto6.logica.MovimientoPaquete;
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
public class PaqueteJpaController implements Serializable {

    public PaqueteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paquete paquete) throws PreexistingEntityException, Exception {
        if (paquete.getUnaListaMovimientos() == null) {
            paquete.setUnaListaMovimientos(new ArrayList<MovimientoPaquete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Envio unEnvio = paquete.getUnEnvio();
            if (unEnvio != null) {
                unEnvio = em.getReference(unEnvio.getClass(), unEnvio.getIdEnvio());
                paquete.setUnEnvio(unEnvio);
            }
            Jaula unaJaula = paquete.getUnaJaula();
            if (unaJaula != null) {
                unaJaula = em.getReference(unaJaula.getClass(), unaJaula.getIdJaula());
                paquete.setUnaJaula(unaJaula);
            }
            List<MovimientoPaquete> attachedUnaListaMovimientos = new ArrayList<MovimientoPaquete>();
            for (MovimientoPaquete unaListaMovimientosMovimientoPaqueteToAttach : paquete.getUnaListaMovimientos()) {
                unaListaMovimientosMovimientoPaqueteToAttach = em.getReference(unaListaMovimientosMovimientoPaqueteToAttach.getClass(), unaListaMovimientosMovimientoPaqueteToAttach.getIdMovimiento());
                attachedUnaListaMovimientos.add(unaListaMovimientosMovimientoPaqueteToAttach);
            }
            paquete.setUnaListaMovimientos(attachedUnaListaMovimientos);
            em.persist(paquete);
            if (unEnvio != null) {
                unEnvio.getUnaListaPaquetes().add(paquete);
                unEnvio = em.merge(unEnvio);
            }
            if (unaJaula != null) {
                unaJaula.getUnaListaPaquetes().add(paquete);
                unaJaula = em.merge(unaJaula);
            }
            for (MovimientoPaquete unaListaMovimientosMovimientoPaquete : paquete.getUnaListaMovimientos()) {
                Paquete oldUnPaqueteOfUnaListaMovimientosMovimientoPaquete = unaListaMovimientosMovimientoPaquete.getUnPaquete();
                unaListaMovimientosMovimientoPaquete.setUnPaquete(paquete);
                unaListaMovimientosMovimientoPaquete = em.merge(unaListaMovimientosMovimientoPaquete);
                if (oldUnPaqueteOfUnaListaMovimientosMovimientoPaquete != null) {
                    oldUnPaqueteOfUnaListaMovimientosMovimientoPaquete.getUnaListaMovimientos().remove(unaListaMovimientosMovimientoPaquete);
                    oldUnPaqueteOfUnaListaMovimientosMovimientoPaquete = em.merge(oldUnPaqueteOfUnaListaMovimientosMovimientoPaquete);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPaquete(paquete.getIdPaquete()) != null) {
                throw new PreexistingEntityException("Paquete " + paquete + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Paquete paquete) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Paquete persistentPaquete = em.find(Paquete.class, paquete.getIdPaquete());
            Envio unEnvioOld = persistentPaquete.getUnEnvio();
            Envio unEnvioNew = paquete.getUnEnvio();
            Jaula unaJaulaOld = persistentPaquete.getUnaJaula();
            Jaula unaJaulaNew = paquete.getUnaJaula();
            List<MovimientoPaquete> unaListaMovimientosOld = persistentPaquete.getUnaListaMovimientos();
            List<MovimientoPaquete> unaListaMovimientosNew = paquete.getUnaListaMovimientos();
            if (unEnvioNew != null) {
                unEnvioNew = em.getReference(unEnvioNew.getClass(), unEnvioNew.getIdEnvio());
                paquete.setUnEnvio(unEnvioNew);
            }
            if (unaJaulaNew != null) {
                unaJaulaNew = em.getReference(unaJaulaNew.getClass(), unaJaulaNew.getIdJaula());
                paquete.setUnaJaula(unaJaulaNew);
            }
            List<MovimientoPaquete> attachedUnaListaMovimientosNew = new ArrayList<MovimientoPaquete>();
            for (MovimientoPaquete unaListaMovimientosNewMovimientoPaqueteToAttach : unaListaMovimientosNew) {
                unaListaMovimientosNewMovimientoPaqueteToAttach = em.getReference(unaListaMovimientosNewMovimientoPaqueteToAttach.getClass(), unaListaMovimientosNewMovimientoPaqueteToAttach.getIdMovimiento());
                attachedUnaListaMovimientosNew.add(unaListaMovimientosNewMovimientoPaqueteToAttach);
            }
            unaListaMovimientosNew = attachedUnaListaMovimientosNew;
            paquete.setUnaListaMovimientos(unaListaMovimientosNew);
            paquete = em.merge(paquete);
            if (unEnvioOld != null && !unEnvioOld.equals(unEnvioNew)) {
                unEnvioOld.getUnaListaPaquetes().remove(paquete);
                unEnvioOld = em.merge(unEnvioOld);
            }
            if (unEnvioNew != null && !unEnvioNew.equals(unEnvioOld)) {
                unEnvioNew.getUnaListaPaquetes().add(paquete);
                unEnvioNew = em.merge(unEnvioNew);
            }
            if (unaJaulaOld != null && !unaJaulaOld.equals(unaJaulaNew)) {
                unaJaulaOld.getUnaListaPaquetes().remove(paquete);
                unaJaulaOld = em.merge(unaJaulaOld);
            }
            if (unaJaulaNew != null && !unaJaulaNew.equals(unaJaulaOld)) {
                unaJaulaNew.getUnaListaPaquetes().add(paquete);
                unaJaulaNew = em.merge(unaJaulaNew);
            }
            for (MovimientoPaquete unaListaMovimientosOldMovimientoPaquete : unaListaMovimientosOld) {
                if (!unaListaMovimientosNew.contains(unaListaMovimientosOldMovimientoPaquete)) {
                    unaListaMovimientosOldMovimientoPaquete.setUnPaquete(null);
                    unaListaMovimientosOldMovimientoPaquete = em.merge(unaListaMovimientosOldMovimientoPaquete);
                }
            }
            for (MovimientoPaquete unaListaMovimientosNewMovimientoPaquete : unaListaMovimientosNew) {
                if (!unaListaMovimientosOld.contains(unaListaMovimientosNewMovimientoPaquete)) {
                    Paquete oldUnPaqueteOfUnaListaMovimientosNewMovimientoPaquete = unaListaMovimientosNewMovimientoPaquete.getUnPaquete();
                    unaListaMovimientosNewMovimientoPaquete.setUnPaquete(paquete);
                    unaListaMovimientosNewMovimientoPaquete = em.merge(unaListaMovimientosNewMovimientoPaquete);
                    if (oldUnPaqueteOfUnaListaMovimientosNewMovimientoPaquete != null && !oldUnPaqueteOfUnaListaMovimientosNewMovimientoPaquete.equals(paquete)) {
                        oldUnPaqueteOfUnaListaMovimientosNewMovimientoPaquete.getUnaListaMovimientos().remove(unaListaMovimientosNewMovimientoPaquete);
                        oldUnPaqueteOfUnaListaMovimientosNewMovimientoPaquete = em.merge(oldUnPaqueteOfUnaListaMovimientosNewMovimientoPaquete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = paquete.getIdPaquete();
                if (findPaquete(id) == null) {
                    throw new NonexistentEntityException("The paquete with id " + id + " no longer exists.");
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
            Paquete paquete;
            try {
                paquete = em.getReference(Paquete.class, id);
                paquete.getIdPaquete();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paquete with id " + id + " no longer exists.", enfe);
            }
            Envio unEnvio = paquete.getUnEnvio();
            if (unEnvio != null) {
                unEnvio.getUnaListaPaquetes().remove(paquete);
                unEnvio = em.merge(unEnvio);
            }
            Jaula unaJaula = paquete.getUnaJaula();
            if (unaJaula != null) {
                unaJaula.getUnaListaPaquetes().remove(paquete);
                unaJaula = em.merge(unaJaula);
            }
            List<MovimientoPaquete> unaListaMovimientos = paquete.getUnaListaMovimientos();
            for (MovimientoPaquete unaListaMovimientosMovimientoPaquete : unaListaMovimientos) {
                unaListaMovimientosMovimientoPaquete.setUnPaquete(null);
                unaListaMovimientosMovimientoPaquete = em.merge(unaListaMovimientosMovimientoPaquete);
            }
            em.remove(paquete);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Paquete> findPaqueteEntities() {
        return findPaqueteEntities(true, -1, -1);
    }

    public List<Paquete> findPaqueteEntities(int maxResults, int firstResult) {
        return findPaqueteEntities(false, maxResults, firstResult);
    }

    private List<Paquete> findPaqueteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paquete.class));
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

    public Paquete findPaquete(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paquete.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaqueteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paquete> rt = cq.from(Paquete.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
