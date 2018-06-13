package sim.o.bg.gallerizmus.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.UriPermission
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.toolbar.*
import sim.o.bg.gallerizmus.R
import java.io.File

class HomeActivity : Activity(){

    private val REQUEST_CODE = 0b0000_1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setActionBar(toolbar)

        fragmentManager.beginTransaction().add(R.id.homeScreenFragmentContainer, GalleryFragment()).commit()

        requestImageDirAccess()
    }

    private fun requestImageDirAccess() {
        val sm = getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val volume: StorageVolume = sm.primaryStorageVolume
        volume.createAccessIntent(Environment.DIRECTORY_PICTURES).also { intent ->
            startActivityForResult(intent, REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE){

            if (resultCode != RESULT_OK){
                showAccessDenied()
            } else {
                data!!
                val uri = data.data
                if (!contentResolver.persistedUriPermissions.any { it.uri == uri }){
                    contentResolver.takePersistableUriPermission(data.data, FLAG_GRANT_WRITE_URI_PERMISSION)
                    Toast.makeText(this, "ADDING PURMMISHNZ!", 0).show()
                }

                Toast.makeText(this, data.data.path, 0).show()
            }
        }
    }

    private fun showAccessDenied() {
        AlertDialog.Builder(this)
                .setTitle(R.string.image_dir_access_denied_title)
                .setMessage(R.string.image_dir_access_denied_message)
                .setOnDismissListener { requestImageDirAccess() }
                .create()
                .show()
    }
}