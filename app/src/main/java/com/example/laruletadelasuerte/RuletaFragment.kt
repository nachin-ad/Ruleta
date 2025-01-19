package com.example.laruletadelasuerte

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlin.random.Random
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat

class RuletaFragment : Fragment() {
    private var rondaActual = 2
    private lateinit var ruletaSections: Array<String>
    private var isAnimating = false // Estado para controlar la animación de escalado
    private lateinit var ivRuleta: ImageView
    private var scaleX: ObjectAnimator? = null
    private var scaleY: ObjectAnimator? = null


    private val ruleta1Sections = arrayOf(
        "0", "1/2", "x2", "Pierde turno", "200", "50", "100", "Quiebra", "75", "0", "25", "100",
        "50", "Vocales", "x2", "Pierde turno", "125", "1/2", "75", "Quiebra", "200", "50", "125", "25"
    )

    private val ruleta2Sections = arrayOf(
        "0", "1/2", "x2", "Pierde turno", "200", "50", "100", "Quiebra", "BOTE", "0", "25", "100",
        "50", "Vocales", "x2", "Pierde turno", "125", "1/2", "75", "Quiebra", "200", "50", "125", "25"
    )

    private val ruleta3Sections = arrayOf(
        "L", "A", "*", "R", "U", "L", "E", "T", "A", "*", "D", "E",
        "*", "L", "A", "*", "S", "U", "E", "R", "T", "E", "*", "*"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.fragment_ruleta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSpin = view.findViewById<Button>(R.id.btnGirar)
        ivRuleta = view.findViewById(R.id.ivGirar)

        configurarRuleta(view)

        btnSpin.isClickable = true
        btnSpin.setOnClickListener {
            stopAnimation()
            btnSpin.isClickable = false
            girarRuleta(view)
        }

        startAnimation(ivRuleta)
    }

    private fun configurarRuleta(view: View) {
        val ivRuleta = view.findViewById<ImageView>(R.id.imageView2)
        val btnSpin = view.findViewById<Button>(R.id.btnGirar)

        when (rondaActual) {
            1, 2, 3 -> {
                ruletaSections = ruleta1Sections
                ivRuleta.setImageResource(R.drawable.ruletaprincipal)
                btnSpin.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.morado_ruleta))

            }
            4 -> {
                ruletaSections = ruleta2Sections
                ivRuleta.setImageResource(R.drawable.ruletabote)
                btnSpin.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.azul_bote))
            }
            5 -> {
                ruletaSections = ruleta3Sections
                ivRuleta.setImageResource(R.drawable.ruletafinal)
                btnSpin.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.azul_final))
            }
        }
    }

    private fun girarRuleta(view: View) {
        val ivRuleta = view.findViewById<ImageView>(R.id.imageView2)

        val randomDegree = (5 * 360) + Random.nextInt(0, 360)
        val pivotX = ivRuleta.width / 2f
        val pivotY = ivRuleta.height / 2f

        val rotateAnimation = RotateAnimation(
            0f,
            randomDegree.toFloat(),
            pivotX,
            pivotY
        ).apply {
            duration = 5000
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val finalDegree = randomDegree % 360
                    val result = calcularResultado(finalDegree)
                    Toast.makeText(activity, "Resultado de la ruleta: " + result, Toast.LENGTH_LONG).show()

                    // Retraso de 1 segundo antes de cambiar de fragmento
                    Handler(Looper.getMainLooper()).postDelayed({
                        mostrarConsonantes()
                    }, 1200)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }

        ivRuleta.startAnimation(rotateAnimation)
    }

    private fun mostrarConsonantes() {
        // Reemplaza el fragmento actual con el nuevo
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, ConsonantesFragment())
            .addToBackStack(null) // Opcional: para permitir navegación hacia atrás
            .commit()
    }

    private fun calcularResultado(degree: Int): String {
        val gradosPorSeccion = 360 / ruletaSections.size
        val section = degree / gradosPorSeccion
        return ruletaSections[((ruletaSections.size - section) % ruletaSections.size)]
    }

    private fun startAnimation(ivRuleta: ImageView) {
        if (isAnimating) return // Evitar múltiples animaciones simultáneas

        // Configura la animación de escala en ambos ejes
        scaleX = ObjectAnimator.ofFloat(ivRuleta, "scaleX", 1.0f, 1.3f, 1.0f).apply {
            duration = 1800
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        scaleY = ObjectAnimator.ofFloat(ivRuleta, "scaleY", 1.0f, 1.3f, 1.0f).apply {
            duration = 1800
            repeatCount = ObjectAnimator.INFINITE
            start()
        }

        isAnimating = true
    }

    private fun stopAnimation() {
        // Detener ambas animaciones si están activas
        scaleX?.cancel()
        scaleY?.cancel()
        isAnimating = false
    }
}