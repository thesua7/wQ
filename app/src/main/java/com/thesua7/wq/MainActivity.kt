package com.thesua7.wq

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.thesua7.wq.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    }

    override fun onStart() {
        super.onStart()

        binding.tvPrediction.setOnClickListener {
            if (initInputValidation()) {
                prediction()

            } else {
                Toast.makeText(this, "Please fill all elements properly", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun initInputValidation(): Boolean {
        if (binding.etPh.length() == 0 && binding.etHardness.length() == 0 && binding.etChloramines.length() == 0 && binding.etConductivity.length() == 0 && binding.etOrganicCarbon.length() == 0 && binding.etSolids.length() == 0 && binding.etSulfate.length() == 0 && binding.etTrihalomethanes.length() == 0 && binding.etTurbidity.length() == 0) {
            return false
        }
        return true
    }

    private fun prediction() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.132:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MyApiService::class.java)

        val hashMap = HashMap<String, String>()
        hashMap["ph"] = binding.etPh.text.toString()
        hashMap["hardness"] = binding.etHardness.text.toString()
        hashMap["solid"] = binding.etSolids.text.toString()
        hashMap["chloramine"] = binding.etChloramines.text.toString()
        hashMap["sulfate"] = binding.etSulfate.text.toString()
        hashMap["conductivity"] = binding.etConductivity.text.toString()
        hashMap["trihalomethane"] = binding.etTrihalomethanes.text.toString()
        hashMap["organic_carbon"] = binding.etOrganicCarbon.text.toString()
        hashMap["turbidity"] = binding.etTurbidity.text.toString()

        val result = apiService.postData(hashMap)
        result.enqueue(object : Callback<apiResponse> {
            override fun onResponse(call: Call<apiResponse>, response: Response<apiResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody!!.prediction =="1"){
                        binding.tvResult.text = "Drink!!"
                    }
                    if (responseBody!!.prediction=="0"){
                        binding.tvResult.text = "Don't Drink!!"
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<apiResponse>, t: Throwable) {
                binding.tvResult.text = call.toString()
            }
        })
    }
}