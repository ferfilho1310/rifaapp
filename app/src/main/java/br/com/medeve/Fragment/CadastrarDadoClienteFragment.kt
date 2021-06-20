package br.com.medeve.Fragment

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import br.com.medeve.Models.Cliente
import br.com.medeve.R
import br.com.medeve.ViewModels.CepViewModel
import br.com.medeve.ViewModels.ClienteViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_cadastro_cliente.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CadastrarDadoClienteFragment : BottomSheetDialogFragment(), View.OnClickListener {

    val cepViewModel: CepViewModel by viewModel()
    val clienteViewModel: ClienteViewModel by viewModel()
    val dadosCliente = Cliente()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cadastro_cliente, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnBuscarCep.setOnClickListener(this)
        btnAtualizarCliente.setOnClickListener(this)

        setObservers()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBuscarCep -> {
                cepViewModel.getCep(ed_cep?.text.toString())
                return
            }
            R.id.btnAtualizarCliente -> {
                when {
                    edNomeCliente.text.toString().isEmpty() -> {
                        edNomeCliente.error = String.format("Informe o nome do cliente")
                        return
                    }
                    ed_telefone.text.toString().isEmpty() -> {
                        ed_telefone.error = String.format("Informe o telefone do cliente")
                        return
                    }
                    edEndereco.text.toString().isEmpty() -> {
                        edEndereco.error = String.format("Informe o endereço do cliente")
                        return
                    }
                    edBairro.text.toString().isEmpty() -> {
                        edBairro.error = String.format("Informe o bairro do cliente")
                        return
                    }
                    edCidade.text.toString().isEmpty() -> {
                        edCidade.error = String.format("Informe a cidade do cliente")
                        return
                    }
                    ed_numero.text.toString().isEmpty() -> {
                        ed_numero.error = String.format("Informe o número do cliente")
                        return
                    }
                    edEstado.text.toString().isEmpty() -> {
                        edEstado.error = String.format("Informe o estado do cliente")
                        return
                    }
                    else -> {

                        dadosCliente.nome = edNomeCliente.getText().toString()
                        dadosCliente.telefone = ed_telefone.getText().toString()
                        dadosCliente.cep = ed_cep.getText().toString()
                        dadosCliente.logradouro = edEndereco.getText().toString()
                        dadosCliente.bairro = edBairro.getText().toString()
                        dadosCliente.cidade = edCidade.getText().toString()
                        dadosCliente.estado = edEstado.getText().toString()
                        dadosCliente.numero = ed_numero.getText().toString()

                        clienteViewModel.saveCliente(dadosCliente)
                        dismiss()
                    }
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    fun setObservers() {
        cepViewModel.celClienteObserver.observe(this, {

            dadosCliente.logradouro = it.logradouro
            dadosCliente.bairro = it.bairro
            dadosCliente.cidade = it.localidade
            dadosCliente.estado = it.uf

            edEndereco.setText(dadosCliente.logradouro)
            edBairro.setText(dadosCliente.bairro)
            edCidade.setText(dadosCliente.cidade)
            edEstado.setText(dadosCliente.estado)
        })

      /*  clienteViewModel.saveCliente().observe(this, {
            when (it) {
                Constantes.ClienteRepository.CLIENTE_SALVO_SUCESSO -> {
                    Toast.makeText(activity, "Cliente salvo com sucesso", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                Constantes.ClienteRepository.FALHA_AO_SALVAR_CLIENTE -> {
                    Toast.makeText(
                        activity,
                        "Falha ao salvar os dados do cliente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })*/
    }


    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}