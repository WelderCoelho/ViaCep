package com.welder.viacep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.welder.viacep.api.Api
import com.welder.viacep.databinding.ActivityMainBinding
import com.welder.viacep.model.Endereco
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //configurando o retrofit

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://viacep.com.br/ws/")
            .build()
            .create(Api::class.java)


        binding.buscar.setOnClickListener {

            val  cep = binding.campocep.text.toString()

            if (cep.isEmpty()){
                Toast.makeText(this, "Preencha o cep!", Toast.LENGTH_SHORT).show()
            }else{
                retrofit.setEndereco(cep).enqueue(object : Callback<Endereco>{
                    override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                        if (response.code() == 200){
                            val logradouro = response.body()?.logradouro.toString()
                            val localidade = response.body()?.localidade.toString()
                            val bairro = response.body()?.bairro.toString()
                            val uf = response.body()?.uf.toString()
                            setFormularios(logradouro,bairro,localidade,uf)
                        }else{
                            Toast.makeText(applicationContext,"Cep Invalido", Toast.LENGTH_SHORT ).show()                    }
                    }

                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                     Toast.makeText(applicationContext,"Erro inesperado!", Toast.LENGTH_SHORT ).show()                    }

                })
            }

        }

    }

    private fun setFormularios(logradouro: String, bairro: String, localidade: String, uf : String){

        binding.campologradouro.setText(logradouro)
        binding.campobairro.setText(bairro)
        binding.campocidade.setText(localidade)
        binding.campoestado.setText(uf)
 }
}