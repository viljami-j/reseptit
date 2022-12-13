package com.example.reseptit.ui.add


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.*
import android.provider.MediaStore
import android.util.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.reseptit.Recipe
import com.example.reseptit.databinding.FragmentAddBinding
import java.io.ByteArrayOutputStream


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding get() = _binding!!

    private lateinit var  addRecipesViewModel: AddViewModel

    lateinit var cameraButton: Button
    lateinit var libButton: Button
    lateinit var addButton: Button
    lateinit var selectedImage: ImageView
    companion object {
        // Muutetaan joksikin muuksi kun lisätään tietokantaan
        private const val pic_id = 123
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addRecipesViewModel = ViewModelProvider(this)[AddViewModel::class.java]
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        cameraButton = binding.cameraButton
        libButton = binding.libraryButton
        selectedImage = binding.selectedImage
        addButton = binding.addButton
        // Alustetaan listenerit
        cameraButton.setOnClickListener { openCamera() }
        libButton.setOnClickListener { openLibrary() }
        addButton.setOnClickListener { addRecipeToDB(addRecipesViewModel) }
        textFocusListener()

        return binding.root
    }

    private fun addRecipeToDB(addViewModel: AddViewModel) {
        val name: String = binding.inputName.text.toString()
        val desc: String = binding.inputDescription.text.toString()
        val ing: String = binding.inputIngredients.text.toString()
        val instr: String = binding.inputInstructions.text.toString()
        val time: Int? = Integer.parseInt(binding.inputTime.text.toString())

        val byteArrayOutputStream = ByteArrayOutputStream()
        val bitmap = (binding.selectedImage.drawable as BitmapDrawable).bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val imageString: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        val recipe = Recipe(name,desc,ing,instr,time,imageString)

        addRecipesViewModel.addRecipe(recipe)
        addViewModel.recipeAddedToast.show()
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val photo = data!!.extras!!["data"] as Bitmap?
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
        binding.inputDescription.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                binding.descriptionContainer.helperText = validIng()
            }
        }
        binding.inputInstructions.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                binding.instructionContainer.helperText = validManual()
            }
        }
    }
    //Tarkistetaan syötekentät
    private fun validManual(): String? {
        val manualText = binding.inputInstructions.text.toString()
        if(manualText.length<=5) return "Liian lyhyet ohjeet"

        return null
    }

    private fun validIng(): String? {
        val ingText = binding.inputDescription.text.toString()
        if(ingText.length<=5) return "Liian lyhyt aineslista ja/tai valmistuohje"

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