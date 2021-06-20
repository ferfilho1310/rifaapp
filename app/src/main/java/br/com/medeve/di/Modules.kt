package br.com.medeve

import br.com.medeve.Activitys.*
import br.com.medeve.Fragment.CadastrarDadoClienteFragment
import br.com.medeve.Repository.CepRepository
import br.com.medeve.Repository.ClienteRepository
import br.com.medeve.Repository.DatasVendasClienteRepository
import br.com.medeve.ViewModels.UsuarioViewModel
import br.com.medeve.Repository.UsuarioRepository
import br.com.medeve.Retrofit.getcepService
import br.com.medeve.Retrofit.retrofitBuilder
import br.com.medeve.ViewModels.CepViewModel
import br.com.medeve.ViewModels.ClienteViewModel
import br.com.medeve.ViewModels.DataVendasClienteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {
    val activityModules = module {
        single { EntrarUsuarioActView() }
        single { CadastrarUsuarioActView() }
        single { ResetSenha() }
        single { CadastroClienteActView() }
        single { CadastrarDadoClienteFragment() }
        single { DatasVendasCliente() }
        single { SplashScreen() }
    }

    val viewModel = module {
        viewModel { UsuarioViewModel(get()) }
        viewModel { CepViewModel(get()) }
        viewModel { ClienteViewModel(get()) }
        viewModel { DataVendasClienteViewModel(get()) }
    }

    val repository = module {
        single { UsuarioRepository() }
        single { ClienteRepository() }
        factory { CepRepository(get()) }
        single { DatasVendasClienteRepository() }
    }

    val retrofit = module(override = true) {
        factory { retrofitBuilder() }
        factory { getcepService(get()) }
    }

    val adapters = module {

    }

}
