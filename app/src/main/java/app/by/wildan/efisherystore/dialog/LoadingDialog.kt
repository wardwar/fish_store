package app.by.wildan.efisherystore.dialog

import android.app.Dialog
import android.content.Context
import app.by.wildan.efisherystore.R


class LoadingDialog(private val context: Context) {
    private var dialog: Dialog? = null
    val isShowing
        get() = dialog?.isShowing ?: false

    private fun show() {
        dialog = Dialog(context)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.show()
    }

    private fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }


    fun perform(isShow:Boolean){
        if(isShow){
            show()
        }else{
            dismiss()
        }
    }
}
