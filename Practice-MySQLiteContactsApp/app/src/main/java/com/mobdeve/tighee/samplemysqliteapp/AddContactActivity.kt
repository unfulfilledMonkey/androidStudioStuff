package com.mobdeve.tighee.samplemysqliteapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.tighee.samplemysqliteapp.databinding.ActivityAddContactBinding
import com.squareup.picasso.Picasso
import java.util.concurrent.Executors

class AddContactActivity : AppCompatActivity() {
    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var viewBinding : ActivityAddContactBinding
    private lateinit var myDbHelper: MyDbHelper
    private var imageUri: Uri? = null
    // We added a contactId variable so we can keep track of where we should edit in the DB.
    // We assign it -1 as the default value.
    private var contactId: Long = -1

    /*
    *   Note that we added a TextView for the title of the activity's layout so we can change it
    *   from "Add" to "Edit" depending on what the user wants to do. Since we're using ViewBinding,
    *   we should find it there.
    * */

    private val myActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            try {
                if (result.data != null) {
                    imageUri = result.data!!.data
                    Picasso.get().load(imageUri).into(viewBinding.tempImageIv)
                }
            } catch (exception: Exception) {
                Log.d("TAG", "" + exception.localizedMessage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialization of our Views through ViewBinding
        this.viewBinding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        // Check if an intent is there (i.e. if its coming for an EDIT operation)
        val i = intent
        this.contactId = i.getLongExtra(IntentKeys.CONTACT_ID_KEY.name, -1)

        // If the contactId is default, it means there's no intent. If there is a value other than
        // -1, then we need to edit.
        if (this.contactId != -1L) {
            this.viewBinding.firstNameEtv.setText(i.getStringExtra(IntentKeys.FIRST_NAME_KEY.name))
            this.viewBinding.lastNameEtv.setText(i.getStringExtra(IntentKeys.LAST_NAME_KEY.name))
            this.viewBinding.numberEtv.setText(i.getStringExtra(IntentKeys.NUMBER_KEY.name))
            imageUri = Uri.parse(i.getStringExtra(IntentKeys.IMAGE_URI_KEY.name))
            Picasso.get().load(imageUri).into(this.viewBinding.tempImageIv)

            // Para alam ni user na edit ang gagawin, hindi add
            this.viewBinding.addBtn.text = "EDIT CONTACT"
            this.viewBinding.titleTv.text = "Edit Contact"
        }

        this.viewBinding.selectBtn.setOnClickListener(View.OnClickListener {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_OPEN_DOCUMENT
            myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"))
        })

        // Note that while this is logic for the "add" button, it also should contain logic that
        // handles when an edit operation is performed.
        this.viewBinding.addBtn.setOnClickListener(View.OnClickListener { view ->
            if (areFieldsComplete()) {
                /*
                 * TODO: Handle the logic for knowing whether its an add or edit operation.
                 *       HINT: Utilize whether or not the contactId is the default value.
                 * */
                executorService.execute {
                    // This is logic for adding a Contact
                    myDbHelper = MyDbHelper.getInstance(this@AddContactActivity)!!
                    if (contactId == -1L) {
                        // ADD
                        val newId = myDbHelper.insertContact(
                            Contact(
                                viewBinding.lastNameEtv.text.toString(),
                                viewBinding.firstNameEtv.text.toString(),
                                viewBinding.numberEtv.text.toString(),
                                imageUri.toString()
                            )
                        )

                        val i = Intent().apply {
                            putExtra(IntentKeys.CONTACT_ID_KEY.name, newId)
                            putExtra(IntentKeys.FIRST_NAME_KEY.name, viewBinding.firstNameEtv.text.toString())
                            putExtra(IntentKeys.LAST_NAME_KEY.name, viewBinding.lastNameEtv.text.toString())
                            putExtra(IntentKeys.NUMBER_KEY.name, viewBinding.numberEtv.text.toString())
                            putExtra(IntentKeys.IMAGE_URI_KEY.name, imageUri.toString())
                        }
                        setResult(ResultCodes.ADD_RESULT.ordinal, i)
                    }else {
                        // EDIT
                        myDbHelper.updateContact(
                            Contact(
                                viewBinding.lastNameEtv.text.toString(),
                                viewBinding.firstNameEtv.text.toString(),
                                viewBinding.numberEtv.text.toString(),
                                imageUri.toString(),
                                contactId
                            )
                        )

                        val i = Intent().apply {
                            putExtra(IntentKeys.CONTACT_ID_KEY.name, contactId)
                            putExtra(
                                IntentKeys.FIRST_NAME_KEY.name,
                                viewBinding.firstNameEtv.text.toString()
                            )
                            putExtra(
                                IntentKeys.LAST_NAME_KEY.name,
                                viewBinding.lastNameEtv.text.toString()
                            )
                            putExtra(
                                IntentKeys.NUMBER_KEY.name,
                                viewBinding.numberEtv.text.toString()
                            )
                            putExtra(IntentKeys.IMAGE_URI_KEY.name, imageUri.toString())
                        }
                        setResult(ResultCodes.EDIT_RESULT.ordinal, i)
                    }
                }
                finish()
            } else {
                Toast.makeText(view.context, "Please fill up all fields", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun areFieldsComplete(): Boolean {
        return this.viewBinding.firstNameEtv.text.isNotEmpty() && this.viewBinding.lastNameEtv.text.isNotEmpty() && this.viewBinding.numberEtv.text.isNotEmpty() && (imageUri != null)
    }
}