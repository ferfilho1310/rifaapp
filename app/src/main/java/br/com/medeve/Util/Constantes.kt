package br.com.medeve.Util

class Constantes  {

    object CadastroUsuario {
        const val SUCESSO_CADASTRO_USUARIO = 0
        const val SENHA_COM_MENOS_DE_SEIS_CARACTERES = 1
        const val EMAIL_INVALIDO = 2
        const val EMAIL_JA_CADASTRADO = 3
        const val ERRO_DESCONHECIDO = 99
        const val INTERNET_OFF = 4
    }

    object EntrarUsuario {
        const val LOGIN_REALIZADO_COM_SUCESSO = 0
        const val FALHA_NO_LOGIN = 1
    }

    object ResetSenha {
        const val RESET_SENHA_SUCESSO = 0
        const val EMAIL_INVALIDO = 1
        const val ERRO_ENVIO_EMAIL = 2
    }
}