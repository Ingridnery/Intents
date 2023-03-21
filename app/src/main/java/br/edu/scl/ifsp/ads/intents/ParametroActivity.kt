package br.edu.scl.ifsp.ads.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.scl.ifsp.ads.intents.MainActivity.Constantes.PARAMETRO_EXTRA
import br.edu.scl.ifsp.ads.intents.databinding.ActivityParametroBinding

class ParametroActivity : AppCompatActivity() {
    private val apb: ActivityParametroBinding by lazy{
        ActivityParametroBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)
        intent.getStringExtra(PARAMETRO_EXTRA)?.let {parametro->
            apb.parametroEt.setText(parametro)
        }

        apb.enviarParametroBt.setOnClickListener {
            val intent = Intent()
            intent.putExtra(PARAMETRO_EXTRA, apb.parametroEt.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }


}