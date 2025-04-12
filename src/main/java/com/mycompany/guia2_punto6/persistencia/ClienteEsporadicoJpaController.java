/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.guia2_punto6.persistencia;

import com.mycompany.guia2_punto6.logica.ClienteEsporadico;
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
public class ClienteEsporadicoJpaController implements Serializable {

    public ClienteEsporadicoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClienteEsporadico clienteEsporadico) throws PreexistingEntityException, Exception {
        if (clienteEsporadico.getListaEnvios() == null) {
            clienteEsporadico.setListaEnvios(new ArrayList<Envio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Envio> attachedListaEnvios = new ArrayList<Envio>();
            for (Envio listaEnviosEnvioToAttach : clienteEsporadico.getListaEnvios()) {
                listaEnviosEnvioToAttach = em.getReference(listaEnviosEnvioToAttach.getClass(), listaEnviosEnvioToAttach.getIdEnvio());
                attachedListaEnvios.add(listaEnviosEnvioToAttach);
            }
            clienteEsporadico.setListaEnvios(attachedListaEnvios);
            em.persist(clienteEsporadico);
            for (Envio listaEnviosEnvio : clienteEsporadico.getListaEnvios()) {
                ClienteEsporadico oldUnClienteEsporadicoOfListaEnviosEnvio = listaEnviosEnvio.getUnClienteEsporadico();
                listaEnviosEnvio.setUnClienteEsporadico(clienteEsporadico);
                listaEnviosEnvio = em.merge(listaEnviosEnvio);
                if (oldUnClienteEsporadicoOfListaEnviosEnvio != null) {
                    oldUnClienteEsporadicoOfListaEnviosEnvio.getListaEnvios().remove(listaEnviosEnvio);
                    oldUnClienteEsporadicoOfListaEnviosEnvio = em.merge(oldUnClienteEsporadicoOfListaEnviosEnvio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClienteEsporadico(clienteEsporadico.getDniCliente()) != null) {
                throw new PreexistingEntityException("ClienteEsporadico " + clienteEsporadico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClienteEsporadico clienteEsporadico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClienteEsporadico persistentClienteEsporadico = em.find(ClienteEsporadico.class, clienteEsporadico.getDniCliente());
            List<Envio> listaEnviosOld = persistentClienteEsporadico.getListaEnvios();
            List<Envio> listaEnviosNew = clienteEsporadico.getListaEnvios();
            List<Envio> attachedListaEnviosNew = new ArrayList<Envio>();
            for (Envio listaEnviosNewEnvioToAttach : listaEnviosNew) {
                listaEnviosNewEnvioToAttach = em.getReference(listaEnviosNewEnvioToAttach.getClass(), listaEnviosNewEnvioToAttach.getIdEnvio());
                attachedListaEnviosNew.add(listaEnviosNewEnvioToAttach);
            }
            listaEnviosNew = attachedListaEnviosNew;
            clienteEsporadico.setListaEnvios(listaEnviosNew);
            clienteEsporadico = em.merge(clienteEsporadico);
            for (Envio listaEnviosOldEnvio : listaEnviosOld) {
                if (!listaEnviosNew.contains(listaEnviosOldEnvio)) {
                    listaEnviosOldEnvio.setUnClienteEsporadico(null);
                    listaEnviosOldEnvio = em.merge(listaEnviosOldEnvio);
                }
            }
            for (Envio listaEnviosNewEnvio : listaEnviosNew) {
                if (!listaEnviosOld.contains(listaEnviosNewEnvio)) {
                    ClienteEsporadico oldUnClienteEsporadicoOfListaEnviosNewEnvio = listaEnviosNewEnvio.getUnClienteEsporadico();
                    listaEnviosNewEnvio.setUnClienteEsporadico(clienteEsporadico);
                    listaEnviosNewEnvio = em.merge(listaEnviosNewEnvio);
                    if (oldUnClienteEsporadicoOfListaEnviosNewEnvio != null && !oldUnClienteEsporadicoOfListaEnviosNewEnvio.equals(clienteEsporadico)) {
                        oldUnClienteEsporadicoOfListaEnviosNewEnvio.getListaEnvios().remove(listaEnviosNewEnvio);
                        oldUnClienteEsporadicoOfListaEnviosNewEnvio = em.merge(oldUnClienteEsporadicoOfListaEnviosNewEnvio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = clienteEsporadico.getDniCliente();
                if (findClienteEsporadico(id) == null) {
                    throw new NonexistentEntityException("The clienteEsporadico with id " + id + " no longer exists.");
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
            ClienteEsporadico clienteEsporadico;
            try {
                clienteEsporadico = em.getReference(ClienteEsporadico.class, id);
                clienteEsporadico.getDniCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clienteEsporadico with id " + id + " no longer exists.", enfe);
            }
            List<Envio> listaEnvios = clienteEsporadico.getListaEnvios();
            for (Envio listaEnviosEnvio : listaEnvios) {
                listaEnviosEnvio.setUnClienteEsporadico(null);
                listaEnviosEnvio = em.merge(listaEnviosEnvio);
            }
            em.remove(clienteEsporadico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClienteEsporadico> findClienteEsporadicoEntities() {
        return findClienteEsporadicoEntities(true, -1, -1);
    }

    public List<ClienteEsporadico> findClienteEsporadicoEntities(int maxResults, int firstResult) {
        return findClienteEsporadicoEntities(false, maxResults, firstResult);
    }

    private List<ClienteEsporadico> findClienteEsporadicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClienteEsporadico.class));
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

    public ClienteEsporadico findClienteEsporadico(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClienteEsporadico.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteEsporadicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClienteEsporadico> rt = cq.from(ClienteEsporadico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
