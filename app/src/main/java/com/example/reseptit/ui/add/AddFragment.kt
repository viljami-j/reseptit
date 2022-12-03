package com.example.reseptit.ui.add


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.*
import androidx.camera.core.*
import androidx.core.content.*
import androidx.fragment.app.Fragment
import com.example.reseptit.databinding.FragmentAddBinding
import java.util.*

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding get() = _binding!!

    lateinit var cameraButton: Button
    lateinit var libButton: Button
    lateinit var selectedImage: ImageView
    companion object {
        // Muutetaan joksikin muuksi kun lisätään tietokantaan
        private const val pic_id = 123
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        cameraButton = binding.cameraButton
        libButton = binding.libraryButton
        selectedImage = binding.selectedImage
        // Alustetaan listenerit
        cameraButton.setOnClickListener { openCamera() }
        libButton.setOnClickListener { openLibrary() }
        textFocusListener()

        return binding.root
    }
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val photo = data!!.extras!!["data"] as Bitmap?
            Log.d("DATA",data!!.extras!!["data"].toString())
            // Set the image in imageview for display
            selectedImage.setImageBitmap(photo?.let { Bitmap.createScaledBitmap(it, 600,600, false) })
        }
    }
    private var resultLauncherLib = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val contentUri = data!!.data

            selectedImage.setImageURI(contentUri)
        }
    }
    private fun openCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(cameraIntent)
    }
    private fun openLibrary(){
        val libIntent = Intent(Intent.ACTION_GET_CONTENT)
        libIntent.type = "image/*"

        resultLauncherLib.launch(libIntent)
    }
    //Listeners:
    private fun textFocusListener() {
        binding.inputName.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                binding.nameContainer.helperText = validName()
            }
        }
        binding.inputTime.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                binding.timeContainer.helperText = validTime()
            }
        }
        binding.inputIngredients.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                binding.ingredientsContainer.helperText = validIng()
            }
        }
        binding.inputManual.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                binding.manualContainer.helperText = validManual()
            }
        }
    }
    //Tarkistetaan syötekentät
    private fun validManual(): String? {
        val manualText = binding.inputManual.text.toString()
        if(manualText.length<=5) return "Liian lyhyet ohjeet"

        return null
    }

    private fun validIng(): String? {
        val ingText = binding.inputIngredients.text.toString()
        if(ingText.length<=5) return "Liian lyhyt aineslista"

        return null
    }

    private fun validTime(): String? {
        val timeText = binding.inputTime.text.toString()
        if(timeText.length<=5) return "Vaaditaan vähintään 5 merkkiä"

        return null
    }

    private fun validName(): String? {
        val nameText = binding.inputName.text.toString()
        if(nameText.length<=5) return "Liian lyhyt nimi reseptille"

        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}