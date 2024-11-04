package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Stack

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val btnOpDiv: Button = findViewById(R.id.btnOpDiv)
        val btnOpMul: Button = findViewById(R.id.btnOpMul)
        val btnOpAdd: Button = findViewById(R.id.btnOpAdd)
        val btnOpSub: Button = findViewById(R.id.btnOpSub)
        val btnOpEq: Button = findViewById(R.id.btnOpEq)
        val btnClear: Button = findViewById(R.id.btnClear)
        val btnDelete: TextView = findViewById(R.id.btnDelete)
        val input: TextView = findViewById(R.id.input)
        val result: TextView = findViewById(R.id.result)
        val buttons = arrayOf(
            button0,
            button1,
            button2,
            button3,
            button4,
            button5,
            button6,
            button7,
            button8,
            button9,
            btnOpSub,
            btnOpDiv,
            btnOpAdd,
            btnOpMul
        )

        fun applyOperation(op: Char, b: Double, a: Double): Double {
            return when (op) {
                '+' -> a + b
                '-' -> a - b
                '*' -> a * b
                '/' -> a / b
                else -> 0.0
            }
        }

        fun hasPrecedence(op1: Char, op2: Char): Boolean {
            return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-')
        }

        fun calculateExpression(expression: String): Double {
            val numbers = Stack<Double>()
            val operations = Stack<Char>()

            var i = 0
            while (i < expression.length) {
                val c = expression[i]

                when {
                    c.isDigit() || c == '.' -> {
                        val sb = StringBuilder()
                        while (i < expression.length && (expression[i].isDigit() || expression[i] == '.')) {
                            sb.append(expression[i])
                            i++
                        }
                        numbers.push(sb.toString().toDouble())
                        i--
                    }

                    c in listOf('+', '-', '*', '/') -> {
                        while (operations.isNotEmpty()&& hasPrecedence(c, operations.peek())) {
                            numbers.push(
                                applyOperation(
                                    operations.pop(),
                                    numbers.pop(),
                                    numbers.pop()
                                )
                            )
                        }
                        operations.push(c)
                    }
                }
                i++
            }

            while (operations.isNotEmpty()) {
                numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()))
            }

            return numbers.pop()
        }

        fun formatResult(result: Double): String {
            return if (result % 1 == 0.0) {
                result.toLong().toString()
            } else {
                result.toString()
            }
        }

        for (button in buttons) {
            button.setOnClickListener {
                if (input.text == "0") {
                    input.text = ""
                }
                if (button.text.toString()[0].isDigit()) {
                    input.text = input.text.toString().plus(button.text)
                    val expression = input.text.toString()
                    result.text = formatResult(calculateExpression(expression))
                } else {
                    if (input.text.isNotEmpty() && input.text.last().isDigit()) {
                        input.text = input.text.toString().plus(button.text)
                    }
                }
            }
        }
        btnOpEq.setOnClickListener {
            input.text = result.text.toString()
            Toast.makeText(applicationContext, input.text, Toast.LENGTH_SHORT).show()
        }
        btnClear.setOnClickListener {
            input.text = ""
            result.text = ""
        }
        btnDelete.setOnClickListener {
            input.setText(input.text.toString().dropLast(1))
            if (input.text.toString().isNotEmpty()) {
                val expression = input.text.toString()
                if (input.text.toString().last().isDigit()) {
                    result.text = formatResult(calculateExpression(expression))
                } else {
                    result.text = formatResult(calculateExpression(expression.dropLast(1)))
                }
            } else {
                result.text = "0"
            }
        }
    }
}

