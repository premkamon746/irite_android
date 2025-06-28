package com.csi.irite


import SyncRepository
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.ValueCallback
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.csi.irite.datainit.InitCheckList
import com.csi.irite.helper.TransferEvidenceHelper
import com.csi.irite.utils.Tools
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.csi.irite.data.repo.TokenManager
import com.csi.irite.room.dao.AssetReportDao
import com.csi.irite.room.dao.BomReportDao
import com.csi.irite.room.dao.CheckListHeadDao
import com.csi.irite.room.dao.ChecklistSaveDao
import com.csi.irite.room.dao.DistrictDao
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.dao.EvidenceDao
import com.csi.irite.room.dao.FireReportDao
import com.csi.irite.room.dao.HistoryDao
import com.csi.irite.room.dao.LifeReportDao
import com.csi.irite.room.database.AppDatabase
import com.csi.irite.ui.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.mapsforge.map.model.com.csi.irite.room.dao.EvidentBagDao
import java.util.Properties


class MainActivity : BaseActivity() {
    private var actionBar: ActionBar? = null
    private var toolbar: Toolbar? = null
    private lateinit var progressBar: ProgressBar

    private lateinit var syncProgressBar: ProgressBar
    private lateinit var syncStatusText: TextView
    var db: AppDatabase? = null

    private lateinit var syncRepository: SyncRepository

    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null
    private var cameraPhotoUri: Uri? = null

    // ActivityResultLauncher for file selection
    private val fileChooserLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (fileUploadCallback == null) {
                return@registerForActivityResult
            }

            var results: Array<Uri>? = null
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data == null) {
                    // If no data is returned, it might be a camera capture
                    if (cameraPhotoUri != null) {
                        results = arrayOf(cameraPhotoUri!!)
                    }
                } else {
                    val dataString = result.data?.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    } else {
                        // Handle multiple files
                        val clipData = result.data?.clipData
                        if (clipData != null) {
                            val uriList = mutableListOf<Uri>()
                            for (i in 0 until clipData.itemCount) {
                                uriList.add(clipData.getItemAt(i).uri)
                            }
                            results = uriList.toTypedArray()
                        }
                    }
                }
            }

            fileUploadCallback?.onReceiveValue(results)
            fileUploadCallback = null
            cameraPhotoUri = null // Clear camera URI after use
        }

    // ActivityResultLauncher for camera capture
    private val cameraLauncher: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (fileUploadCallback == null) {
                return@registerForActivityResult
            }

            if (success) {
                fileUploadCallback?.onReceiveValue(arrayOf(cameraPhotoUri!!))
            } else {
                fileUploadCallback?.onReceiveValue(null)
            }
            fileUploadCallback = null
            cameraPhotoUri = null // Clear camera URI after use
        }

    // ActivityResultLauncher for requesting permissions
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                // Permissions granted, try showing file chooser again
                Toast.makeText(this, "Permissions granted. Please try selecting a file again.", Toast.LENGTH_SHORT).show()
                // You might need to trigger the file chooser again if it was initiated by a web page
                // However, the typical flow is that the user clicks the input again after granting permissions.
            } else {
                Toast.makeText(this, "Permissions denied. Cannot access storage/camera.", Toast.LENGTH_LONG).show()
                fileUploadCallback?.onReceiveValue(null) // Cancel file upload
                fileUploadCallback = null
                cameraPhotoUri = null
            }
        }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // WRITE_EXTERNAL_STORAGE is deprecated after API 28, so only request for older versions
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()
        setContentView(R.layout.activity_main)
        initToolbar()
        initNavigationMenu()
        initBottombar()
        progressBar = findViewById(R.id.progressBar)

        syncProgressBar = findViewById(R.id.syncProgressBar)
        syncStatusText = findViewById(R.id.syncStatusText)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        sync() //sync data to server

        val bottomNav = findViewById<BottomNavigationView>(R.id.navigation)
        bottomNav.setOnItemSelectedListener {

                when (it.itemId) {
                    //R.id.check_list  -> loadFragment(ChecklistMainFragment())
                    R.id.home  -> loadFragment(HomeFragment())
                    R.id.sync  -> sync()
                    R.id.navigation_books  -> loadFragment(IncidentFragment())
                    else -> false
                }
                true
            }

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.home
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                // For example, if you want to show a dialog before exiting the app
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes") { _, _ ->
                        finish() // or call super.onBackPressed() if you need default behavior
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        })
    }

    private fun someFunction(text: String) {
        // Accessing MainActivity properties and methods directly within the function
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar!!.setNavigationIcon(R.drawable.ic_menu)
        toolbar!!.getNavigationIcon()!!
            .setColorFilter(resources.getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
        actionBar!!.title = ""
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)/**/

    }

    private fun initNavigationMenu() {
        val nav_view = findViewById<View>(R.id.nav_view) as NavigationView
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                //R.id.check_list  -> loadFragment(ChecklistMainFragment())
                R.id.home  -> loadFragment(HomeFragment())
                R.id.nav_user  -> loadFragment(WUsersFragment())
                R.id.logout  -> {
                    TokenManager.clearTokens()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
            actionBar!!.title = item.title
            drawer.closeDrawers()
            true
        }

        // open drawer at start
        //drawer.openDrawer(GravityCompat.START)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_chat_bbm, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.grey_60))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            //finish();
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    /*override fun onBackPressed() {
        super.onBackPressed()
    }*/

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            // If there are fragments in the back stack, pop back to the previous one
            supportFragmentManager.popBackStack()
        } else {
            // If there's only one fragment left in the back stack, finish the activity
            super.onBackPressed()
        }
    }

    private fun initBottombar() {
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    fun updateProgress(progress: Int) {
        progressBar.progress = progress
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun syncOnStartup() {
        syncProgressBar.visibility = View.VISIBLE
        syncStatusText.visibility = View.VISIBLE
        syncStatusText.text = "Syncing..."

        lifecycleScope.launch {
            try {
                // your sync code here
                syncRepository.syncAll()
                syncStatusText.text = "Sync complete"
            } catch (e: Exception) {
                syncStatusText.text = "Sync failed"
            } finally {
                syncProgressBar.visibility = View.GONE
                delay(2000)
                syncStatusText.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sync(){
        val assetReportDao: AssetReportDao = db!!.assetReportDao()
        val bomReportDao: BomReportDao = db!!.bomReportDao()
        val checkListHeadDao: CheckListHeadDao = db!!.checkListHeadDao()
        val checklistSaveDao: ChecklistSaveDao = db!!.checklistSaveDao()
        val eventReportDao: EventReportDao = db!!.eventReportDao()
        val evidenceDao: EvidenceDao = db!!.evidenceDao()
        val evidentBagDao: EvidentBagDao = db!!.evidentBagDao()
        val fireReportDao: FireReportDao = db!!.fireReportDao()
        val lifeReportDao: LifeReportDao = db!!.lifeReportDao()
        val districtDao: DistrictDao = db!!.districtDao()
        val historyDao: HistoryDao = db!!.historyDao()

        syncRepository = SyncRepository(assetReportDao, bomReportDao, checkListHeadDao
            , checklistSaveDao, eventReportDao, evidenceDao
            , evidentBagDao, fireReportDao, lifeReportDao, districtDao,historyDao)


        syncOnStartup()
    }
}
