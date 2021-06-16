package br.com.medeve

import br.com.medeve.Activitys.CadastrarUsuarioActView
import br.com.medeve.Activitys.CadastroClienteActView
import br.com.medeve.Activitys.EntrarUsuarioActView
import br.com.medeve.Activitys.ResetSenha
import br.com.medeve.Fragment.CadastrarDadoClienteFragment
import br.com.medeve.Repository.CepRepository
import br.com.medeve.Repository.ClienteRepository
import br.com.medeve.ViewModels.UsuarioViewModel
import br.com.medeve.Repository.UsuarioRepository
import br.com.medeve.Retrofit.getcepService
import br.com.medeve.Retrofit.retrofitBuilder
import br.com.medeve.ViewModels.CepViewModel
import br.com.medeve.ViewModels.ClienteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules{
    val activityModules = module {
        single { EntrarUsuarioActView() }
        single { CadastrarUsuarioActView() }
        single { ResetSenha() }
        single { CadastroClienteActView() }
        single { CadastrarDadoClienteFragment() }
    }

    val viewModel = module {
        viewModel { UsuarioViewModel(get() as UsuarioRepository) }
        viewModel { CepViewModel(get()) }
        viewModel { ClienteViewModel(get()) }
    }

    val repository = module {
        single { UsuarioRepository() }
        single { ClienteRepository() }
        factory { CepRepository(get()) }
    }

    val retrofit = module(override = true) {
        factory { retrofitBuilder() }
        factory { getcepService(get()) }
    }

    val adapters = module {

    }

}
