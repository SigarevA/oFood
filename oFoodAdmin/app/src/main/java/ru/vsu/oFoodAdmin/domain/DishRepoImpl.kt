package ru.vsu.oFoodAdmin.domain

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.renderscript.RenderScript
import android.util.Log
import kotlinx.coroutines.delay
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.vsu.oFoodAdmin.entities.Dish
import ru.vsu.oFoodAdmin.network.api.DishAPI
import ru.vsu.oFoodAdmin.network.response.DishResponse
import ru.vsu.oFoodAdmin.network.response.StatusResponse
import ru.vsu.oFoodAdmin.ui.addingdish.AddingDishScreenContent
import java.io.File
import java.io.IOException
import javax.inject.Inject

class DishRepoImpl @Inject constructor(
    private val dishAPI: DishAPI,
    private val context: Context
) : DishRepo {
    override suspend fun getDishes(): Result<List<Dish>> {
        try {
            delay(3000L)
            return Result.Success(
                listOf(
                    Dish(
                        id = "dsa",
                        price = 234.0,
                        name = "Пицца",
                        description = "Пицца пицца пицца",
                        photoPath = "dasd"
                    ),
                    Dish(
                        id = "dsa5",
                        price = 234.0,
                        name = "Сендвич",
                        description = "Пицца пицца пицца",
                        photoPath = "dasd"
                    ),
                    Dish(
                        id = "dsa9",
                        price = 234.0,
                        name = "Гамбургер",
                        description = "Пицца пицца пицца",
                        photoPath = "dasd"
                    )
                )
            )
        } catch (ex: Exception) {
            return Result.Error(IOException())
        }
    }

    override suspend fun getCurrentDishes(): Result<List<DishResponse>> {
        return try {
            Result.Success(dishAPI.getCurrentDishes())
        } catch (ex: Exception) {
            Log.e("DishRepo", "currentDishes", ex)
            Result.Error(ex)
        }
    }

    override suspend fun createDish(addingDishScreenContent: AddingDishScreenContent): Result<String> {
        val inputSteam =
            context.contentResolver.openInputStream(addingDishScreenContent.imageUri!!)!!
        val type = context.contentResolver.getType(addingDishScreenContent.imageUri!!) ?: "image/form-data"
        Log.d("TAG", context.contentResolver.getType(addingDishScreenContent.imageUri!!) ?: "")
        val requestFile = inputSteam.readBytes().let {
            it.toRequestBody(
                type.toMediaTypeOrNull(),
                0, it.size
            )
        }
        Log.d("TAG", "content : $addingDishScreenContent")
        //    val file = File(getRealPathFromURI(addingDishScreenContent.imageUri!!)!!)
        //  val requestFile = file.asRequestBody("multipart/form-data".toMediaType())
        val body = MultipartBody.Part.createFormData("image", "", requestFile)
        val nameRequest =
            addingDishScreenContent.name.text.toRequestBody("multipart/form-data".toMediaType())
        val priceRequest =
            addingDishScreenContent.price.text.toRequestBody("multipart/form-data".toMediaType())
        val descriptionRequest =
            addingDishScreenContent.description.text.toRequestBody("multipart/form-data".toMediaType())
        val categoryId =
            addingDishScreenContent.categoryId!!.toRequestBody("multipart/form-data".toMediaType())
        return try {
            Result.Success(
                dishAPI.createDish(
                    nameRequest,
                    categoryId,
                    priceRequest,
                    body,
                    descriptionRequest
                )
            )
        } catch (ex: Exception) {
            Log.e("TAG", "createDish", ex)
            Result.Error(ex)
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? =
            context.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}