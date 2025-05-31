package com.example.control2TBD.Service;

import com.example.control2TBD.Entity.ClienteEntity;
import com.example.control2TBD.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepo;

    public ClienteEntity getById(long idCliente) {
        return clienteRepo.findById(idCliente);
    }

    public List<ClienteEntity> getAll() {
        return clienteRepo.findAll();
    }

    public void save(ClienteEntity cliente) {
        clienteRepo.save(cliente);
    }

    public void update(ClienteEntity cliente) {
        clienteRepo.update(cliente);
    }

    public void delete(long idCliente) {
        clienteRepo.deleteById(idCliente);
    }

}
