package br.com.medeve

import br.com.medeve.Activitys.CadastrarUsuarioActView
import br.com.medeve.Activitys.EntrarUsuarioActView
import br.com.medeve.ViewModels.UsuarioViewModel
import br.com.medeve.Repository.UsuarioRepository
import org.koin.dsl.module

object Modules{
    val activityModules = module {
        single { EntrarUsuarioActView() }
        single { CadastrarUsuarioActView() }
    }

    val viewModel = module {
        factory { UsuarioViewModel(get()) }
    }

    val repository = module {
        factory { UsuarioRepository() }
    }
}
