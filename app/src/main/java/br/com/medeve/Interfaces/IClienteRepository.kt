package br.com.medeve.Interfaces

import br.com.medeve.Models.Cliente

interface IClienteRepository {
    fun salvarCliente(cliente: Cliente)
}