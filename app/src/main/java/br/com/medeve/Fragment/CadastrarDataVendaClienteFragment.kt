package br.com.medeve.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.medeve.Models.Cliente
import br.com.medeve.Models.DataCobrancaVenda
import br.com.medeve.R
import br.com.medeve.ViewModels.DataVendasClienteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_cadastro_data_venda_cliente.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CadastrarDataVendaClienteFragment : BottomSheetDialogFragment(), View.OnClickListener {

    val dataVendasClienteViewModel: DataVendasClienteViewModel by viewModel()

    var idClient: String? = null

    var cliente: Cliente? = Cliente()
    var dataVendaCliente: DataCobrancaVenda = DataCobrancaVenda()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cadastro_data_venda_cliente, container, false)
        idClient = activity?.intent?.extras?.getString("id_cliente")
        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnCadastrarDatas.setOnClickListener(this)

        datePickDataVenda()
        datePickDataCompra()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCadastrarDatas -> {
                when {
                    ed_data_venda.toString().isEmpty() -> {
                        Toast.makeText(activity, "Informe a data da venda", Toast.LENGTH_SHORT).show()
                        return
                    }
                    ed_data_cobranca.toString().isEmpty() -> {
                        Toast.makeText(activity, "Informe a data de cobrar", Toast.LENGTH_SHORT).show()
                        return
                    }
                    else -> {
                        dataVendaCliente.data_cobranca = ed_data_venda.text.toString()
                        dataVendaCliente.data_venda = ed_data_cobranca.text.toString()

                        dataVendasClienteViewModel.adicionarDatasVendaCliente(
                            idClient,
                            dataVendaCliente
                        )
                        dismiss()
                    }
                }
            }
        }
    }

    fun datePickDataVenda() {

        val calendar = Calendar.getInstance()
        val dia = calendar[Calendar.DAY_OF_MONTH]
        val mes = calendar[Calendar.MONTH]
        val ano = calendar[Calendar.YEAR]

        btn_data_venda.setOnClickListener {
            ed_data_venda.setText("")
            val dpd = DatePickerDialog(
                requireActivity(), { view, year, monthOfYear, dayOfMonth ->
                    var data = ""

                    if(monthOfYear.toString().length > 1){
                        data = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    } else {
                        data = dayOfMonth.toString() + "/" + "0" + (monthOfYear + 1) + "/" + year
                    }

                    ed_data_venda.setText(data)

                }, ano, mes, dia)

            dpd.show()
        }
    }

    fun datePickDataCompra() {
        val calendar_cobranca = Calendar.getInstance()
        val dia_cobranca = calendar_cobranca[Calendar.DAY_OF_MONTH]
        val mes_cobranca = calendar_cobranca[Calendar.MONTH]
        val ano_cobranca = calendar_cobranca[Calendar.YEAR]

        btn_data_cobranca.setOnClickListener {
            ed_data_venda.setText("")
            val dpd = DatePickerDialog(
                requireActivity(), { view, year, monthOfYear, dayOfMonth ->
                    var data: String

                    if(monthOfYear.toString().length > 1){
                        data = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    } else {
                        data = dayOfMonth.toString() + "/" + "0" + (monthOfYear + 1) + "/" + year
                    }

                    ed_data_cobranca.setText(data)

                }, ano_cobranca, mes_cobranca, dia_cobranca)

            dpd.show()
        }
    }

}