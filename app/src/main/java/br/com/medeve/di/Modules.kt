package br.com.medeve

import androidx.lifecycle.MutableLiveData
import br.com.medeve.Activitys.CadastrarUsuarioActView
import br.com.medeve.Activitys.EntrarUsuarioActView
import br.com.medeve.Activitys.ResetSenha
import br.com.medeve.Interfaces.IUsuarioDao
import br.com.medeve.ViewModels.UsuarioViewModel
import br.com.medeve.Repository.UsuarioRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules{
    val activityModules = module {
        single { EntrarUsuarioActView() }
        single { CadastrarUsuarioActView() }
        single { ResetSenha() }
    }

    val viewModel = module {
        viewModel { UsuarioViewModel(get() as UsuarioRepository) }
    }

    val repository = module {
        single { UsuarioRepository() }
    }

}
