package br.com.medeve.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.medeve.Models.Cep
import br.com.medeve.Repository.CepRepository
import br.com.medeve.Repository.ClienteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CepViewModel(val cepRepository: CepRepository) : ViewModel() {

    private val cepCliente = MutableLiveData<Cep>()
    val celClienteObserver = cepCliente

    fun getCep(cep: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                cepRepository.getCep(cep)
            }.onSuccess {
                celClienteObserver.postValue(it)
            }.onFailure {
                Log.i("Fernando", "Resultado ${it.printStackTrace()}")
            }
        }
    }

}