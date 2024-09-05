package com.yunju.myfridge.ui.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.yunju.myfridge.R
import com.yunju.myfridge.databinding.DialogProductDetailBinding
import com.yunju.myfridge.models.Product
import java.io.Serializable
import java.util.Locale

class ProductDetailDialog: AppCompatDialogFragment() {
    companion object {
        private const val EXTRA_PRODUCT_DIALOG_TAG = "PRODUCT_DIALOG"
        var addProduct: ((Product) -> Unit?)? = null
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())

        fun newInstance(activity:AppCompatActivity, addProduct: (Product) -> Unit) {
            val productDetailDialog = ProductDetailDialog()
            productDetailDialog.show(activity.supportFragmentManager, EXTRA_PRODUCT_DIALOG_TAG)
            this.addProduct = addProduct
        }

        fun newInstance(activity:AppCompatActivity, argInfo: ArgInfo,  addProduct: (Product) -> Unit) {
            val productDetailDialog = ProductDetailDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_PRODUCT_DIALOG_TAG, argInfo)
                }
            }

            productDetailDialog.show(activity.supportFragmentManager, EXTRA_PRODUCT_DIALOG_TAG)
            this.addProduct = addProduct
        }
    }

    data class ArgInfo(
        var productName: String?,
        var productAddedDate: String?,
        var productExpireDate: String?
    ): Serializable

    lateinit var binding : DialogProductDetailBinding
    private var argInfo: ArgInfo? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_product_detail, null, false)
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        argInfo = arguments?.getSerializable(EXTRA_PRODUCT_DIALOG_TAG) as? ArgInfo

        setLayout()
        return dialog
    }

    private fun setLayout() {
        with(binding) {
            argInfo?.let {
                productNameEdit.setText(it.productName)
                productNameEdit.isEnabled = false
                productAddedDateEdit.text = it.productAddedDate
                productExpireDateEdit.text = it.productExpireDate
            } ?: run {
                val date = Calendar.getInstance().time
                productAddedDateEdit.text = dateFormat.format(date)
            }

            productAddedDateEdit.setOnClickListener {
                showDatePickerDialog(productAddedDateEdit)
            }

            productExpireDateEdit.setOnClickListener {
                showDatePickerDialog(productExpireDateEdit)
            }

            btnAdd.setOnClickListener {
                val productName = productNameEdit.text.toString()
                val productAddedDate = productAddedDateEdit.text.toString()
                val productExpireDate = productExpireDateEdit.text.toString()

                if (productName.isEmpty()) {
                    Toast.makeText(context, "상품명은 필수 입력 값 입니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (productAddedDate.isEmpty()) {
                    Toast.makeText(context, "상품 입고일은 필수 입력 값 입니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                addProduct?.invoke(Product(productName, productAddedDate, productExpireDate))
                dialog?.dismiss()
            }

            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

    private fun showDatePickerDialog(textView: TextView) {
        val selectDate = Calendar.getInstance()

        try {
            selectDate.time = dateFormat.parse(textView.text.toString())
        } catch (e: Exception) {
            Log.d("yj", "getCalendar() Exception : $e")
        }

        val listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            selectDate.set(year, month, day)
            textView.text = dateFormat.format(selectDate.time)
        }

        val datePickerDialog = DatePickerDialog(requireContext(), listener, selectDate.get(Calendar.YEAR), selectDate.get(Calendar.MONDAY), selectDate.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }
}