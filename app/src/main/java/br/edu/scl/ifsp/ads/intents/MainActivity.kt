package br.edu.scl.ifsp.ads.intents

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.ads.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var  parl : ActivityResultLauncher<Intent>
    private lateinit var  permissaoChamadaActivityResultLauncher: ActivityResultLauncher<String>
    private lateinit var pegarImagemActivityResultLauncher: ActivityResultLauncher<Intent>

    companion object Constantes{
        const val PARAMETRO_EXTRA = "PARAMETRO_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        parl=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
                if(result?.resultCode == RESULT_OK){
                    val retorno = result.data?.getStringExtra(PARAMETRO_EXTRA)?:""
                    amb.parametroTv.text = retorno
                }
        }

        amb.entrarParametrobt.setOnClickListener {
            //sai da tela atual e vai para a tela de parametro
            val intent = Intent("DIA_DE_SOL_ACTION")
            intent.putExtra("PARAMETRO_EXTRA", amb.parametroTv.text.toString())
            parl.launch(intent)
        }

        permissaoChamadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){permissaoConcedida ->
            if(permissaoConcedida){
                chamarNumero(true)
            }else{
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        pegarImagemActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado ->
            if(resultado?.resultCode == RESULT_OK){
                val imagemUri = resultado.data?.data
                imagemUri?.let {
                    amb.parametroTv.text = it.toString()
                    //abrindo imagem para a visualização
                    val visualizarImagemIntent = Intent(Intent.ACTION_VIEW, it)
                    startActivity(visualizarImagemIntent)
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.viewMi -> {
                val url: Uri = Uri.parse(amb.parametroTv.text.toString())
                val navegadorIntentt: Intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(navegadorIntentt)
                true
            }
            R.id.callMi -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                   if(checkSelfPermission(CALL_PHONE)== PERMISSION_GRANTED){
                          chamarNumero(true)
                   }
                    else{
                        permissaoChamadaActivityResultLauncher.launch(CALL_PHONE)
                    }
                }
                else{
                    chamarNumero(true)
                }
                true
            }
            R.id.dialMi ->{
                //nao precisa de permissão adicional, pq o usuario usa o teclado para digitar o numero
                chamarNumero(false)
                true
            }
            R.id.pickMi -> {
                //vamos abrir uma imagem da galeria
                val pegarImagemIntent: Intent = Intent(Intent.ACTION_PICK)
                val diretorioImagens = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
                pegarImagemIntent.setDataAndType(Uri.parse(diretorioImagens), "image/*")
                pegarImagemActivityResultLauncher.launch(pegarImagemIntent)

                true}
            R.id.chooserMi -> {
                val url: Uri = Uri.parse(amb.parametroTv.text.toString())
                var navegadorIntentt: Intent = Intent(Intent.ACTION_VIEW, url)
                val escolherAppIntent: Intent = Intent(ACTION_CHOOSER)
                escolherAppIntent.putExtra(EXTRA_TITLE, "Escolha seu navegador preferido")
                escolherAppIntent.putExtra(EXTRA_INTENT, navegadorIntentt)
                startActivity(escolherAppIntent)
                true}
            else ->false
        }
    }


    private fun chamarNumero(chamar: Boolean){
        val numeroUri = Uri.parse("tel:${amb.parametroTv.text}")
        val chamarIntent = Intent(if(chamar)ACTION_CALL else ACTION_DIAL, numeroUri)
        startActivity(chamarIntent)
    }


}