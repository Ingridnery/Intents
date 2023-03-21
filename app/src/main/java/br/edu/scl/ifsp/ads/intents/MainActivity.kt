package br.edu.scl.ifsp.ads.intents

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                //fazer a chamada
            }else{
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                finish()
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
                    //checar permissao
                }
                else{
                    //fazer a chamada
                }
                true
            }
            R.id.dialMi -> true
            R.id.pickMi -> true
            R.id.chooserMi -> true
            else ->false
        }
    }

}