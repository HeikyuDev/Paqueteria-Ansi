/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.guia2_punto6.persistencia;

import com.mycompany.guia2_punto6.logica.ClienteFijo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.guia2_punto6.logica.Envio;
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
public class ClienteFijoJpaController implements Serializable {

    public ClienteFijoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClienteFijo clienteFijo) throws PreexistingEntityException, Exception {
        if (clienteFijo.getListaEnvios() == null) {
            clienteFijo.setListaEnvios(new ArrayList<Envio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Envio> attachedListaEnvios = new ArrayList<Envio>();
            for (Envio listaEnviosEnvioToAttach : clienteFijo.getListaEnvios()) {
                listaEnviosEnvioToAttach = em.getReference(listaEnviosEnvioToAttach.getClass(), listaEnviosEnvioToAttach.getIdEnvio());
                attachedListaEnvios.add(listaEnviosEnvioToAttach);
            }
            clienteFijo.setListaEnvios(attachedListaEnvios);
            em.persist(clienteFijo);
            for (Envio listaEnviosEnvio : clienteFijo.getListaEnvios()) {
                ClienteFijo oldUnClienteFijoOfListaEnviosEnvio = listaEnviosEnvio.getUnClienteFijo();
                listaEnviosEnvio.setUnClienteFijo(clienteFijo);
                listaEnviosEnvio = em.merge(listaEnviosEnvio);
                if (oldUnClienteFijoOfListaEnviosEnvio != null) {
                    oldUnClienteFijoOfListaEnviosEnvio.getListaEnvios().remove(listaEnviosEnvio);
                    oldUnClienteFijoOfListaEnviosEnvio = em.merge(oldUnClienteFijoOfListaEnviosEnvio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClienteFijo(clienteFijo.getCodigoCliente()) != null) {
                throw new PreexistingEntityException("ClienteFijo " + clienteFijo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClienteFijo clienteFijo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClienteFijo persistentClienteFijo = em.find(ClienteFijo.class, clienteFijo.getCodigoCliente());
            List<Envio> listaEnviosOld = persistentClienteFijo.getListaEnvios();
            List<Envio> listaEnviosNew = clienteFijo.getListaEnvios();
            List<Envio> attachedListaEnviosNew = new ArrayList<Envio>();
            for (Envio listaEnviosNewEnvioToAttach : listaEnviosNew) {
                listaEnviosNewEnvioToAttach = em.getReference(listaEnviosNewEnvioToAttach.getClass(), listaEnviosNewEnvioToAttach.getIdEnvio());
                attachedListaEnviosNew.add(listaEnviosNewEnvioToAttach);
            }
            listaEnviosNew = attachedListaEnviosNew;
            clienteFijo.setListaEnvios(listaEnviosNew);
            clienteFijo = em.merge(clienteFijo);
            for (Envio listaEnviosOldEnvio : listaEnviosOld) {
                if (!listaEnviosNew.contains(listaEnviosOldEnvio)) {
                    listaEnviosOldEnvio.setUnClienteFijo(null);
                    listaEnviosOldEnvio = em.merge(listaEnviosOldEnvio);
                }
            }
            for (Envio listaEnviosNewEnvio : listaEnviosNew) {
                if (!listaEnviosOld.contains(listaEnviosNewEnvio)) {
                    ClienteFijo oldUnClienteFijoOfListaEnviosNewEnvio = listaEnviosNewEnvio.getUnClienteFijo();
                    listaEnviosNewEnvio.setUnClienteFijo(clienteFijo);
                    listaEnviosNewEnvio = em.merge(listaEnviosNewEnvio);
                    if (oldUnClienteFijoOfListaEnviosNewEnvio != null && !oldUnClienteFijoOfListaEnviosNewEnvio.equals(clienteFijo)) {
                        oldUnClienteFijoOfListaEnviosNewEnvio.getListaEnvios().remove(listaEnviosNewEnvio);
                        oldUnClienteFijoOfListaEnviosNewEnvio = em.merge(oldUnClienteFijoOfListaEnviosNewEnvio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = clienteFijo.getCodigoCliente();
                if (findClienteFijo(id) == null) {
                    throw new NonexistentEntityException("The clienteFijo with id " + id + " no longer exists.");
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
            ClienteFijo clienteFijo;
            try {
                clienteFijo = em.getReference(ClienteFijo.class, id);
                clienteFijo.getCodigoCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clienteFijo with id " + id + " no longer exists.", enfe);
            }
            List<Envio> listaEnvios = clienteFijo.getListaEnvios();
            for (Envio listaEnviosEnvio : listaEnvios) {
                listaEnviosEnvio.setUnClienteFijo(null);
                listaEnviosEnvio = em.merge(listaEnviosEnvio);
            }
            em.remove(clienteFijo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClienteFijo> findClienteFijoEntities() {
        return findClienteFijoEntities(true, -1, -1);
    }

    public List<ClienteFijo> findClienteFijoEntities(int maxResults, int firstResult) {
        return findClienteFijoEntities(false, maxResults, firstResult);
    }

    private List<ClienteFijo> findClienteFijoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClienteFijo.class));
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

    public ClienteFijo findClienteFijo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClienteFijo.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteFijoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClienteFijo> rt = cq.from(ClienteFijo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
