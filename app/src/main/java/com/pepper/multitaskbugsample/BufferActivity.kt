package com.pepper.multitaskbugsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pepper.multitaskbugsample.MainActivity.Companion.DEST_EXTRA

class BufferActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buffer)
        val destinationIntents: ArrayList<Intent> =
            intent.extras?.getParcelableArrayList(DEST_EXTRA)!!
        startActivities(
            destinationIntents.toTypedArray().apply {
//                those are the exact flag values on Pepper app just before the final startActivities()
//                get(0).setFlags(268484608)
//                get(1).setFlags(603979776)
            }
        )
        finish()
    }

}
