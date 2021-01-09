package adarsh.bolt.calculatorapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.lang.NumberFormatException
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.*

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND_STORED = "Operate_stored"

@Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
class MainActivity : AppCompatActivity() {

    //Variables to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listner = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listner)
        button1.setOnClickListener(listner)
        button2.setOnClickListener(listner)
        button3.setOnClickListener(listner)
        button4.setOnClickListener(listner)
        button5.setOnClickListener(listner)
        button6.setOnClickListener(listner)
        button7.setOnClickListener(listner)
        button8.setOnClickListener(listner)
        button9.setOnClickListener(listner)
        buttonDot.setOnClickListener(listner)

        val opListner = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }

        equal.setOnClickListener(opListner)
        add.setOnClickListener(opListner)
        divide.setOnClickListener(opListner)
        multiply.setOnClickListener(opListner)
        subtract.setOnClickListener(opListner)
        buttonRoot.setOnClickListener(opListner)
        buttonPow.setOnClickListener(opListner)

        buttonNeg.setOnClickListener { view ->
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    //newNumber was "-" or "." so clear it
                    newNumber.setText("")
                }
            }
        }

        buttonLog.setOnClickListener { view ->
            val value = newNumber.text.toString()
            var doubleValue = value.toDouble()
            doubleValue = log10(doubleValue)
            funcOperation(doubleValue, value)
        }

        buttonSin.setOnClickListener { view ->
            val value = newNumber.text.toString()
            var doubleValue = value.toDouble()
            doubleValue = sin(doubleValue)
            funcOperation(doubleValue, value)
        }

        buttonCos.setOnClickListener { view ->
            val value = newNumber.text.toString()
            var doubleValue = value.toDouble()
            doubleValue = cos(doubleValue)
            funcOperation(doubleValue, value)
        }

        buttonTan.setOnClickListener { view ->
            val value = newNumber.text.toString()
            var doubleValue = value.toDouble()
            doubleValue = tan(doubleValue)
            funcOperation(doubleValue, value)
        }

        buttonClear.setOnClickListener { view ->
            val value = 0
            operand1 = null
            if (value == 0) {
                result.setText("")
                operation.text = ""
            }
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN //handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
                "Root" -> operand1 = operand1!!.pow(1.0 / value)
                "Pow" -> operand1 = operand1!!.pow(value)

            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    private fun funcOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            pendingOperation = operation
            when (pendingOperation) {
                "log" -> operand1 = log10(value)
                "sin" -> operand1 = sin(value)
                "cos" -> operand1 = cos(value)
                "tan" -> operand1 = tan(value)
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND_STORED, false)) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            null
        }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        operation.text = pendingOperation
    }
}
