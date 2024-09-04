package com.yunju.myfridge.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.yunju.myfridge.R
import com.yunju.myfridge.databinding.DialogProductDetailBinding
import com.yunju.myfridge.models.Product

class ProductDetailDialog: AppCompatDialogFragment() {
    lateinit var binding : DialogProductDetailBinding

    companion object {
        var addProduct: ((Product) -> Unit?)? = null

        fun newInstance(activity:AppCompatActivity, addProduct: (Product) -> Unit) {
            val productDetailDialog = ProductDetailDialog()
            productDetailDialog.show(activity.supportFragmentManager, "ProductDialogTag")
            this.addProduct = addProduct
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_product_detail, null, false)
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)

        setLayout()
        return dialog
    }

    private fun setLayout() {
        with(binding) {
            btnAdd.setOnClickListener {
                val productName = producrNameEdit.text.toString()
                val productCount = productAddedEdit.text.toString()
                val productDate = producrExpireDateEdit.text.toString()

                if (productName.isEmpty() && productCount.isEmpty() && productDate.isEmpty()) {
                    Toast.makeText(context, "상품을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                addProduct?.invoke(Product(productName, productCount, productDate))
                dialog?.dismiss()
            }

            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }
}