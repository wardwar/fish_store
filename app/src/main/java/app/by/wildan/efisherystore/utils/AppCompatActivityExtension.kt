package app.by.wildan.efisherystore.utils

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import app.by.wildan.efisherystore.Event


fun Activity.toast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.contentView(): View = findViewById<View>(android.R.id.content)


/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun Activity.setupToast(
    lifecycleOwner: LifecycleOwner,
    toastEvent: LiveData<Event<Any>>
) {
    toastEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is Int -> {
                    toast(this.getString(it))
                }
                is String -> {
                    toast(it)
                }
            }

        }
    })
}


