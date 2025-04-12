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
import com.mycompany.guia2_punto6.logica.ClienteFijo;
import com.mycompany.guia2_punto6.logica.ClienteEsporadico;
import com.mycompany.guia2_punto6.logica.Envio;
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
public class EnvioJpaController implements Serializable {

    public EnvioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Envio envio) throws PreexistingEntityException, Exception {
        if (envio.getUnaListaPaquetes() == null) {
            envio.setUnaListaPaquetes(new ArrayList<Paquete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClienteFijo unClienteFijo = envio.getUnClienteFijo();
            if (unClienteFijo != null) {
                unClienteFijo = em.getReference(unClienteFijo.getClass(), unClienteFijo.getCodigoCliente());
                envio.setUnClienteFijo(unClienteFijo);
            }
            ClienteEsporadico unClienteEsporadico = envio.getUnClienteEsporadico();
            if (unClienteEsporadico != null) {
                unClienteEsporadico = em.getReference(unClienteEsporadico.getClass(), unClienteEsporadico.getDniCliente());
                envio.setUnClienteEsporadico(unClienteEsporadico);
            }
            List<Paquete> attachedUnaListaPaquetes = new ArrayList<Paquete>();
            for (Paquete unaListaPaquetesPaqueteToAttach : envio.getUnaListaPaquetes()) {
                unaListaPaquetesPaqueteToAttach = em.getReference(unaListaPaquetesPaqueteToAttach.getClass(), unaListaPaquetesPaqueteToAttach.getIdPaquete());
                attachedUnaListaPaquetes.add(unaListaPaquetesPaqueteToAttach);
            }
            envio.setUnaListaPaquetes(attachedUnaListaPaquetes);
            em.persist(envio);
            if (unClienteFijo != null) {
                unClienteFijo.getListaEnvios().add(envio);
                unClienteFijo = em.merge(unClienteFijo);
            }
            if (unClienteEsporadico != null) {
                unClienteEsporadico.getListaEnvios().add(envio);
                unClienteEsporadico = em.merge(unClienteEsporadico);
            }
            for (Paquete unaListaPaquetesPaquete : envio.getUnaListaPaquetes()) {
                Envio oldUnEnvioOfUnaListaPaquetesPaquete = unaListaPaquetesPaquete.getUnEnvio();
                unaListaPaquetesPaquete.setUnEnvio(envio);
                unaListaPaquetesPaquete = em.merge(unaListaPaquetesPaquete);
                if (oldUnEnvioOfUnaListaPaquetesPaquete != null) {
                    oldUnEnvioOfUnaListaPaquetesPaquete.getUnaListaPaquetes().remove(unaListaPaquetesPaquete);
                    oldUnEnvioOfUnaListaPaquetesPaquete = em.merge(oldUnEnvioOfUnaListaPaquetesPaquete);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEnvio(envio.getIdEnvio()) != null) {
                throw new PreexistingEntityException("Envio " + envio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Envio envio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Envio persistentEnvio = em.find(Envio.class, envio.getIdEnvio());
            ClienteFijo unClienteFijoOld = persistentEnvio.getUnClienteFijo();
            ClienteFijo unClienteFijoNew = envio.getUnClienteFijo();
            ClienteEsporadico unClienteEsporadicoOld = persistentEnvio.getUnClienteEsporadico();
            ClienteEsporadico unClienteEsporadicoNew = envio.getUnClienteEsporadico();
            List<Paquete> unaListaPaquetesOld = persistentEnvio.getUnaListaPaquetes();
            List<Paquete> unaListaPaquetesNew = envio.getUnaListaPaquetes();
            if (unClienteFijoNew != null) {
                unClienteFijoNew = em.getReference(unClienteFijoNew.getClass(), unClienteFijoNew.getCodigoCliente());
                envio.setUnClienteFijo(unClienteFijoNew);
            }
            if (unClienteEsporadicoNew != null) {
                unClienteEsporadicoNew = em.getReference(unClienteEsporadicoNew.getClass(), unClienteEsporadicoNew.getDniCliente());
                envio.setUnClienteEsporadico(unClienteEsporadicoNew);
            }
            List<Paquete> attachedUnaListaPaquetesNew = new ArrayList<Paquete>();
            for (Paquete unaListaPaquetesNewPaqueteToAttach : unaListaPaquetesNew) {
                unaListaPaquetesNewPaqueteToAttach = em.getReference(unaListaPaquetesNewPaqueteToAttach.getClass(), unaListaPaquetesNewPaqueteToAttach.getIdPaquete());
                attachedUnaListaPaquetesNew.add(unaListaPaquetesNewPaqueteToAttach);
            }
            unaListaPaquetesNew = attachedUnaListaPaquetesNew;
            envio.setUnaListaPaquetes(unaListaPaquetesNew);
            envio = em.merge(envio);
            if (unClienteFijoOld != null && !unClienteFijoOld.equals(unClienteFijoNew)) {
                unClienteFijoOld.getListaEnvios().remove(envio);
                unClienteFijoOld = em.merge(unClienteFijoOld);
            }
            if (unClienteFijoNew != null && !unClienteFijoNew.equals(unClienteFijoOld)) {
                unClienteFijoNew.getListaEnvios().add(envio);
                unClienteFijoNew = em.merge(unClienteFijoNew);
            }
            if (unClienteEsporadicoOld != null && !unClienteEsporadicoOld.equals(unClienteEsporadicoNew)) {
                unClienteEsporadicoOld.getListaEnvios().remove(envio);
                unClienteEsporadicoOld = em.merge(unClienteEsporadicoOld);
            }
            if (unClienteEsporadicoNew != null && !unClienteEsporadicoNew.equals(unClienteEsporadicoOld)) {
                unClienteEsporadicoNew.getListaEnvios().add(envio);
                unClienteEsporadicoNew = em.merge(unClienteEsporadicoNew);
            }
            for (Paquete unaListaPaquetesOldPaquete : unaListaPaquetesOld) {
                if (!unaListaPaquetesNew.contains(unaListaPaquetesOldPaquete)) {
                    unaListaPaquetesOldPaquete.setUnEnvio(null);
                    unaListaPaquetesOldPaquete = em.merge(unaListaPaquetesOldPaquete);
                }
            }
            for (Paquete unaListaPaquetesNewPaquete : unaListaPaquetesNew) {
                if (!unaListaPaquetesOld.contains(unaListaPaquetesNewPaquete)) {
                    Envio oldUnEnvioOfUnaListaPaquetesNewPaquete = unaListaPaquetesNewPaquete.getUnEnvio();
                    unaListaPaquetesNewPaquete.setUnEnvio(envio);
                    unaListaPaquetesNewPaquete = em.merge(unaListaPaquetesNewPaquete);
                    if (oldUnEnvioOfUnaListaPaquetesNewPaquete != null && !oldUnEnvioOfUnaListaPaquetesNewPaquete.equals(envio)) {
                        oldUnEnvioOfUnaListaPaquetesNewPaquete.getUnaListaPaquetes().remove(unaListaPaquetesNewPaquete);
                        oldUnEnvioOfUnaListaPaquetesNewPaquete = em.merge(oldUnEnvioOfUnaListaPaquetesNewPaquete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = envio.getIdEnvio();
                if (findEnvio(id) == null) {
                    throw new NonexistentEntityException("The envio with id " + id + " no longer exists.");
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
            Envio envio;
            try {
                envio = em.getReference(Envio.class, id);
                envio.getIdEnvio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The envio with id " + id + " no longer exists.", enfe);
            }
            ClienteFijo unClienteFijo = envio.getUnClienteFijo();
            if (unClienteFijo != null) {
                unClienteFijo.getListaEnvios().remove(envio);
                unClienteFijo = em.merge(unClienteFijo);
            }
            ClienteEsporadico unClienteEsporadico = envio.getUnClienteEsporadico();
            if (unClienteEsporadico != null) {
                unClienteEsporadico.getListaEnvios().remove(envio);
                unClienteEsporadico = em.merge(unClienteEsporadico);
            }
            List<Paquete> unaListaPaquetes = envio.getUnaListaPaquetes();
            for (Paquete unaListaPaquetesPaquete : unaListaPaquetes) {
                unaListaPaquetesPaquete.setUnEnvio(null);
                unaListaPaquetesPaquete = em.merge(unaListaPaquetesPaquete);
            }
            em.remove(envio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Envio> findEnvioEntities() {
        return findEnvioEntities(true, -1, -1);
    }

    public List<Envio> findEnvioEntities(int maxResults, int firstResult) {
        return findEnvioEntities(false, maxResults, firstResult);
    }

    private List<Envio> findEnvioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Envio.class));
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

    public Envio findEnvio(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Envio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnvioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Envio> rt = cq.from(Envio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
