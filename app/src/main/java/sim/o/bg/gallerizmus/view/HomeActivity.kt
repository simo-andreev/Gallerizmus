package sim.o.bg.gallerizmus.view

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import sim.o.bg.gallerizmus.R

class HomeActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setActionBar(toolbar)

        fragmentManager.beginTransaction().add(R.id.homeScreenFragmentContainer, GalleryFragment()).commit()
    }
}