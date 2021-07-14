package br.com.medeve.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.medeve.Models.Produto
import br.com.medeve.R
import br.com.medeve.ViewModels.ProdutosViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_cadastrar_produto.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class CadastroProdutoFragment : BottomSheetDialogFragment(), View.OnClickListener {

    var id_data: String = String()
    val produtosViewModel: ProdutosViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cadastrar_produto, container, false)

        val receber: Intent = activity?.intent!!
        val data = receber.extras

        id_data = data!!.getString("id_data_compra")!!
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setListeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cadastrarProduto -> {
                salvarDadosProdutos()
            }
        }
    }

    private fun setListeners() {
        cadastrarProduto.setOnClickListener(this)
    }

    @SuppressLint("SimpleDateFormat")
    private fun salvarDadosProdutos() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        val data = dateFormat.format(date)

        val produto = Produto()

        when {
            ed_nome_produto.text.toString().isEmpty() -> {
                Toast.makeText(
                    activity?.applicationContext,
                    "Informe o nome do produto vendido",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ed_quantidade.text.toString().isEmpty() -> {
                Toast.makeText(
                    activity?.applicationContext,
                    "Informe a quantidade vendida",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ed_preco_produto.text.toString().isEmpty() -> {
                Toast.makeText(
                    activity?.applicationContext,
                    "Informe o preço do(s) produtos(s) vendido(s)",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                produto.nomedoproduto = ed_nome_produto.text.toString()
                produto.quantidade = ed_quantidade.text.toString()
                produto.valor = ed_preco_produto.text.toString()
                produto.total =
                    (ed_quantidade.text.toString().toFloat() * ed_preco_produto.text.toString()
                        .toFloat()).toString()
                produto.data = data
                produto.recebido = false
                produto.devolvido = false

                produtosViewModel.salvarProdutos(produto, id_data)
                dismiss()
            }
        }


    }
}