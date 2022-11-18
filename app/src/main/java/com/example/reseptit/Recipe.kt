package com.example.reseptit

import android.content.Context
import androidx.room.*

@Entity(tableName = "recipe")
data class Recipe(
    // Aina kun tätä classia muokataan niin poista sovellus emulaattorista, tai tyhjennä tiedot
    // ennen uudelleenajoa (Run 'app'), muuten crashaa.

    val name: String?,
    val description: String?,
    val ingredients: String?, // String = faster development? could be swapped later to a proper list/etc.
    @ColumnInfo(name = "cooking_instructions") val cookingInstructions: String?,
    @ColumnInfo(name = "cooking_time_estimate") val cookingTimeEstimate: Float?, // Could add time units selection, but maybe default to hours.
    @ColumnInfo(name = "image_uri") val imageUri: String?,

    // Tämä pitää olla pohjalla
    @PrimaryKey(autoGenerate = true) val rid: Int = 0
)

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE rid IN (:recipeIds)")
    fun loadAllByIds(recipeIds: IntArray): List<Recipe>

    @Query("SELECT * FROM recipe WHERE rid LIKE :recipeId LIMIT 1")
    fun findRecipeByRid(recipeId: Int): Recipe

    @Query("SELECT * FROM recipe WHERE name LIKE :recipeName LIMIT 5")
    fun findRecipesByName(recipeName: String): List<Recipe>

    @Insert
    fun insertAll(vararg recipes: Recipe)

    @Insert
    fun insertOne(recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)
}

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {

        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): RecipeDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                "recipe_database",
            )
                .allowMainThreadQueries().build()
            // allowMainThreadQueries hidastaa sovellusta paljon kun reseptejä tulee enemmän
            // mutta jätetty väliaikaisratkasuks
        }
    }
}